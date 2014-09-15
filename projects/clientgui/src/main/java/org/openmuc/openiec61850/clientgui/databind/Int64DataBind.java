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

import org.openmuc.openiec61850.BdaInt64;
import org.openmuc.openiec61850.BdaType;

public class Int64DataBind extends TextFieldDataBind<BdaInt64> {

	private static final Int64Filter FILTER = new Int64Filter();

	public Int64DataBind(BdaInt64 data) {
		super(data, BdaType.INT64, FILTER);
	}

	@Override
	protected void resetImpl() {
		inputField.setText(new Long(data.getValue()).toString());
	}

	@Override
	protected void writeImpl() {
		data.setValue(Long.parseLong(inputField.getText()));
	}

	private static class Int64Filter extends AbstractFilter {
		@Override
		protected boolean test(String text) {
			try {
				Long.parseLong(text);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
	}
}
