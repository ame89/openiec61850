/*
 * Copyright 2011-14 Fraunhofer ISE
 *
 * This file is part of jOSIStack.
 * For more information visit http://www.openmuc.org
 *
 * jOSIStack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jOSIStack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jOSIStack.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.josistack;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;

public class ClientServerITest {

	private final byte[] mmsInitRequestPDU = new byte[] { (byte) 0xa8, 0x26, (byte) 0x80, 0x03, 0x00, (byte) 0xfd,
			(byte) 0xe8, (byte) 0x81, 0x01, 0x06, (byte) 0x82, 0x01, 0x06, (byte) 0x83, 0x01, 0x06, (byte) 0xa4, 0x16,
			(byte) 0x80, 0x01, 0x01, (byte) 0x81, 0x03, 0x05, (byte) 0xf1, 0x00, (byte) 0x82, 0x0c, 0x03, (byte) 0xee,
			0x08, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x01, (byte) 0xef, 0x18 };

	private final byte[] testData = { (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05,
			(byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d,
			(byte) 0x0e, (byte) 0x0f, };

	public class SampleServer implements AcseAssociationListener {

		@Override
		public void serverStoppedListeningIndication(IOException e) {
		}

		@Override
		public void connectionIndication(AcseAssociation acseAssociation, ByteBuffer data) {

			try {
				acseAssociation.accept(data);
			} catch (IOException e) {
				System.err.println("Caught accepting association:");
				e.printStackTrace();
				return;
			}

			ByteBuffer receivedData = ByteBuffer.allocate(1000);
			try {
				acseAssociation.receive(receivedData);
			} catch (IOException e) {
				System.err.println("Caught exception receiving data:");
				e.printStackTrace();
				return;
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (DecodingException e) {
				e.printStackTrace();
			}

			try {
				acseAssociation.send(receivedData);
			} catch (IOException e) {
				System.err.println("Caught exception sending data:");
				e.printStackTrace();
				return;
			}

		}
	}

	@Test
	public void testClientServerCom() throws IOException, TimeoutException, DecodingException {

		int port = 14322;

		ServerAcseSap serverAcseSAP = new ServerAcseSap(port, 0, null, new SampleServer());
		serverAcseSAP.serverTSap.setMessageTimeout(6000);

		serverAcseSAP.startListening();

		InetAddress address = InetAddress.getByName("127.0.0.1");

		ClientAcseSap clientAcseSap = new ClientAcseSap();
		clientAcseSap.tSap.setMaxTPDUSizeParam(7);
		clientAcseSap.tSap.setMessageTimeout(6000);
		clientAcseSap.pSelLocal = new byte[] { 0x1, 0x1, 0x1, 0x1 };

		AcseAssociation acseAssociation = clientAcseSap.associate(address, port, null, -1, null,
				ByteBuffer.wrap(mmsInitRequestPDU));

		ByteBuffer associationResponsePDU = acseAssociation.getAssociateResponseAPdu();

		Assert.assertThat(findSubArr(associationResponsePDU.array(), mmsInitRequestPDU), is(not(0)));

		acseAssociation.send(ByteBuffer.wrap(testData));

		ByteBuffer receivedData = ByteBuffer.allocate(1000);

		acseAssociation.receive(receivedData);

		Assert.assertThat(findSubArr(receivedData.array(), testData), is(not(0)));

		acseAssociation.disconnect();

		serverAcseSAP.stopListening();
	}

	public static int findArray(Byte[] array, Byte[] subArray) {
		return Collections.indexOfSubList(Arrays.asList(array), Arrays.asList(subArray));
	}

	public static String getByteArrayString(byte[] byteArray) {
		StringBuilder builder = new StringBuilder();
		int l = 1;
		for (byte b : byteArray) {
			if ((l != 1) && ((l - 1) % 8 == 0)) {
				builder.append(' ');
			}
			if ((l != 1) && ((l - 1) % 16 == 0)) {
				builder.append('\n');
			}
			l++;
			builder.append("0x");
			String hexString = Integer.toHexString(b & 0xff);
			if (hexString.length() == 1) {
				builder.append(0);
			}
			builder.append(hexString + " ");
		}
		return builder.toString();
	}

	int findSubArr(byte[] arr, byte[] subarr) {
		int lim = arr.length - subarr.length;
		byte[] tmpArr = new byte[subarr.length];
		for (int i = 0; i <= lim; i++) {
			System.arraycopy(arr, i, tmpArr, 0, subarr.length);
			if (Arrays.equals(tmpArr, subarr)) {
				return i; // returns starting index of sub array
			}
		}
		return -1;// return -1 on finding no sub-array

	}

}
