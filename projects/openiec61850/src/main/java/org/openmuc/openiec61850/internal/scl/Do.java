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
package org.openmuc.openiec61850.internal.scl;

import org.openmuc.openiec61850.SclParseException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public final class Do extends AbstractElement {

    private String type;

    public Do(String name, String desc, String type) {
        super(name, desc);
    }

    public Do(Node xmlNode) throws SclParseException {
        super(xmlNode);

        NamedNodeMap attributes = xmlNode.getAttributes();

        Node node = attributes.getNamedItem("type");

        if (node == null) {
            throw new SclParseException("Required attribute \"type\" not found!");
        }

        type = node.getNodeValue();
    }

    public String getType() {
        return type;
    }

}
