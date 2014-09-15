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

import java.io.IOException;
import java.net.InetAddress;

import javax.net.SocketFactory;

import org.openmuc.josistack.ClientAcseSap;

/**
 * The <code>ClientSap</code> class represents the IEC 61850 service access point for client applications. A client
 * application that wants to connect to a server should first create an instance of <code>ClientSap</code>. Next all the
 * necessary configuration parameters can be set. Finally the <code>associate</code> function is called to connect to
 * the server. An instance of <code>ClientSap</code> can be used to create an unlimited number of associations. Changing
 * the parameters of a ClientSap has no affect on associations that have already been created.
 */
public final class ClientSap {

	static final int MINIMUM_MMS_PDU_SIZE = 64;
	private static final int MAXIMUM_MMS_PDU_SIZE = 65000;

	private static final byte[] DEFAULT_TSEL_LOCAL = new byte[] { 0, 0 };
	private static final byte[] DEFAULT_TSEL_REMOTE = new byte[] { 0, 1 };

	private int proposedMaxMmsPduSize = 65000;
	private final int proposedMaxServOutstandingCalling = 5;
	private final int proposedMaxServOutstandingCalled = 5;
	private final int proposedDataStructureNestingLevel = 10;
	private byte[] servicesSupportedCalling = new byte[] { (byte) 0xee, 0x1c, 0, 0, 0x04, 0x08, 0, 0, 0x79,
			(byte) 0xef, 0x18 };

	private int messageFragmentTimeout = 10000;
	private int responseTimeout = 20000;

	private final ClientAcseSap acseSap;

	/**
	 * Use this constructor to create a default client SAP.
	 */
	public ClientSap() {
		acseSap = new ClientAcseSap();
		acseSap.tSap.tSelLocal = DEFAULT_TSEL_LOCAL;
		acseSap.tSap.tSelRemote = DEFAULT_TSEL_REMOTE;
	}

	/**
	 * Use this constructor to create a client SAP that uses the given <code>SocketFactory</code> to connect to servers.
	 * You could pass an SSLSocketFactory to enable SSL.
	 */
	public ClientSap(SocketFactory socketFactory) {
		acseSap = new ClientAcseSap(socketFactory);
		acseSap.tSap.tSelLocal = DEFAULT_TSEL_LOCAL;
		acseSap.tSap.tSelRemote = DEFAULT_TSEL_REMOTE;
	}

	/**
	 * Sets the maximum MMS PDU size in bytes that the client association will support. The client proposes this value
	 * to the server during association. If the server requires the use of a smaller maximum MMS PDU size, then the
	 * smaller size will be accepted by the client. The default size is 65000.
	 * 
	 * @param size
	 *            cannot be less than 64. The upper limit is 65000 so that segmentation at the lower transport layer is
	 *            avoided. The Transport Layer's maximum PDU size is 65531.
	 */
	public void setMaxMmsPduSize(int size) {
		if (size >= MINIMUM_MMS_PDU_SIZE && size <= MAXIMUM_MMS_PDU_SIZE) {
			proposedMaxMmsPduSize = size;
		}
		else {
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
	 * Sets the SevicesSupportedCalling parameter. The given parameter is sent to the server but has no affect on the
	 * functionality of this client.
	 * 
	 * @param services
	 *            the ServicesSupportedCalling parameter
	 */
	public void setServicesSupportedCalling(byte[] services) {
		if (services.length != 11) {
			throw new IllegalArgumentException("The services parameter needs to be of lenth 11");
		}
		servicesSupportedCalling = services;
	}

	/**
	 * Gets the ServicesSupportedCalling parameter.
	 * 
	 * @return the ServicesSupportedCalling parameter.
	 */
	public byte[] getServicesSupportedCalling() {
		return servicesSupportedCalling;
	}

	/**
	 * Sets the remote/called Transport-Selector (T-SEL). It is optionally transmitted in the OSI Transport Layer
	 * connection request (CR). The default remote T-SEL is byte[] { 0, 1 }.
	 * 
	 * @param tSelRemote
	 *            the remote/called T-SEL. If null the T-SEL will be omitted. No maximum size is defined by the
	 *            standard.
	 */
	public void setTSelRemote(byte[] tSelRemote) {
		acseSap.tSap.tSelRemote = tSelRemote;
	}

	/**
	 * Sets the local/calling Transport-Selector (T-SEL). It is optionally transmitted in the OSI Transport Layer
	 * connection request (CR). The default local T-SEL byte[] { 0, 0 }.
	 * 
	 * @param tSelLocal
	 *            the local/calling T-SEL. If null the T-SEL will be omitted. No maximum size is defined by the
	 *            standard.
	 */
	public void setTSelLocal(byte[] tSelLocal) {
		acseSap.tSap.tSelLocal = tSelLocal;
	}

	/**
	 * Sets the default response timeout of the <code>ClientAssociation</code> that is created using this ClientSap.
	 * 
	 * @param timeout
	 *            the response timeout in milliseconds. The default is 20000.
	 */
	public void setResponseTimeout(int timeout) {
		responseTimeout = timeout;
	}

	/**
	 * Sets the message fragment timeout. This is the timeout that the socket timeout is set to after the first byte of
	 * a message has been received. A request function (e.g. setDataValues()) will throw an IOException if the socket
	 * throws this timeout because the association/connection cannot recover from this kind of error.
	 * 
	 * @param timeout
	 *            the timeout in milliseconds. The default is 10000.
	 */
	public void setMessageFragmentTimeout(int timeout) {
		messageFragmentTimeout = timeout;
	}

	/**
	 * Connects to the IEC 61850 MMS server at the given address and port and returns the resulting association object.
	 * 
	 * @param address
	 *            the address to connect to.
	 * @param port
	 *            the port to connect to. Usually the MMS port is 102.
	 * @param authenticationParameter
	 *            an optional authentication parameters that is transmitted. It will be omitted if equal to null.
	 * @return the association object.
	 * @throws IOException
	 *             if any kind of error occurs trying build up the association.
	 */
	public ClientAssociation associate(InetAddress address, int port, String authenticationParameter,
			ClientEventListener reportListener) throws IOException {

		return associate(address, port, authenticationParameter, null, -1, reportListener);
	}

	/**
	 * Connects to the IEC 61850 MMS server at the given address and port and returns the resulting association object.
	 * 
	 * @param address
	 *            the address to connect to.
	 * @param port
	 *            the port to connect to. Usually the MMS port is 102.
	 * @param authenticationParameter
	 *            an optional authentication parameters that is transmitted. It will be omitted if equal to null.
	 * @return the association object.
	 * @throws IOException
	 *             if any kind of error occurs trying build up the association.
	 */
	public ClientAssociation associate(InetAddress address, int port, String authenticationParameter,
			InetAddress localAddr, int localPort, ClientEventListener reportListener) throws IOException {

		if (port < 0 || port > 65535) {
			throw new IllegalArgumentException("invalid port");
		}

		if (address == null) {
			throw new IllegalArgumentException("address may not be null");
		}

		ClientAssociation clientAssociation = new ClientAssociation(address, port, localAddr, localPort,
				authenticationParameter, acseSap, proposedMaxMmsPduSize, proposedMaxServOutstandingCalling,
				proposedMaxServOutstandingCalled, proposedDataStructureNestingLevel, servicesSupportedCalling,
				responseTimeout, messageFragmentTimeout, reportListener);

		return clientAssociation;
	}

}
