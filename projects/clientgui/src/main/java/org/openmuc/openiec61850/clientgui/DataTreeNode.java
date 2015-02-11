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
import org.openmuc.openiec61850.ServiceError;

import javax.swing.tree.TreeNode;
import java.io.IOException;

public interface DataTreeNode {

    public abstract BasicDataBind<?> getData();

    public abstract void reset(ClientAssociation association) throws ServiceError, IOException;

    public abstract void writeValues(ClientAssociation association) throws ServiceError, IOException;

    public abstract int getChildCount();

    public abstract TreeNode getChildAt(int index);

    public abstract boolean writable();

    public abstract boolean readable();
}
