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

import org.openmuc.openiec61850.BdaBoolean;
import org.openmuc.openiec61850.BdaType;
import org.openmuc.openiec61850.clientgui.BasicDataBind;

import javax.swing.*;

public class BooleanDataBind extends BasicDataBind<BdaBoolean> {

    private JCheckBox checkbox;

    public BooleanDataBind(BdaBoolean data) {
        super(data, BdaType.BOOLEAN);
    }

    @Override
    protected JComponent init() {
        checkbox = new JCheckBox();
        checkbox.setBorder(null);
        checkbox.setSelected(data.getValue());
        return checkbox;
    }

    @Override
    protected void resetImpl() {
        checkbox.setSelected(data.getValue());
    }

    @Override
    protected void writeImpl() {
        data.setValue(checkbox.isSelected());
    }
}
