/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;

public class PlanningTreeModel extends GenericTreeTableModel
{	
	public PlanningTreeModel(Project projectToUse)
	{
		super(new PlanningTreeNode(projectToUse, projectToUse.getMetadata().getRef()));
		project = projectToUse;
	}

	public int getColumnCount()
	{
		return columnNames.length;
	}

	public String getColumnName(int column)
	{
		return columnNames[column];
	}
	
	public Object getValueAt(Object rawNode, int col)
	{
		PlanningTreeNode treeNode = (PlanningTreeNode) rawNode;
		String columnTag = getTagForColumn(col);
		try
		{
			return treeNode.getObject().getData(columnTag);
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	private String getTagForColumn(int col)
	{
		if (col == 5)
			return Indicator.TAG_PRIORITY;
		
		return "";
	}
	
	Project project;
	
	public static final String[] columnNames = {"Item", "Full Text", "% Complete","Method", "Assoc Factor", "Priority", "Progress", "Who", "When", "Budget", "Budget Total ($)"};	
}
