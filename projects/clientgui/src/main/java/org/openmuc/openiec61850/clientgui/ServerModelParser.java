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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.tree.TreeNode;

import org.openmuc.openiec61850.DataSet;
import org.openmuc.openiec61850.Fc;
import org.openmuc.openiec61850.FcModelNode;
import org.openmuc.openiec61850.LogicalDevice;
import org.openmuc.openiec61850.LogicalNode;
import org.openmuc.openiec61850.ModelNode;
import org.openmuc.openiec61850.ServerModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerModelParser {

	private final static Logger logger = LoggerFactory.getLogger(ServerModelParser.class);

	private final ServerModel model;
	private DataObjectTreeNode modelTree;

	public ServerModelParser(ServerModel model) {
		this.model = model;
	}

	public TreeNode getModelTree() {
		if (modelTree == null) {
			createModelTree();
		}
		return modelTree;
	}

	private synchronized void createModelTree() {
		if (modelTree == null) {
			modelTree = new DataObjectTreeNode("server", null);
			for (ModelNode node : model.getChildren()) {
				if (node instanceof LogicalDevice == false) {
					logger.error("Node " + node.getName() + " is " + node.getClass() + " (should be LogicalDevice)");
					continue;
				}
				addLogicalDevice(modelTree, (LogicalDevice) node);
			}
			for (DataSet dataSet : model.getDataSets()) {
				addDataSet(modelTree, dataSet);
			}
		}
	}

	private void addLogicalDevice(DataObjectTreeNode root, LogicalDevice node) {
		DataObjectTreeNode treeLD = new DataObjectTreeNode(node.getName(), node);
		root.add(treeLD);
		for (ModelNode subNode : node.getChildren()) {
			if (subNode instanceof LogicalNode == false) {
				logger.error("Node " + subNode.getName() + " is " + subNode.getClass() + " (should be LogicalNode)");
				continue;
			}
			addLogicalNode(treeLD, (LogicalNode) subNode);
		}
	}

	private void addLogicalNode(DataObjectTreeNode parent, LogicalNode node) {
		DataObjectTreeNode treeLN = new DataObjectTreeNode(node.getName(), node);
		parent.add(treeLN);
		Collection<ModelNode> children = node.getChildren();
		Map<String, Set<Fc>> childMap = new HashMap<String, Set<Fc>>();
		for (ModelNode child : children) {
			if (!childMap.containsKey(child.getName())) {
				childMap.put(child.getName(), new HashSet<Fc>());
			}
			childMap.get(child.getName()).add(((FcModelNode) child).getFc());
		}
		for (Entry<String, Set<Fc>> childEntry : childMap.entrySet()) {
			addFunctionalConstraintObject(treeLN, node, childEntry.getKey(), childEntry.getValue());
		}
	}

	private void addDataSet(DataObjectTreeNode parent, DataSet node) {
		DataSetTreeNode treeDS = new DataSetTreeNode(node.getReferenceStr(), node);
		parent.add(treeDS);
		Collection<FcModelNode> children = node.getMembers();
		for (ModelNode child : children) {
			addFunctionalConstraintObject(treeDS, node, child);
		}
	}

	private void addFunctionalConstraintObject(DataObjectTreeNode parent, LogicalNode parentNode, String childName,
			Set<Fc> childFcs) {
		DataObjectTreeNode treeFCDO = new DataObjectTreeNode(childName, null);
		parent.add(treeFCDO);

		for (Fc constraint : childFcs) {
			ModelNode subNode = parentNode.getChild(childName, constraint);
			addDataObject(treeFCDO, "[" + constraint + "]", subNode);
		}
	}

	private void addFunctionalConstraintObject(DataSetTreeNode parent, DataSet parentNode, ModelNode node) {
		DataObjectTreeNode treeFCDO = new DataObjectTreeNode(node.getReference().toString(), node);
		parent.add(treeFCDO);
		if (node.getChildren() != null) {
			for (ModelNode subNode : node.getChildren()) {
				addDataObject(treeFCDO, subNode.getName(), subNode);
			}
		}
	}

	private void addDataObject(DataObjectTreeNode parent, String name, ModelNode node) {
		DataObjectTreeNode treeDO = new DataObjectTreeNode(name, node);
		parent.add(treeDO);
		if (node.getChildren() != null) {
			for (ModelNode subNode : node.getChildren()) {
				addDataObject(treeDO, subNode.getName(), subNode);
			}
		}
	}
}
