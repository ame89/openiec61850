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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class extends Thread. It is started by ServerTSAP and listens on a socket for connections and hands them to the
 * ConnectionHandler class. It notifies ConnectionListener if the socket is closed.
 *
 * @author Stefan Feuerhahn
 */
final class ServerThread extends Thread {

    private final static Logger logger = LoggerFactory.getLogger(ServerThread.class);

    private final ServerSocket serverSocket;
    private final int maxTPduSizeParam;
    private final int messageTimeout;
    private final int messageFragmentTimeout;
    private final int maxConnections;
    private final TConnectionListener connectionListener;

    private boolean stopServer = false;
    private volatile int numConnections = 0;

    ServerThread(ServerSocket socket, int maxTPduSizeParam, int maxConnections, int messageTimeout, int messageFragmentTimeout,
                 TConnectionListener connectionListener) {
        serverSocket = socket;
        this.maxTPduSizeParam = maxTPduSizeParam;
        this.maxConnections = maxConnections;
        this.messageTimeout = messageTimeout;
        this.messageFragmentTimeout = messageFragmentTimeout;
        this.connectionListener = connectionListener;
    }

    public final class ConnectionHandler extends Thread {

        private final Socket socket;
        private final ServerThread serverThread;

        ConnectionHandler(Socket socket, ServerThread serverThread) {
            this.socket = socket;
            this.serverThread = serverThread;
        }

        @Override
        public void run() {

            TConnection tConnection;
            try {
                tConnection = new TConnection(socket, maxTPduSizeParam, messageTimeout, messageFragmentTimeout, serverThread);
            } catch (IOException e) {
                logger.warn("Exception occured when someone tried to connect.", e);
                numConnections--;
                return;
            }
            try {
                tConnection.listenForCR();
            } catch (IOException e) {
                logger.warn("Exception occured when someone tried to connect. Server was listening for ISO Transport CR packet.", e);
                tConnection.close();
                return;
            }
            connectionListener.connectionIndication(tConnection);

        }
    }

    @Override
    public void run() {

        ExecutorService executor = Executors.newFixedThreadPool(maxConnections);
        try {

            Socket clientSocket = null;

            while (true) {
                try {
                    clientSocket = serverSocket.accept();
                } catch (IOException e) {
                    if (stopServer == false) {
                        connectionListener.serverStoppedListeningIndication(e);
                    }
                    return;
                }

                if (numConnections < maxConnections) {
                    numConnections++;
                    ConnectionHandler myConnectionHandler = new ConnectionHandler(clientSocket, this);
                    executor.execute(myConnectionHandler);
                } else {
                    logger.warn(
                            "Maximum number of connections reached. Ignoring connection request. Maximum number of connections: " + maxConnections);
                }

            }
        } finally {
            executor.shutdown();
        }
    }

    void connectionClosedSignal() {
        numConnections--;
    }

    /**
     * Stops listening for new connections. Existing connections are not touched.
     */
    void stopServer() {
        stopServer = true;
        if (serverSocket.isBound()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
            }
        }
    }

}
