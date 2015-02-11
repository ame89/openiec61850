/*
 * Copyright 2011-14 Fraunhofer ISE, energy & meteo Systems GmbH and other contributors
 *
 * This file is part of OpenIEC61850.
 * For more information visit http://www.openmuc.org
 *
 * OpenIEC61850 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * OpenIEC61850 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with OpenIEC61850.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.openiec61850;

import org.openmuc.josistack.AcseAssociation;
import org.openmuc.josistack.ServerAcseSap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * The <code>ServerSap</code> class represents the IEC 61850 service access point for server applications. It
 * corresponds to the AccessPoint defined in the ICD/SCL file. A server application that is to listen for client
 * connections should first get an instance of <code>ServerSap</code> using the static function
 * ServerSap.getSapsFromSclFile(). Next all the necessary configuration parameters can be set. Finally the
 * <code>startListening</code> function is called to listen for client associations. Changing properties of a ServerSap
 * after starting to listen is not recommended and has unknown effects.
 */
public final class ServerSap {

    private final static Logger logger = LoggerFactory.getLogger(ServerSap.class);

    static final int MINIMUM_MMS_PDU_SIZE = 64;
    private static final int MAXIMUM_MMS_PDU_SIZE = 65000;

    private int proposedMaxMmsPduSize = 65000;
    private int proposedMaxServOutstandingCalling = 5;
    private int proposedMaxServOutstandingCalled = 5;
    private int proposedDataStructureNestingLevel = 10;
    byte[] servicesSupportedCalled = new byte[]{(byte) 0xee, 0x1c, 0, 0, 0x04, 0x08, 0, 0, 0x79, (byte) 0xef, 0x18};
    byte[] cbbBitString = {(byte) (0xfb), 0x00};

    ServerEventListener serverEventListener;
    private ServerAcseSap acseSap;

    private final String name;
    private int port = 102;
    private int backlog = 0;
    private InetAddress bindAddr = null;
    private ServerSocketFactory serverSocketFactory = null;

    Timer timer;

    List<ServerAssociation> associations = new ArrayList<ServerAssociation>();
    boolean listening = false;

    final ServerModel serverModel;

    public static List<ServerSap> getSapsFromSclFile(String sclFilePath) throws SclParseException {
        SclParser sclParserObject = new SclParser();
        sclParserObject.parse(sclFilePath);
        return sclParserObject.getServerSaps();
    }

    public static List<ServerSap> getSapsFromSclFile(InputStream sclFileStream) throws SclParseException {
        SclParser sclParserObject = new SclParser();
        sclParserObject.parse(sclFileStream);
        return sclParserObject.getServerSaps();
    }

    /**
     * Creates a ServerSap.
     *
     * @param port                local port to listen on for new connections
     * @param backlog             The maximum queue length for incoming connection indications (a request to connect) is set to the
     *                            backlog parameter. If a connection indication arrives when the queue is full, the connection is
     *                            refused. Set to 0 or less for the default value.
     * @param bindAddr            local IP address to bind to, pass null to bind to all
     * @param serverSocketFactory the factory class to generate the ServerSocket. Could be used to create SSLServerSockets. null =
     *                            default
     */
    ServerSap(int port, int backlog, InetAddress bindAddr, ServerModel serverModel, String name, ServerSocketFactory serverSocketFactory) {
        this.port = port;
        this.backlog = backlog;
        this.bindAddr = bindAddr;
        this.serverSocketFactory = serverSocketFactory;
        this.name = name;
        this.serverModel = serverModel;
    }

    /**
     * Sets local port to listen on for new connections.
     *
     * @param port local port to listen on for new connections
     */
    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    /**
     * Sets the maximum queue length for incoming connection indications (a request to connect) is set to the backlog
     * parameter. If a connection indication arrives when the queue is full, the connection is refused. Set to 0 or less
     * for the default value.
     *
     * @param backlog the maximum queue length for incoming connections.
     */
    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public int getBacklog() {
        return backlog;
    }

    /**
     * Sets the local IP address to bind to, pass null to bind to all
     *
     * @param bindAddr the local IP address to bind to
     */
    public void setBindAddress(InetAddress bindAddr) {
        this.bindAddr = bindAddr;
    }

    public InetAddress getBindAddress() {
        return bindAddr;
    }

    /**
     * Returns the name of the ServerSap / AccessPoint as specified in the SCL file.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the factory class to generate the ServerSocket. The ServerSocketFactory could be used to create
     * SSLServerSockets. Set to <code>null</code> to use <code>ServerSocketFactory.getDefault()</code>.
     *
     * @param serverSocketFactory the factory class to generate the ServerSocket.
     */
    public void setServerSocketFactory(ServerSocketFactory serverSocketFactory) {
        this.serverSocketFactory = serverSocketFactory;
    }

    /**
     * Sets the maximum MMS PDU size in bytes that the server will support. If the client requires the use of a smaller
     * maximum MMS PDU size, then the smaller size will be accepted by the server. The default size is 65000.
     *
     * @param size cannot be less than 64. The upper limit is 65000 so that segmentation at the lower transport layer is
     *             avoided. The Transport Layer's maximum PDU size is 65531.
     */
    public void setMaxMmsPduSize(int size) {
        if (size >= MINIMUM_MMS_PDU_SIZE && size <= MAXIMUM_MMS_PDU_SIZE) {
            proposedMaxMmsPduSize = size;
        } else {
            throw new IllegalArgumentException("maximum size is out of bound");
        }
    }

    /**
     * Gets the maximum MMS PDU size.
     *
     * @return the maximum MMS PDU size.
     */
    public int getMaxMmsPduSize() {
        return proposedMaxMmsPduSize;
    }

    /**
     * Set the maximum number of associations that are allowed in parallel by the server.
     *
     * @param maxAssociations the number of associations allowed (default is 100)
     */
    public void setMaxAssociations(int maxAssociations) {
        acseSap.serverTSap.setMaxConnections(maxAssociations);
    }

    /**
     * Sets the message fragment timeout. This is the timeout that the socket timeout is set to after the first byte of
     * a message has been received. If such a timeout is thrown, the association/socket is closed.
     *
     * @param timeout the message fragment timeout in milliseconds. The default is 60000.
     */
    public void setMessageFragmentTimeout(int timeout) {
        acseSap.serverTSap.setMessageFragmentTimeout(timeout);
    }

    /**
     * Sets the ProposedMaxServOutstandingCalling parameter. The given parameter has no affect on the functionality of
     * this server.
     *
     * @param maxCalling the ProposedMaxServOutstandingCalling parameter. The default is 5.
     */
    public void setProposedMaxServOutstandingCalling(int maxCalling) {
        proposedMaxServOutstandingCalling = maxCalling;
    }

    /**
     * Gets the ProposedMaxServOutstandingCalling parameter.
     *
     * @return the ProposedMaxServOutstandingCalling parameter.
     */
    public int getProposedMaxServOutstandingCalling() {
        return proposedMaxServOutstandingCalling;
    }

    /**
     * Sets the ProposedMaxServOutstandingCalled parameter.The given parameter has no affect on the functionality of
     * this server.
     *
     * @param maxCalled the ProposedMaxServOutstandingCalled parameter. The default is 5.
     */
    public void setProposedMaxServOutstandingCalled(int maxCalled) {
        proposedMaxServOutstandingCalled = maxCalled;
    }

    /**
     * Gets the ProposedMaxServOutstandingCalled parameter.
     *
     * @return the ProposedMaxServOutstandingCalled parameter.
     */
    public int getProposedMaxServOutstandingCalled() {
        return proposedMaxServOutstandingCalled;
    }

    /**
     * Sets the ProposedDataStructureNestingLevel parameter. The given parameter has no affect on the functionality of
     * this server.runServer
     *
     * @param nestingLevel the ProposedDataStructureNestingLevel parameter. The default is 10.
     */
    public void setProposedDataStructureNestingLevel(int nestingLevel) {
        proposedDataStructureNestingLevel = nestingLevel;
    }

    /**
     * Gets the ProposedDataStructureNestingLevel parameter.
     *
     * @return the ProposedDataStructureNestingLevel parameter.
     */
    public int getProposedDataStructureNestingLevel() {
        return proposedDataStructureNestingLevel;
    }

    /**
     * Sets the SevicesSupportedCalled parameter. The given parameter has no affect on the functionality of this server.
     *
     * @param services the ServicesSupportedCalled parameter
     */
    public void setServicesSupportedCalled(byte[] services) {
        if (services.length != 11) {
            throw new IllegalArgumentException("The services parameter needs to be of lenth 11");
        }
        servicesSupportedCalled = services;
    }

    /**
     * Gets the ServicesSupportedCalled parameter.
     *
     * @return the ServicesSupportedCalled parameter.
     */
    public byte[] getServicesSupportedCalled() {
        return servicesSupportedCalled;
    }

    /**
     * Creates a server socket waiting on the configured port for incoming association requests.
     *
     * @param serverEventListener the listener that is notified of incoming writes and when the server stopped listening for new
     *                            connections.
     * @throws IOException if an error occurs binding to the port.
     */
    public void startListening(ServerEventListener serverEventListener) throws IOException {
        timer = new Timer();
        if (serverSocketFactory == null) {
            serverSocketFactory = ServerSocketFactory.getDefault();
        }
        acseSap = new ServerAcseSap(port, backlog, bindAddr, new AcseListener(this), serverSocketFactory);
        this.serverEventListener = serverEventListener;
        listening = true;
        acseSap.startListening();
    }

    /**
     * Stops listening for new connections and closes all existing connections/associations.
     */
    public void stop() {
        acseSap.stopListening();
        synchronized (associations) {
            listening = false;
            for (ServerAssociation association : associations) {
                association.close();
            }
            associations.clear();
        }
    }

    void connectionIndication(AcseAssociation acseAssociation, ByteBuffer psdu) {

        ServerAssociation association;
        synchronized (associations) {
            if (listening) {
                association = new ServerAssociation(this);
                associations.add(association);
            } else {
                acseAssociation.close();
                return;
            }
        }

        try {
            association.handleNewAssociation(acseAssociation, psdu);
        } catch (Exception e) {
            logger.warn("Association closed because of an unexpected exception.", e);
        }

        association.close();
        synchronized (associations) {
            associations.remove(association);
        }
    }

    void serverStoppedListeningIndication(IOException e) {
        if (serverEventListener != null) {
            serverEventListener.serverStoppedListening(this);
        }
    }

    public ServerModel getModelCopy() {
        return serverModel.copy();
    }

    public void setValues(List<BasicDataAttribute> bdas) {
        synchronized (serverModel) {
            for (BasicDataAttribute bda : bdas) {
                // if (bda.getFunctionalConstraint() != FunctionalConstraint.ST) {
                // logger.debug("fc:" + bda.getFunctionalConstraint());
                // throw new IllegalArgumentException(
                // "One can only set values of BDAs with Functional Constraint ST(status)");
                // }

                BasicDataAttribute bdaMirror = bda.mirror;

                if (bdaMirror.dchg && bdaMirror.chgRcbs.size() != 0 && !bda.equals(bdaMirror)) {
                    bdaMirror.setValueFrom(bda);
                    synchronized (bdaMirror.chgRcbs) {
                        for (Urcb urcb : bdaMirror.chgRcbs) {
                            if (bdaMirror.dupd && urcb.getTrgOps().isDataUpdate()) {
                                urcb.report(bdaMirror, true, false, true);
                            } else {
                                urcb.report(bdaMirror, true, false, false);
                            }
                        }
                    }
                } else if (bdaMirror.dupd && bdaMirror.dupdRcbs.size() != 0) {
                    bdaMirror.setValueFrom(bda);
                    synchronized (bdaMirror.dupdRcbs) {
                        for (Urcb urcb : bdaMirror.dupdRcbs) {
                            urcb.report(bdaMirror, false, false, true);
                        }
                    }
                } else if (bdaMirror.qchg && bdaMirror.chgRcbs.size() != 0 && !bda.equals(bdaMirror)) {
                    bdaMirror.setValueFrom(bda);
                    synchronized (bdaMirror.chgRcbs) {
                        for (Urcb urcb : bdaMirror.chgRcbs) {
                            urcb.report(bdaMirror, false, true, false);
                        }
                    }
                } else {
                    bdaMirror.setValueFrom(bda);
                }
            }
        }
    }
}
