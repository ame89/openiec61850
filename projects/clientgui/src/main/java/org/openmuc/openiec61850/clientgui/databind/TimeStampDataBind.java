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

import com.toedter.calendar.JDateChooser;
import org.openmuc.openiec61850.BdaTimestamp;
import org.openmuc.openiec61850.BdaType;
import org.openmuc.openiec61850.clientgui.BasicDataBind;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class TimeStampDataBind extends BasicDataBind<BdaTimestamp> {

    private static final Dimension DATECHOOSERDIMENSION = new Dimension(120, 20);

    private JDateChooser dateChooser;
    private JSpinner timeSpinner;

    public TimeStampDataBind(BdaTimestamp data) {
        super(data, BdaType.TIMESTAMP);
    }

    @Override
    protected JComponent init() {
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd-MM-yyyy");
        dateChooser.setPreferredSize(DATECHOOSERDIMENSION);
        timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
        timeSpinner.setEditor(timeEditor);

        Date d = data.getDate();
        if (d == null) {
            d = new Date(0);
        }
        dateChooser.setDate(d);
        timeSpinner.setValue(d);

        JPanel dateTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        dateTimePanel.add(dateChooser);
        dateTimePanel.add(Box.createRigidArea(new Dimension(5, 0)));
        dateTimePanel.add(timeSpinner);
        return dateTimePanel;
    }

    @Override
    protected void resetImpl() {
        Date d = data.getDate();
        if (d == null) {
            d = new Date(0);
        }
        dateChooser.setDate(d);
        timeSpinner.setValue(d);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void writeImpl() {
        Date newDate = dateChooser.getDate();
        Date timeValues = (Date) timeSpinner.getValue();
        newDate.setHours(timeValues.getHours());
        newDate.setMinutes(timeValues.getMinutes());
        newDate.setSeconds(timeValues.getSeconds());
        data.setDate(newDate);
    }
}
