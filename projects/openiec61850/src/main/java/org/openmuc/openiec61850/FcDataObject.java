/*
 * Copyright 2011-14 Fraunhofer ISE, energy & meteo Systems GmbH and other contributors
 *
 * This file is part of OpenIEC61850.
 * For more information visit http://www.openmuc.org
 *
 * OpenIEC61850 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * OpenIEC61850 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with OpenIEC61850.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.openiec61850;

import org.openmuc.openiec61850.internal.mms.asn1.Data;
import org.openmuc.openiec61850.internal.mms.asn1.DataSequence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This class represents a functionally constraint DataObject. That means it has unique reference and
 * FunctionalConstraint. A DataObject as defined in part 7-3 is made up of 1..n FcDataObjects where n is the number of
 * different FunctionalConstraints that the children of the DataObject have. An FcDataObject can have children of types
 * FcDataObject, Array, ConstructedDataAttribute or BasicDataAttribute.
 *
 * @author Stefan Feuerhahn
 */
public class FcDataObject extends FcModelNode {

    public FcDataObject(ObjectReference objectReference, Fc fc, List<FcModelNode> children) {

        this.children = new LinkedHashMap<String, ModelNode>((int) ((children.size() / 0.75) + 1));
        this.objectReference = objectReference;
        for (ModelNode child : children) {
            this.children.put(child.getReference().getName(), child);
            child.setParent(this);
        }
        this.fc = fc;
    }

    @Override
    public FcDataObject copy() {
        List<FcModelNode> childCopies = new ArrayList<FcModelNode>(children.size());
        for (ModelNode childNode : children.values()) {
            childCopies.add((FcModelNode) childNode.copy());
        }
        return new FcDataObject(objectReference, fc, childCopies);
    }

    @Override
    Data getMmsDataObj() {
        ArrayList<Data> seq = new ArrayList<Data>(children.size());
        for (ModelNode modelNode : getChildren()) {
            Data child = modelNode.getMmsDataObj();
            if (child == null) {
                throw new IllegalArgumentException("Unable to convert Child: " + modelNode.objectReference + " to MMS Data Object.");
            }
            seq.add(child);
        }
        if (seq.size() == 0) {
            throw new IllegalArgumentException(
                    "Converting ModelNode: " + objectReference + " to MMS Data Object resulted in Sequence of size zero.");
        }

        return new Data(null, new DataSequence(seq), null, null, null, null, null, null, null, null, null, null);

    }

    @Override
    void setValueFromMmsDataObj(Data data) throws ServiceError {
        if (data.structure == null) {
            throw new ServiceError(ServiceError.TYPE_CONFLICT, "expected type: structure");
        }
        if (data.structure.seqOf.size() != children.size()) {
            throw new ServiceError(ServiceError.TYPE_CONFLICT, "expected type: structure with " + children.size() + " elements");
        }

        Iterator<Data> iterator = data.structure.seqOf.iterator();
        for (ModelNode child : children.values()) {
            child.setValueFromMmsDataObj(iterator.next());
        }
    }

}
