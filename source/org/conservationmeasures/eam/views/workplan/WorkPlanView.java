package org.conservationmeasures.eam.views.workplan;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.actions.ActionAddAssignment;
import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
import org.conservationmeasures.eam.actions.ActionTreeCreateActivity;
import org.conservationmeasures.eam.actions.ActionTreeCreateMethod;
import org.conservationmeasures.eam.actions.ActionTreeCreateTask;
import org.conservationmeasures.eam.actions.ActionRemoveAssignment;
import org.conservationmeasures.eam.actions.ActionTreeNodeDown;
import org.conservationmeasures.eam.actions.ActionTreeNodeUp;
import org.conservationmeasures.eam.dialogs.ActivityPoolManagementPanel;
import org.conservationmeasures.eam.dialogs.MethodPoolManagementPanel;
import org.conservationmeasures.eam.dialogs.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.ResourcePoolManagementPanel;
import org.conservationmeasures.eam.dialogs.TaskPropertiesPanel;
import org.conservationmeasures.eam.dialogs.WorkPlanManagementPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.budget.AddAssignmentDoer;
import org.conservationmeasures.eam.views.budget.RemoveAssignmentDoer;
import org.conservationmeasures.eam.views.treeViews.TaskTreeTablePanel;
import org.conservationmeasures.eam.views.umbrella.CreateResource;
import org.conservationmeasures.eam.views.umbrella.DeleteResource;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.workplan.wizard.WorkPlanWizardPanel;

public class WorkPlanView extends TabbedView
{
	public WorkPlanView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		mainWindow = mainWindowToUse;
		setToolBar(new WorkPlanToolBar(mainWindowToUse.getActions()));
		addWorkPlanDoersToMap();
		add(createScreenShotLabel(), BorderLayout.BEFORE_FIRST_LINE);
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.WORK_PLAN_VIEW_NAME;
	}

	public void createTabs() throws Exception
	{
		WorkPlanPanel workPlanPanel = WorkPlanPanel.createWorkPlanPanel(mainWindow, getProject());
		TaskPropertiesPanel taskPropertiesPanel = new TaskPropertiesPanel(getProject(), getMainWindow().getActions(), workPlanPanel.getTree());
		workPlanManagementPanel = new WorkPlanManagementPanel(workPlanPanel, taskPropertiesPanel); 
		resourceManagementPanel = new ResourcePoolManagementPanel(getProject(), getMainWindow().getActions(), "");
		activitiesManagementPanel = new ActivityPoolManagementPanel(getProject(), getMainWindow().getActions());
		methodPoolManagementPanel = new MethodPoolManagementPanel(getProject(), getMainWindow().getActions());
		
		addTab(EAM.text("Work Plan"), workPlanManagementPanel);
		addScrollableTab(activitiesManagementPanel);
		addScrollableTab(methodPoolManagementPanel);
		addScrollableTab(resourceManagementPanel);
	}

	public WizardPanel createWizardPanel() throws Exception
	{
		return new WorkPlanWizardPanel();
	}

	public void deleteTabs() throws Exception
	{
		workPlanManagementPanel.dispose();
		workPlanManagementPanel = null;
		resourceManagementPanel.dispose();
		resourceManagementPanel = null;
		activitiesManagementPanel.dispose();
		activitiesManagementPanel = null;
		methodPoolManagementPanel.dispose();
		methodPoolManagementPanel = null;
	}
	
	public TaskTreeTablePanel getTaskTreeTablePanel()
	{
		if(workPlanManagementPanel == null)
			return null;
		return workPlanManagementPanel.getWorkPlanPanel();
	}
	
	public ActivityPoolManagementPanel getActivitiesManagementPanel()
	{
		return activitiesManagementPanel;
	}
	
	private void addWorkPlanDoersToMap()
	{
		addDoerToMap(ActionTreeCreateActivity.class, new CreateActivityDoer());
		addDoerToMap(ActionTreeCreateMethod.class, new CreateMethodDoer());
		addDoerToMap(ActionTreeCreateTask.class, new CreateTaskDoer());
//		addDoerToMap(ActionDeleteWorkPlanNode.class, new DeleteWorkPlanTreeNode());
		addDoerToMap(ActionTreeNodeUp.class, new TreeNodeUp());
		addDoerToMap(ActionTreeNodeDown.class, new TreeNodeDown());
		
		addDoerToMap(ActionCreateResource.class, new CreateResource());
		addDoerToMap(ActionDeleteResource.class, new DeleteResource());
		
		addDoerToMap(ActionAddAssignment.class, new AddAssignmentDoer());
		addDoerToMap(ActionRemoveAssignment.class, new RemoveAssignmentDoer());
	}
	
	public void showResourceAddDialog() throws Exception
	{
		PossibleResourcesPanel panel = new PossibleResourcesPanel(getMainWindow());
		ModelessDialogWithClose dlg = new ModelessDialogWithClose(getMainWindow(), panel, panel.getPanelDescription());
		showFloatingPropertiesDialog(dlg);
	}
	
	public EAMObject getSelectedObject()
	{
		if (getActivitiesManagementPanel() != null)
			return getActivitiesManagementPanel().getObject();
		
		return null;
	}
	
	MainWindow mainWindow;

	WorkPlanManagementPanel workPlanManagementPanel;
	ResourcePoolManagementPanel resourceManagementPanel;
	ActivityPoolManagementPanel activitiesManagementPanel;
	MethodPoolManagementPanel methodPoolManagementPanel;
		
}
