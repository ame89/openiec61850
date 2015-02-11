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

import org.openmuc.openiec61850.BasicDataAttribute;
import org.openmuc.openiec61850.FcModelNode;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class DataObjectTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = 1682378972258556129L;

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean
            hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        if (value instanceof DataObjectTreeNode) {
            DataObjectTreeNode treeNode = (DataObjectTreeNode) value;
            if (!leaf && treeNode.getNode() instanceof FcModelNode) {
                setIcon(getLeafIcon());
            }

            if (treeNode.getNode() instanceof BasicDataAttribute) {
                BasicDataAttribute attribute = (BasicDataAttribute) treeNode.getNode();
                String tooltip = attribute.getSAddr();
                setToolTipText(tooltip);
            }
        }

        return this;
    }
}
