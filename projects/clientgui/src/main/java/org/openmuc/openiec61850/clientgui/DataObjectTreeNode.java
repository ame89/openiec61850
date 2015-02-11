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

import org.openmuc.openiec61850.*;
import org.openmuc.openiec61850.clientgui.databind.*;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;

public class DataObjectTreeNode extends DefaultMutableTreeNode implements DataTreeNode {

    private static final long serialVersionUID = -3596243932937737877L;

    private final ModelNode node;
    private final BasicDataBind<?> data;

    public DataObjectTreeNode(String name, ModelNode node) {
        super(name);
        this.node = node;
        if (node != null && node.getChildren() == null) {
            // for (ModelNode child : node.getChildren()) {
            // if (child instanceof BasicDataAttribute) {
            // data.add(createDataBind((BasicDataAttribute) child));
            // }
            // }
            data = createDataBind((BasicDataAttribute) node);
        } else {
            data = null;
        }
    }

    public ModelNode getNode() {
        return node;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openmuc.openiec61850.clientgui.DataTreeNode#getData()
     */
    @Override
    public BasicDataBind<?> getData() {
        return data;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openmuc.openiec61850.clientgui.DataTreeNode#reset()
     */
    @Override
    public void reset(ClientAssociation association) throws ServiceError, IOException {
        if (association != null) {
            association.getDataValues((FcModelNode) node);
        }
        if (data != null) {
            data.reset();
        } else {
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) instanceof DataObjectTreeNode) {
                    DataTreeNode child = (DataTreeNode) getChildAt(i);
                    child.reset(null);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openmuc.openiec61850.clientgui.DataTreeNode#writeValues()
     */
    @Override
    public void writeValues(ClientAssociation association) throws ServiceError, IOException {
        if (data != null) {
            data.write();
        } else {
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) instanceof DataObjectTreeNode) {
                    DataTreeNode child = (DataTreeNode) getChildAt(i);
                    child.writeValues(null);
                }
            }
        }
        if (association != null) {
            association.setDataValues((FcModelNode) node);
        }
    }

    @Override
    public boolean writable() {
        if (node instanceof FcModelNode) {
            FcModelNode modelNode = (FcModelNode) node;
            Fc constraint = modelNode.getFc();
            return constraint != Fc.ST && constraint != Fc.MX;
        }
        return false;
    }

    @Override
    public boolean readable() {
        return node instanceof FcModelNode;
    }

    private static BasicDataBind<?> createDataBind(BasicDataAttribute bda) {
        switch (bda.getBasicType()) {
            case BOOLEAN:
                return new BooleanDataBind((BdaBoolean) bda);
            case ENTRY_TIME:
                return new EntryTimeDataBind((BdaEntryTime) bda);
            case FLOAT32:
                return new Float32DataBind((BdaFloat32) bda);
            case FLOAT64:
                return new Float64DataBind((BdaFloat64) bda);
            case INT16:
                return new Int16DataBind((BdaInt16) bda);
            case INT16U:
                return new Int16UDataBind((BdaInt16U) bda);
            case INT32:
                return new Int32DataBind((BdaInt32) bda);
            case INT32U:
                return new Int32UDataBind((BdaInt32U) bda);
            case INT64:
                return new Int64DataBind((BdaInt64) bda);
            case INT8:
                return new Int8DataBind((BdaInt8) bda);
            case INT8U:
                return new Int8UDataBind((BdaInt8U) bda);
            case OCTET_STRING:
                return new OctetStringDataBind((BdaOctetString) bda);
            case TIMESTAMP:
                return new TimeStampDataBind((BdaTimestamp) bda);
            case UNICODE_STRING:
                return new UnicodeStringDataBind((BdaUnicodeString) bda);
            case VISIBLE_STRING:
                return new VisibleStringDataBind((BdaVisibleString) bda);
            case CHECK:
                return new CheckDataBind((BdaCheck) bda);
            case DOUBLE_BIT_POS:
                return new DoubleBitPosDataBind((BdaDoubleBitPos) bda);
            case OPTFLDS:
                return new OptfldsDataBind((BdaOptFlds) bda);
            case QUALITY:
                return new QualityDataBind((BdaQuality) bda);
            case REASON_FOR_INCLUSION:
                return new ReasonForInclusionDataBind((BdaReasonForInclusion) bda);
            case TAP_COMMAND:
                return new TapCommandDataBind((BdaTapCommand) bda);
            case TRIGGER_CONDITIONS:
                return new TriggerConditionDataBind((BdaTriggerConditions) bda);
            default:
                throw new IllegalArgumentException("BasicType " + bda.getBasicType() + " unknown");
        }
    }
}
