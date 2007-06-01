/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import javax.swing.tree.DefaultMutableTreeNode;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTreeTable;
import org.conservationmeasures.eam.project.Project;

import com.java.sun.jtreetable.AbstractTreeTableModel;
import com.java.sun.jtreetable.TreeTableModel;

public class TaskTree extends PanelTreeTable
{
	public TaskTree(Project project, int interventionId)
	{
		super(new TaskTreeModel(project, interventionId));
	}
}

class TaskTreeModel extends AbstractTreeTableModel
{

	public TaskTreeModel(Project projectToUse, int interventionIdToUse)
	{
		super(new TaskTreeNode("ACTION: Survey islets", ""));
		TaskTreeNode rootNode = (TaskTreeNode)getRoot();
		rootNode.add(new TaskTreeNode("Desk review of maps", "Jose"));
		rootNode.add(new TaskTreeNode("Hire boats", "Elena"));
		rootNode.add(new TaskTreeNode("Conduct surveys", "Mary"));
	}

	public Class getColumnClass(int column)
	{
		if(column == 0)
			return TreeTableModel.class;
		return String.class;
	}

	public int getColumnCount()
	{
		return columnNames.length;
	}

	public String getColumnName(int column)
	{
		return columnNames[column];
	}

	public Object getValueAt(Object node, int column)
	{
		TaskTreeNode taskTreeNode = (TaskTreeNode)node;
		if(column == 0)
			return node.toString();
		return taskTreeNode.getResource();
	}

	public int getChildCount(Object node)
	{
		TaskTreeNode taskTreeNode = (TaskTreeNode)node;
		return taskTreeNode.getChildCount();
	}

	public Object getChild(Object node, int index)
	{
		TaskTreeNode taskTreeNode = (TaskTreeNode)node;
		return taskTreeNode.getChildAt(index);
	}
	
	private static final String[] columnNames = {"Description", "Resource",};
}

class TaskTreeNode extends DefaultMutableTreeNode
{
	public TaskTreeNode(String labelToUse, String resourceToUse)
	{
		super.setUserObject(this);
		label = labelToUse;
		resource = resourceToUse;
	}
	
	public String getResource()
	{
		return resource;
	}
	
	public String toString()
	{
		return label;
	}
	
	String label;
	String resource;
}
