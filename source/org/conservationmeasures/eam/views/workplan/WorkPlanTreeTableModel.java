/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.treeViews.TaskTreeTableModel;

public class WorkPlanTreeTableModel extends TaskTreeTableModel
{
	public WorkPlanTreeTableModel(Project projectToUse)
	{
		super(new WorkPlanRoot(projectToUse));
		project = projectToUse;
	}

	public int getColumnCount()
	{
		return columnTags.length;
	}

	public String getColumnName(int column)
	{
		return EAM.fieldLabel(ObjectType.TASK, columnTags[column]);
	}
	
	public BaseObject getParentObject(Task task)
	{
		TreePath interventionPath = getPathOfParent(task.getType(), task.getId());
		WorkPlanStrategyNode workPlanStrategy = (WorkPlanStrategyNode)interventionPath.getLastPathComponent();
		return workPlanStrategy.getIntervention();
	}

	public static String[] columnTags = {"Item", };
	Project project;
}
