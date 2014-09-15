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
package org.openmuc.openiec61850.clientgui;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.openmuc.openiec61850.BasicDataAttribute;
import org.openmuc.openiec61850.BdaType;

public abstract class BasicDataBind<E extends BasicDataAttribute> {
	protected final E data;

	private JComponent valueField;

	public BasicDataBind(E data, BdaType type) {
		if (data.getBasicType() != type) {
			throw new IllegalArgumentException(data.getName() + " is no " + type);
		}
		this.data = data;
	}

	public JLabel getNameLabel() {
		return new JLabel(data.getName());
	}

	public JComponent getValueField() {
		if (valueField == null) {
			valueField = init();
		}

		return valueField;
	}

	public void reset() {
		if (valueField == null) {
			valueField = init();
		}

		resetImpl();
	}

	public void write() {
		if (valueField == null) {
			valueField = init();
		}

		writeImpl();
	}

	protected abstract JComponent init();

	protected abstract void resetImpl();

	protected abstract void writeImpl();
}
