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

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * This class implements a client Transport Service Access Point (TSAP) over TCP/IP as defined in RFC 1006, ISO 8072,
 * and ISO 8073. It can be used to create TConnections that connect to remote ServerTSAPs.
 */
public final class ClientTSap {

    private int maxTPDUSizeParam = 16;
    private SocketFactory socketFactory = null;
    private int messageTimeout = 0;
    private int messageFragmentTimeout = 60000;

    public byte[] tSelRemote = null;
    public byte[] tSelLocal = null;

    /**
     * Use this constructor to create a client TSAP that will start connections to remote TSAPs.
     */
    public ClientTSap() {
        socketFactory = SocketFactory.getDefault();
    }

    /**
     * Use this constructor to create a client TSAP that will start connections to remote TSAPs. You could pass an
     * SSLSocketFactory to enable SSL.
     *
     * @param socketFactory the socket factory to create the underlying socket
     */
    public ClientTSap(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    /**
     * Set the TConnection timeout for waiting for the first byte of a new message. Default is 0 (unlimited)
     *
     * @param messageTimeout in milliseconds
     */
    public void setMessageTimeout(int messageTimeout) {
        this.messageTimeout = messageTimeout;
    }

    /**
     * Set the TConnection timeout for receiving data once the beginning of a message has been received. Default is
     * 60000 (60seconds)
     *
     * @param messageFragmentTimeout in milliseconds
     */
    public void setMessageFragmentTimeout(int messageFragmentTimeout) {
        this.messageFragmentTimeout = messageFragmentTimeout;
    }

    /**
     * Set the maxTPDUSize. The default maxTPduSize is 65531 (see RFC 1006).
     *
     * @param maxTPduSizeParam The maximum length is equal to 2^(maxTPduSizeParam) octets. Note that the actual TSDU size that can be
     *                         transfered is equal to TPduSize-3. Default is 65531 octets (see RFC 1006), 7 &lt;= maxTPduSizeParam
     *                         &lt;= 16, needs to be set before listening or connecting
     */
    public void setMaxTPDUSizeParam(int maxTPduSizeParam) {
        if (maxTPduSizeParam < 7 || maxTPduSizeParam > 16) {
            throw new IllegalArgumentException("maxTPDUSizeParam is out of bound");
        }
        this.maxTPDUSizeParam = maxTPduSizeParam;
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

    /**
     * Connect to a remote TSAP that is listening at the destination address.
     *
     * @param address remote IP
     * @param port    remote port
     * @return the Connection Object
     * @throws IOException is thrown if connection was unsuccessful.
     */
    public TConnection connectTo(InetAddress address, int port) throws IOException {
        return connectTo(address, port, null, -1);
    }

    /**
     * Connect to a remote TSAP that is listening at the destination address.
     *
     * @param address   remote IP
     * @param port      remote port
     * @param localAddr local IP
     * @param localPort local port
     * @return the Connection Object
     * @throws IOException is thrown if connection was unsuccessful.
     */
    public TConnection connectTo(InetAddress address, int port, InetAddress localAddr, int localPort) throws IOException {
        Socket socket;
        if (localAddr == null) {
            socket = socketFactory.createSocket(address, port);
        } else {
            socket = socketFactory.createSocket(address, port, localAddr, localPort);
        }
        TConnection tConnection = new TConnection(socket, maxTPDUSizeParam, messageTimeout, messageFragmentTimeout, null);
        tConnection.tSelRemote = tSelRemote;
        tConnection.tSelLocal = tSelLocal;
        tConnection.startConnection();

        return tConnection;
    }

    public void setSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

}
