/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.treeViews.TaskTreeTablePanel;

public class WorkPlanPanel extends TaskTreeTablePanel
{
	public static WorkPlanPanel createWorkPlanPanel(MainWindow mainWindowToUse, Project projectToUse)
	{
		WorkPlanTreeTableModel model = new WorkPlanTreeTableModel(projectToUse);
		WorkPlanTreeTable tree = new WorkPlanTreeTable(projectToUse, model);
		return new WorkPlanPanel(mainWindowToUse, projectToUse, tree, model);
	}
	
	private WorkPlanPanel(MainWindow mainWindowToUse, Project projectToUse, WorkPlanTreeTable treeToUse, WorkPlanTreeTableModel modelToUse)
	{
		super(mainWindowToUse, projectToUse, treeToUse);
		model = modelToUse;
	}
	
	public EAMObject getParentObject(Task task)
	{
		return ((WorkPlanTreeTableModel)model).getParentObject(task);
	}
	
}
