/*
 * Copyright 2013-14 Fraunhofer ISE
 *
 * This file is part of OpenIEC61850 Client GUI.
 * For more information visit http://www.openmuc.org
 *
 * OpenIEC61850 Client GUI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenIEC61850 Client GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenIEC61850 Client GUI.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.openiec61850.clientgui.databind;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.openmuc.openiec61850.BdaOptFlds;
import org.openmuc.openiec61850.BdaType;
import org.openmuc.openiec61850.clientgui.BasicDataBind;

public class OptfldsDataBind extends BasicDataBind<BdaOptFlds> {

	private final JCheckBox bufferOverflow = new JCheckBox("BufferOverflow");
	private final JCheckBox configRevision = new JCheckBox("ConfigRevision");
	private final JCheckBox dataReference = new JCheckBox("DataReference");
	private final JCheckBox dataSetName = new JCheckBox("DataSetName");
	private final JCheckBox entryId = new JCheckBox("EntryId");
	private final JCheckBox reasonForInclusion = new JCheckBox("ReasonForInclusion");
	private final JCheckBox reportTimestamp = new JCheckBox("ReportTimestamp");
	private final JCheckBox segmentation = new JCheckBox("Segmentation");
	private final JCheckBox sequenceNumber = new JCheckBox("SequenceNumber");

	public OptfldsDataBind(BdaOptFlds data) {
		super(data, BdaType.OPTFLDS);
	}

	@Override
	protected JComponent init() {
		bufferOverflow.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel valuePanel = new JPanel();
		valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.PAGE_AXIS));
		valuePanel.add(bufferOverflow);
		valuePanel.add(configRevision);
		valuePanel.add(dataReference);
		valuePanel.add(dataSetName);
		valuePanel.add(entryId);
		valuePanel.add(reasonForInclusion);
		valuePanel.add(reportTimestamp);
		valuePanel.add(segmentation);
		valuePanel.add(sequenceNumber);
		return valuePanel;
	}

	@Override
	protected void resetImpl() {
		bufferOverflow.setSelected(data.isBufferOverflow());
		configRevision.setSelected(data.isConfigRevision());
		dataReference.setSelected(data.isDataReference());
		dataSetName.setSelected(data.isDataSetName());
		entryId.setSelected(data.isEntryId());
		reasonForInclusion.setSelected(data.isReasonForInclusion());
		reportTimestamp.setSelected(data.isReportTimestamp());
		segmentation.setSelected(data.isSegmentation());
		sequenceNumber.setSelected(data.isSequenceNumber());
	}

	@Override
	protected void writeImpl() {
		data.setBufferOverflow(bufferOverflow.isSelected());
		data.setConfigRevision(configRevision.isSelected());
		data.setDataReference(dataReference.isSelected());
		data.setDataSetName(dataSetName.isSelected());
		data.setEntryId(entryId.isSelected());
		data.setReasonForInclusion(reasonForInclusion.isSelected());
		data.setReportTimestamp(reportTimestamp.isSelected());
		data.setSegmentation(segmentation.isSelected());
		data.setSequenceNumber(sequenceNumber.isSelected());
	}
}
