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

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.openmuc.openiec61850.BdaTapCommand;
import org.openmuc.openiec61850.BdaType;
import org.openmuc.openiec61850.clientgui.BasicDataBind;

public class TapCommandDataBind extends BasicDataBind<BdaTapCommand> {

	private final JComboBox tapCommand = new JComboBox(BdaTapCommand.TapCommand.values());

	public TapCommandDataBind(BdaTapCommand data) {
		super(data, BdaType.TAP_COMMAND);
	}

	@Override
	protected JComponent init() {
		return tapCommand;
	}

	@Override
	protected void resetImpl() {
		tapCommand.setSelectedItem(data.getTapCommand());
	}

	@Override
	protected void writeImpl() {
		// TODO uncomment once data.setTapCommand is implemented
		// data.setTapCommand(tapCommand.getSelectedItem());
	}
}
