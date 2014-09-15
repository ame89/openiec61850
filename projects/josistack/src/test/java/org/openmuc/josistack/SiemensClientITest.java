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

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

public class SiemensClientITest {

	public static boolean success = false;

	private final byte[] iso8073ConPacket = new byte[] { (byte) 0x03, 0x00, 0x00, 0x16, 0x11, (byte) 0xe0, 0x00, 0x00,
			0x00, 0x44, 0x00, (byte) 0xc1, 0x02, 0x00, 0x01, (byte) 0xc2, 0x02, 0x00, 0x01, (byte) 0xc0, 0x01, 0x0d };
	private final byte[] mmsConPacket = new byte[] { 0x03, (byte) 0x00, (byte) 0x00, (byte) 0xcd, (byte) 0x02,
			(byte) 0xf0, (byte) 0x80, (byte) 0x0d, (byte) 0xc4, (byte) 0x05, (byte) 0x06, (byte) 0x13, (byte) 0x01,
			(byte) 0x00, (byte) 0x16, (byte) 0x01, (byte) 0x02, (byte) 0x14, (byte) 0x02, (byte) 0x00, (byte) 0x02,
			(byte) 0x33, (byte) 0x02, (byte) 0x00, (byte) 0x01, (byte) 0x34, (byte) 0x02, (byte) 0x00, (byte) 0x01,
			(byte) 0xc1, (byte) 0xae, (byte) 0x31, (byte) 0x81, (byte) 0xab, (byte) 0xa0, (byte) 0x03, (byte) 0x80,
			(byte) 0x01, (byte) 0x01, (byte) 0xa2, (byte) 0x81, (byte) 0xa3, (byte) 0x81, (byte) 0x04, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x82, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x01, (byte) 0xa4, (byte) 0x23, (byte) 0x30, (byte) 0x0f, (byte) 0x02, (byte) 0x01, (byte) 0x01,
			(byte) 0x06, (byte) 0x04, (byte) 0x52, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x30, (byte) 0x04,
			(byte) 0x06, (byte) 0x02, (byte) 0x51, (byte) 0x01, (byte) 0x30, (byte) 0x10, (byte) 0x02, (byte) 0x01,
			(byte) 0x03, (byte) 0x06, (byte) 0x05, (byte) 0x28, (byte) 0xca, (byte) 0x22, (byte) 0x02, (byte) 0x01,
			(byte) 0x30, (byte) 0x04, (byte) 0x06, (byte) 0x02, (byte) 0x51, (byte) 0x01, (byte) 0x88, (byte) 0x02,
			(byte) 0x06, (byte) 0x00, (byte) 0x61, (byte) 0x6c, (byte) 0x30, (byte) 0x6a, (byte) 0x02, (byte) 0x01,
			(byte) 0x01, (byte) 0xa0, (byte) 0x65, (byte) 0x60, (byte) 0x63, (byte) 0x80, (byte) 0x02, (byte) 0x07,
			(byte) 0x80, (byte) 0xa1, (byte) 0x07, (byte) 0x06, (byte) 0x05, (byte) 0x28, (byte) 0xca, (byte) 0x22,
			(byte) 0x02, (byte) 0x03, (byte) 0xa2, (byte) 0x06, (byte) 0x06, (byte) 0x04, (byte) 0x2b, (byte) 0xce,
			(byte) 0x0f, (byte) 0x17, (byte) 0xa3, (byte) 0x03, (byte) 0x02, (byte) 0x01, (byte) 0x17, (byte) 0xa4,
			(byte) 0x03, (byte) 0x02, (byte) 0x01, (byte) 0x00, (byte) 0xa5, (byte) 0x03, (byte) 0x02, (byte) 0x01,
			(byte) 0x00, (byte) 0xa6, (byte) 0x06, (byte) 0x06, (byte) 0x04, (byte) 0x2b, (byte) 0xce, (byte) 0x0f,
			(byte) 0x69, (byte) 0xa7, (byte) 0x03, (byte) 0x02, (byte) 0x01, (byte) 0x21, (byte) 0xbe, (byte) 0x30,
			(byte) 0x28, (byte) 0x2e, (byte) 0x02, (byte) 0x01, (byte) 0x03, (byte) 0xa0, (byte) 0x29, (byte) 0xa8,
			(byte) 0x27, (byte) 0x80, (byte) 0x02, (byte) 0x75, (byte) 0x30, (byte) 0x81, (byte) 0x02, (byte) 0x03,
			(byte) 0xe8, (byte) 0x82, (byte) 0x02, (byte) 0x03, (byte) 0xe8, (byte) 0x83, (byte) 0x01, (byte) 0x05,
			(byte) 0xa4, (byte) 0x16, (byte) 0x80, (byte) 0x01, (byte) 0x02, (byte) 0x81, (byte) 0x03, (byte) 0x05,
			(byte) 0xfb, (byte) 0x00, (byte) 0x82, (byte) 0x0c, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00 };

	public class SampleServer implements AcseAssociationListener {

		@Override
		public void serverStoppedListeningIndication(IOException e) {
			// System.out.println("Got indication that Server stopped listening.");
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

			SiemensClientITest.success = true;

		}
	}

	@Test
	public void testClientServerCom() throws IOException, InterruptedException {

		int port = 14322;

		ServerAcseSap serverAcseSAP = new ServerAcseSap(port, 0, null, new SampleServer());
		serverAcseSAP.serverTSap.setMessageTimeout(6000);
		serverAcseSAP.startListening();

		Socket socket = new Socket("localhost", port);
		DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		dataOutputStream.write(iso8073ConPacket);
		dataOutputStream.write(mmsConPacket);
		dataOutputStream.flush();

		Thread.sleep(2000);

		Assert.assertTrue(success);

		serverAcseSAP.stopListening();
	}

}
