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
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.openmuc.openiec61850.BdaQuality;
import org.openmuc.openiec61850.BdaType;
import org.openmuc.openiec61850.clientgui.BasicDataBind;

public class QualityDataBind extends BasicDataBind<BdaQuality> {

	@SuppressWarnings("unchecked")
	private final JComboBox validity = new JComboBox(BdaQuality.Validity.values());

	private final JCheckBox badReference = new JCheckBox("BadReference");
	private final JCheckBox failure = new JCheckBox("Failure");
	private final JCheckBox inaccurate = new JCheckBox("Inaccurate");
	private final JCheckBox inconsistent = new JCheckBox("Inconsistent");
	private final JCheckBox oldData = new JCheckBox("OldData");
	private final JCheckBox operatorBlocked = new JCheckBox("OperatorBlocked");
	private final JCheckBox oscillatory = new JCheckBox("Oscillatory");
	private final JCheckBox outOfRange = new JCheckBox("OutOfRange");
	private final JCheckBox overflow = new JCheckBox("Overflow");
	private final JCheckBox substituded = new JCheckBox("Substituded");
	private final JCheckBox test = new JCheckBox("Test");

	public QualityDataBind(BdaQuality data) {
		super(data, BdaType.QUALITY);
	}

	@Override
	protected JComponent init() {
		validity.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel valuePanel = new JPanel();
		valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.PAGE_AXIS));
		valuePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		valuePanel.add(validity);
		valuePanel.add(badReference);
		valuePanel.add(failure);
		valuePanel.add(inaccurate);
		valuePanel.add(inconsistent);
		valuePanel.add(oldData);
		valuePanel.add(operatorBlocked);
		valuePanel.add(oscillatory);
		valuePanel.add(outOfRange);
		valuePanel.add(overflow);
		valuePanel.add(substituded);
		valuePanel.add(test);
		return valuePanel;
	}

	@Override
	protected void resetImpl() {
		validity.setSelectedItem(data.getValidity());
		badReference.setSelected(data.isBadReference());
		failure.setSelected(data.isFailure());
		inaccurate.setSelected(data.isInaccurate());
		inconsistent.setSelected(data.isInconsistent());
		oldData.setSelected(data.isOldData());
		operatorBlocked.setSelected(data.isOperatorBlocked());
		oscillatory.setSelected(data.isOscillatory());
		outOfRange.setSelected(data.isOutOfRange());
		overflow.setSelected(data.isOverflow());
		substituded.setSelected(data.isSubstituted());
		test.setSelected(data.isTest());
	}

	@Override
	protected void writeImpl() {
		// TODO uncomment once mutators are implemented
		// data.setValidity(validity.getSelectedItem());
		// data.setBadReference(badReference.isSelected());
		// data.setFailure(failure.isSelected());
		// data.setInaccurate(inaccurate.isSelected());
		// data.setInconsistent(inconsistent.isSelected());
		// data.setOldData(oldData.isSelected());
		// data.setOperatorBlocked(operatorBlocked.isSelected());
		// data.setOlscillatory(oscillatory.isSelected());
		// data.setOutOfRange(outOfRange.isSelected());
		// data.setOverflow(overflow.isSelected());
		// data.setSubstituded(substituded.isSelected());
		// data.setTest(test.isSelected());
	}
}
