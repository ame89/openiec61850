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

import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ClientServerITest {

    public class SampleServer implements TConnectionListener {

        @Override
        public void connectionIndication(TConnection tConnection) {

            ByteBuffer pduBuffer = ByteBuffer.allocate(600);

            try {
                tConnection.receive(pduBuffer);
            } catch (IOException e1) {
                System.err.println("Caught exception reading data:" + e1.getMessage());
                e1.printStackTrace();
                return;
            } catch (TimeoutException e) {
                System.err.println("Caught TimeoutException reading data:" + e.getMessage());
                e.printStackTrace();
            }

            try {
                tConnection.send(pduBuffer.array(), pduBuffer.position(), pduBuffer.limit() - pduBuffer.position());
            } catch (IOException e) {
                System.err.println("Caught exception writing data:");
                e.printStackTrace();
                return;
            }
        }

        @Override
        public void serverStoppedListeningIndication(IOException e) {
            System.out.println("Got an indication that the server stopped listening.");
        }

    }

    @Test
    public void testClientServerCom() throws IOException, TimeoutException {

        int port = 18982;

        ServerTSap serverTSAP = new ServerTSap(port, new SampleServer());

        serverTSAP.startListening();

        InetAddress address = InetAddress.getByName("127.0.0.1");

        ClientTSap tSAP = new ClientTSap();
        tSAP.setMaxTPDUSizeParam(7);
        TConnection tConnection = null;

        tConnection = tSAP.connectTo(address, port);

        byte[] testData = {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte)
                0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte)
                0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte)
                0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte)
                0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte)
                0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte)
                0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte)
                0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte)
                0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f, (byte)
                0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte)
                0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte)
                0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte)
                0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte)
                0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte)
                0x0d, (byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte)
                0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte)
                0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte)
                0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte)
                0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte)
                0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte)
                0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte)
                0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte)
                0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte)
                0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte)
                0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f, (byte)
                0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte)
                0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte)
                0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte)
                0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte)
                0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte)
                0x0d, (byte) 0x0e, (byte) 0x0f, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte)
                0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f,};

        List<byte[]> testDataList = new ArrayList<byte[]>();
        testDataList.add(testData);
        testDataList.add(testData);

        List<Integer> offsetList = new ArrayList<Integer>();
        offsetList.add(0);
        offsetList.add(0);

        List<Integer> lengthList = new ArrayList<Integer>();
        lengthList.add(testData.length);
        lengthList.add(testData.length);

        tConnection.send(testDataList, offsetList, lengthList);

        tConnection.setMessageTimeout(1000);

        ByteBuffer pduBuffer = ByteBuffer.allocate(600);

        tConnection.receive(pduBuffer);

        byte[] subArrayOfReturnedData = new byte[pduBuffer.limit() - pduBuffer.position()];

        System.arraycopy(pduBuffer.array(), pduBuffer.arrayOffset() + pduBuffer.position(), subArrayOfReturnedData, 0,
                         subArrayOfReturnedData.length);

        Arrays.equals(subArrayOfReturnedData, concat(testData, testData));

        tConnection.disconnect();

        serverTSAP.stopListening();
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

    byte[] concat(byte[] A, byte[] B) {
        byte[] C = new byte[A.length + B.length];
        System.arraycopy(A, 0, C, 0, A.length);
        System.arraycopy(B, 0, C, A.length, B.length);

        return C;
    }

}
