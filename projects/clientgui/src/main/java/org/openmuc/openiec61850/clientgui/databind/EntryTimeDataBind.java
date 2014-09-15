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

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.openmuc.openiec61850.BdaEntryTime;
import org.openmuc.openiec61850.BdaType;
import org.openmuc.openiec61850.clientgui.BasicDataBind;

public class EntryTimeDataBind extends BasicDataBind<BdaEntryTime> {

	public EntryTimeDataBind(BdaEntryTime data) {
		super(data, BdaType.ENTRY_TIME);
	}

	@Override
	protected JComponent init() {
		byte[] value = data.getValue();
		StringBuilder sb;

		sb = new StringBuilder("EntryTime [");
		for (int i = 0; i < value.length; i++) {
			sb.append(Integer.toHexString(value[i] & 0xff));
			if (i != value.length - 1) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return new JLabel(sb.toString());
	}

	@Override
	protected void resetImpl() {
		// ignore for now
	}

	@Override
	protected void writeImpl() {
		// ignore for now
	}
}
