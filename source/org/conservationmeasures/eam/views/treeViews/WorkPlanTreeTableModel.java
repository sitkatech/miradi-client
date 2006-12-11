/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.treeViews;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.workplan.WorkPlanRoot;

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

	public static String[] columnTags = {"Item", };
	Project project;
}
