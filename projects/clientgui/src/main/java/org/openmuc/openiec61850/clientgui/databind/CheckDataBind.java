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

import org.openmuc.openiec61850.BdaCheck;
import org.openmuc.openiec61850.BdaType;
import org.openmuc.openiec61850.clientgui.BasicDataBind;

public class CheckDataBind extends BasicDataBind<BdaCheck> {

	private final JCheckBox interlock = new JCheckBox("Interlock");
	private final JCheckBox synchron = new JCheckBox("Synchron");

	public CheckDataBind(BdaCheck data) {
		super(data, BdaType.CHECK);
	}

	@Override
	protected JComponent init() {
		interlock.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel valuePanel = new JPanel();
		valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.PAGE_AXIS));
		valuePanel.add(interlock);
		valuePanel.add(synchron);
		return valuePanel;
	}

	@Override
	protected void resetImpl() {
		interlock.setSelected(data.getInterlockCheck());
		synchron.setSelected(data.getSynchrocheck());
	}

	@Override
	protected void writeImpl() {
		data.setInterlockCheck(interlock.isSelected());
		data.setSynchrocheck(synchron.isSelected());
	}
}
