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

import org.openmuc.openiec61850.BdaFloat64;
import org.openmuc.openiec61850.BdaType;

public class Float64DataBind extends TextFieldDataBind<BdaFloat64> {

    private static final DoubleFilter FILTER = new DoubleFilter();

    public Float64DataBind(BdaFloat64 data) {
        super(data, BdaType.FLOAT64, FILTER);
    }

    @Override
    protected void resetImpl() {
        inputField.setText(data.getDouble().toString());
    }

    @Override
    protected void writeImpl() {
        double newDouble = Double.parseDouble(inputField.getText());
        data.setDouble(newDouble);
    }

    private static class DoubleFilter extends AbstractFilter {
        @Override
        protected boolean test(String text) {
            try {
                Double.parseDouble(text);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
}
