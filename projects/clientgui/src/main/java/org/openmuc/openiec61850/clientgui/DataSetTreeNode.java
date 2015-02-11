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

import org.openmuc.openiec61850.ClientAssociation;
import org.openmuc.openiec61850.DataSet;
import org.openmuc.openiec61850.ServiceError;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;

public class DataSetTreeNode extends DefaultMutableTreeNode implements DataTreeNode {

    private static final long serialVersionUID = 7919716359809465616L;

    private final DataSet node;

    public DataSetTreeNode(String name, DataSet node) {
        super(name);
        this.node = node;
    }

    public DataSet getNode() {
        return node;
    }

    @Override
    public void reset(ClientAssociation association) throws ServiceError, IOException {
        if (association != null) {
            association.getDataSetValues(node);
        }
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof DataObjectTreeNode) {
                DataTreeNode child = (DataTreeNode) getChildAt(i);
                child.reset(null);
            }
        }
    }

    @Override
    public void writeValues(ClientAssociation association) throws ServiceError, IOException {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof DataObjectTreeNode) {
                DataTreeNode child = (DataTreeNode) getChildAt(i);
                child.writeValues(null);
            }
        }
        if (association != null) {
            association.setDataSetValues(node);
        }
    }

    @Override
    public BasicDataBind<?> getData() {
        return null;
    }

    @Override
    public boolean writable() {
        return true;
    }

    @Override
    public boolean readable() {
        return true;
    }
}
