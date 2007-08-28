/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Task;
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
		return columnTags.length;
	}

	public String getColumnName(int column)
	{
		return columnTags[column];
	}
	
	public Object getValueAt(Object rawNode, int col)
	{
		PlanningTreeNode treeNode = (PlanningTreeNode) rawNode;
		String columnTag = getColumnName(col);
		BaseObject baseObject = treeNode.getObject();	
		if (! baseObject.doesFieldExist(columnTag))
			return "";
		
		return baseObject.getData(columnTag);
	}
	
	Project project;
	
	public static final String[] columnTags = {"Item", "Full Text", "% Complete","Method", "Assoc Factor", Indicator.TAG_PRIORITY, "Progress", Task.PSEUDO_TAG_WHO, "When", "Budget", "Budget Total ($)"};	
}
