package org.openmuc.openiec61850.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openmuc.openiec61850.BasicDataAttribute;
import org.openmuc.openiec61850.BdaFloat32;
import org.openmuc.openiec61850.BdaQuality;
import org.openmuc.openiec61850.BdaQuality.Validity;
import org.openmuc.openiec61850.BdaTimestamp;
import org.openmuc.openiec61850.Fc;
import org.openmuc.openiec61850.SclParseException;
import org.openmuc.openiec61850.ServerEventListener;
import org.openmuc.openiec61850.ServerModel;
import org.openmuc.openiec61850.ServerSap;
import org.openmuc.openiec61850.ServiceError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Stefan Feuerhahn
 * 
 */
public class SampleServer implements ServerEventListener {

	private final static Logger logger = LoggerFactory.getLogger(SampleServer.class);

	private static ServerSap serverSap = null;

	public static void main(String[] args) throws IOException {

		if (args.length < 1 || args.length > 2) {
			System.out.println("usage: org.openmuc.openiec61850.sample.Server <scl-file> [<port>]");
			return;
		}

		int port = 102;

		if (args.length == 2) {
			try {
				port = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.out.println("usage: org.openmuc.openiec61850.sample.Server <scl-file> [<port>]");
				return;
			}
		}

		List<ServerSap> serverSaps = null;
		try {
			serverSaps = ServerSap.getSapsFromSclFile(args[0]);
		} catch (SclParseException e) {
			logger.warn("Error parsing SCL/ICD file: " + e.getMessage());
			return;
		}

		serverSap = serverSaps.get(0);
		serverSap.setPort(port);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if (serverSap != null) {
					serverSap.stop();
				}
				logger.error("Server was stopped.");
			}
		});

		ServerModel serverModel = serverSap.getModelCopy();

		// create a SampleServer instance that can be passed as a callback object to startListening() and
		// setDefaultWriteListener()
		SampleServer sampleServer = new SampleServer();

		serverSap.startListening(sampleServer);

		BdaFloat32 totWMag = (BdaFloat32) serverModel.findModelNode("ied1lDevice1/MMXU1.TotW.mag.f", Fc.MX);
		BdaQuality q = (BdaQuality) serverModel.findModelNode("ied1lDevice1/MMXU1.TotW.q", Fc.MX);
		BdaTimestamp t = (BdaTimestamp) serverModel.findModelNode("ied1lDevice1/MMXU1.TotW.t", Fc.MX);

		List<BasicDataAttribute> totWBdas = new ArrayList<BasicDataAttribute>(3);
		totWBdas.add(totWMag);
		totWBdas.add(q);
		totWBdas.add(t);

		float totWMagVal = 0.0f;
		q.setValidity(BdaQuality.Validity.GOOD);

		while (true) {
			// if (stopped == true) {
			// break;
			// }

			totWMagVal += 1.0;

			logger.info("setting totWmag to: " + totWMagVal);
			totWMag.setFloat(totWMagVal);
			t.setCurrentTime();
			if (q.getValidity() == Validity.GOOD) {
				q.setValidity(BdaQuality.Validity.INVALID);
			}
			else {
				q.setValidity(BdaQuality.Validity.GOOD);
			}

			serverSap.setValues(totWBdas);

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
		}

	}

	@Override
	public void serverStoppedListening(ServerSap serverSap) {
		logger.error("The SAP stopped listening");
	}

	@Override
	public void write(List<BasicDataAttribute> bdas) throws ServiceError {
		for (BasicDataAttribute bda : bdas) {
			logger.info("got a write request: " + bda);
		}

	}

}
