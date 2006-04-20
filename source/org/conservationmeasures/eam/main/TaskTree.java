/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.conservationmeasures.eam.project.Project;

public class TaskTree extends JTree
{
	public TaskTree(Project project, int interventionId)
	{
		super(new TaskTreeModel(project, interventionId));
		setRootVisible(true);
	}
}

class TaskTreeModel extends DefaultTreeModel
{
	public TaskTreeModel(Project projectToUse, int interventionIdToUse)
	{
		super(new TaskTreeNode("ACTION: Survey islets"));
		TaskTreeNode rootNode = (TaskTreeNode)getRoot();
		rootNode.add(new TaskTreeNode("Desk review of maps"));
		rootNode.add(new TaskTreeNode("Hire boats"));
		rootNode.add(new TaskTreeNode("Conduct surveys"));
	}
}

class TaskTreeNode extends DefaultMutableTreeNode
{
	public TaskTreeNode(String label)
	{
		super.setUserObject(label);
	}
}
