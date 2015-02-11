/*
 * Copyright 2011-14 Fraunhofer ISE
 *
 * This file is part of jOSITransport.
 * For more information visit http://www.openmuc.org
 *
 * jOSITransport is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jOSITransport is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jOSITransport.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.jositransport;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.InetAddress;

/**
 * This class implements the server Transport Service Access Point (TSAP) over TCP/IP as defined in RFC 1006, ISO 8072,
 * and ISO 8073. It can be used to listen for incoming TConnections.
 */
public class ServerTSap {

    private ServerThread serverThread;

    private final int port;
    private final InetAddress bindAddr;
    private final int backlog;
    private final TConnectionListener connectionListener;
    private final ServerSocketFactory serverSocketFactory;

    private boolean started = false;
    private int maxTPDUSizeParam = 16;
    private int maxConnections = 100;
    private int messageTimeout = 0;
    private int messageFragmentTimeout = 60000;

    /**
     * Use this constructor to create a server TSAP that can listen on a port.
     *
     * @param port        the TCP port that the ServerSocket will connect to. Should be between 1 and 65535.
     * @param conListener the ConnectionListener that will be notified when remote TSAPs are connecting or the server stopped
     *                    listening.
     */
    public ServerTSap(int port, TConnectionListener conListener) {
        this(port, 0, null, conListener, ServerSocketFactory.getDefault());
    }

    /**
     * Use this constructor to create a server TSAP that can listen on a port.
     *
     * @param port        the TCP port that the ServerSocket will connect to. Should be between 1 and 65535.
     * @param conListener the ConnectionListener that will be notified when remote TSAPs are connecting or the server stopped
     *                    listening.
     * @param backlog     is passed to the java.net.ServerSocket
     * @param bindAddr    the IP address to bind to. It is passed to java.net.ServerSocket
     */
    public ServerTSap(int port, int backlog, InetAddress bindAddr, TConnectionListener conListener) {
        this(port, backlog, bindAddr, conListener, ServerSocketFactory.getDefault());
    }

    /**
     * Use this constructor to create a server TSAP that can listen on a port, with a specified ServerSocketFactory.
     *
     * @param port                the TCP port that the ServerSocket will connect to. Should be between 1 and 65535.
     * @param connectionListener  the ConnectionListener that will be notified when remote TSAPs are connecting or the server stopped
     *                            listening.
     * @param backlog             is passed to the java.net.ServerSocket
     * @param bindAddr            the IP address to bind to. It is passed to java.net.ServerSocket
     * @param serverSocketFactory The ServerSocketFactory to be used to create the ServerSocket
     */
    public ServerTSap(int port, int backlog, InetAddress bindAddr, TConnectionListener connectionListener, ServerSocketFactory
            serverSocketFactory) {
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("port number is out of bound");
        }

        this.port = port;
        this.backlog = backlog;
        this.bindAddr = bindAddr;
        this.connectionListener = connectionListener;
        this.serverSocketFactory = serverSocketFactory;
    }

    /**
     * Starts a new thread that listens on the configured port. This method is non-blocking.
     *
     * @throws IOException if a starting to listen fails
     */
    public void startListening() throws IOException {
        started = true;
        serverThread = new ServerThread(serverSocketFactory.createServerSocket(port, backlog, bindAddr), maxTPDUSizeParam, maxConnections,
                                        messageTimeout, messageFragmentTimeout, connectionListener);
        serverThread.start();
    }

    /**
     * Stop listing on the port. Stops the server thread.
     */
    public void stopListening() {
        if (serverThread != null) {
            serverThread.stopServer();
        }
        serverThread = null;
        started = false;
    }

    /**
     * Set the maxTPDUSize. The default maxTPduSize is 65531 (see RFC 1006).
     *
     * @param maxTPduSizeParam The maximum length is equal to 2^(maxTPduSizeParam) octets. Note that the actual TSDU size that can be
     *                         transfered is equal to TPduSize-3. Default is 65531 octets (see RFC 1006), 7 &lt;= maxTPduSizeParam
     *                         &lt;= 16, needs to be set before listening or connecting
     */
    public void setMaxTPDUSizeParam(int maxTPduSizeParam) {
        if (started == true) {
            throw new RuntimeException("Trying to set parameter although server has started.");
        }
        if (maxTPduSizeParam < 7 || maxTPduSizeParam > 16) {
            throw new IllegalArgumentException("maxTPDUSizeParam is out of bound");
        }
        this.maxTPDUSizeParam = maxTPduSizeParam;
    }

    /**
     * Set the maximum number of connections that are allowed in parallel by the Server SAP.
     *
     * @param maxConnections the number of connections allowed (default is 100)
     */
    public void setMaxConnections(int maxConnections) {
        if (started == true) {
            throw new RuntimeException("Trying to set parameter although server has started.");
        }
        if (maxConnections < 0) {
            throw new IllegalArgumentException("maxConnections is out of bound");
        }
        this.maxConnections = maxConnections;
    }

    /**
     * Set the TConnection timeout for waiting for the first byte of a new message. Default is 0 (unlimited)
     *
     * @param messageTimeout in milliseconds
     */
    public void setMessageTimeout(int messageTimeout) {
        if (started == true) {
            throw new RuntimeException("Message timeout may not be set while the server SAP ist listening.");
        }
        this.messageTimeout = messageTimeout;
    }

    /**
     * Set the TConnection timeout for receiving data once the beginning of a message has been received. Default is
     * 60000 (60 seconds)
     *
     * @param messageFragmentTimeout in milliseconds
     */
    public void setMessageFragmentTimeout(int messageFragmentTimeout) {
        if (started == true) {
            throw new RuntimeException("Message fragment timeout may not be set while the server SAP ist listening.");
        }
        this.messageFragmentTimeout = messageFragmentTimeout;
    }

    /**
     * Get the maximum TPDU size parameter to be used by this TSAP
     *
     * @return the maximum TPDU size parameter
     */
    public int getMaxTPDUSizeParam() {
        return maxTPDUSizeParam;
    }

    /**
     * Calculates and returns the maximum TPDUSize. This is equal to 2^(maxTPDUSizeParam)
     *
     * @param maxTPDUSizeParam the size parameter
     * @return the maximum TPDU size
     */
    public static int getMaxTPDUSize(int maxTPDUSizeParam) {
        if (maxTPDUSizeParam < 7 || maxTPDUSizeParam > 16) {
            throw new IllegalArgumentException("maxTPDUSizeParam is out of bound");
        }
        if (maxTPDUSizeParam == 16) {
            return 65531;
        } else {
            return (int) Math.pow(2, maxTPDUSizeParam);
        }
    }

    TConnectionListener getConnectionListener() {
        return connectionListener;
    }

}
