/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
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
		return EAM.fieldLabel(ObjectType.FAKE, getColumnTag(column));
	}
	
	public String getColumnTag(int column)
	{
		return columnTags[column];
	}
	
	public Object getValueAt(Object rawNode, int col)
	{
		PlanningTreeNode treeNode = (PlanningTreeNode) rawNode;
		String columnTag = getColumnTag(col);
		BaseObject baseObject = treeNode.getObject();	
		if (! baseObject.doesFieldExist(columnTag))
			return "";
		
		return baseObject.getData(columnTag);
	}
	
	Project project;
	
	public static final String[] columnTags = {"Item", 
		"Full Text", 
		Indicator.TAG_MEASUREMENT_SUMMARY,
		Indicator.PSEUDO_TAG_METHODS,  
		Indicator.PSEUDO_TAG_FACTOR, 
		Indicator.TAG_PRIORITY, 
		Indicator.PSEUDO_TAG_STATUS_VALUE, 
		Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML,
		Indicator.TAG_MEASUREMENT_DATE,
		Task.PSEUDO_TAG_TASK_TOTAL, };	
}
