package org.openmuc.openiec61850.sample;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.openmuc.openiec61850.BdaFloat32;
import org.openmuc.openiec61850.BdaQuality;
import org.openmuc.openiec61850.BdaTimestamp;
import org.openmuc.openiec61850.ClientAssociation;
import org.openmuc.openiec61850.ClientEventListener;
import org.openmuc.openiec61850.ClientSap;
import org.openmuc.openiec61850.Fc;
import org.openmuc.openiec61850.FcModelNode;
import org.openmuc.openiec61850.Report;
import org.openmuc.openiec61850.ServerModel;
import org.openmuc.openiec61850.ServiceError;
import org.openmuc.openiec61850.Urcb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Stefan Feuerhahn
 *
 */
public class SampleClient implements ClientEventListener {

	private final static Logger logger = LoggerFactory.getLogger(SampleClient.class);

	public static void main(String[] args) throws ServiceError, IOException {

		String usageString = "usage: org.openmuc.openiec61850.sample.SampleClient <host> <port>";

		if (args.length != 2) {
			System.out.println(usageString);
			return;
		}

		String remoteHost = args[0];
		InetAddress address;
		try {
			address = InetAddress.getByName(remoteHost);
		} catch (UnknownHostException e) {
			logger.error("Unknown host: " + remoteHost);
			return;
		}

		int remotePort;
		try {
			remotePort = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out.println(usageString);
			return;
		}

		ClientSap clientSap = new ClientSap();
		// alternatively you could use ClientSap(SocketFactory factory) to e.g. connect using SSL

		// optionally you can set some association parameters (but usually the default should work):
		// clientSap.setTSelRemote(new byte[] { 0, 1 });
		// clientSap.setTSelLocal(new byte[] { 0, 0 });

		SampleClient eventHandler = new SampleClient();
		ClientAssociation association;

		logger.info("Attempting to connect to server " + remoteHost + " on port " + remotePort);
		try {
			association = clientSap.associate(address, remotePort, null, eventHandler);
		} catch (IOException e) {
			// an IOException will always indicate a fatal exception. It indicates that the association was closed and
			// cannot be recovered. You will need to create a new association using ClientSap.associate() in order to
			// reconnect.
			logger.error("Error connecting to server: " + e.getMessage());
			return;
		}

		ServerModel serverModel;
		try {
			// requestModel() will call all GetDirectory and GetDefinition ACSI services needed to get the complete
			// server model
			serverModel = association.retrieveModel();
		} catch (ServiceError e) {
			logger.error("Service Error requesting model.", e);
			association.close();
			return;
		} catch (IOException e) {
			logger.error("Fatal IOException requesting model.", e);
			return;
		}

		// instead of calling retrieveModel you could read the model directly from an SCL file:
		// try {
		// serverModel = association.getModelFromSclFile("../sampleServer/sampleModel.icd");
		// } catch (SclParseException e1) {
		// logger.error("Error parsing SCL file.", e1);
		// return;
		// }

		// get the values of all data attributes in the model:
		association.getAllDataValues();

		// example for writing a variable:
		FcModelNode modCtlModel = (FcModelNode) serverModel.findModelNode("ied1lDevice1/CSWI1.Mod.ctlModel", Fc.CF);
		association.setDataValues(modCtlModel);

		// example for enabling reporting
		Urcb urcb = serverModel.getUrcb("ied1lDevice1/LLN0.urcb1");
		if (urcb == null) {
			logger.error("ReportControlBlock not found");
		}
		else {
			association.getRcbValues(urcb);
			logger.info("urcb name: " + urcb.getName());
			logger.info("RptId: " + urcb.getRptId());
			logger.info("RptEna: " + urcb.getRptEna().getValue());
			association.enableReporting(urcb);
			association.startGi(urcb);
			association.disableReporting(urcb);
		}

		// example for reading a variable:
		FcModelNode totW = (FcModelNode) serverModel.findModelNode("ied1lDevice1/MMXU1.TotW", Fc.MX);
		BdaFloat32 totWmag = (BdaFloat32) totW.getChild("mag").getChild("f");
		BdaTimestamp totWt = (BdaTimestamp) totW.getChild("t");
		BdaQuality totWq = (BdaQuality) totW.getChild("q");

		while (true) {
			association.getDataValues(totW);
			logger.info("got totW: mag " + totWmag.getFloat() + ", time " + totWt.getDate() + ", quality "
					+ totWq.getValidity());

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}

		}

	}

	@Override
	public void newReport(Report report) {
		logger.info("got report with dataset ref: " + report.getDataSet().getReferenceStr());
		// do something with the report

	}

	@Override
	public void associationClosed(IOException e) {
		logger.info("Association was closed");
	}

}
