/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.BaseObject;
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
	
	public BaseObject getParentObject(Task task)
	{
		return ((WorkPlanTreeTableModel)model).getParentObject(task);
	}
	
}
