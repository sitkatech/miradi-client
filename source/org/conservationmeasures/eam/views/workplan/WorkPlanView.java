package org.conservationmeasures.eam.views.workplan;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
import org.conservationmeasures.eam.actions.ActionDeleteWorkPlanNode;
import org.conservationmeasures.eam.actions.ActionInsertActivity;
import org.conservationmeasures.eam.actions.ActionResourceListAdd;
import org.conservationmeasures.eam.actions.ActionResourceListModify;
import org.conservationmeasures.eam.actions.ActionResourceListRemove;
import org.conservationmeasures.eam.actions.ActionTreeNodeDown;
import org.conservationmeasures.eam.actions.ActionTreeNodeUp;
import org.conservationmeasures.eam.actions.ActionViewPossibleResources;
import org.conservationmeasures.eam.dialogs.ActivityPoolManagementPanel;
import org.conservationmeasures.eam.dialogs.ActivityPropertiesPanel;
import org.conservationmeasures.eam.dialogs.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.ResourcePoolManagementPanel;
import org.conservationmeasures.eam.dialogs.WorkPlanManagementPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
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
		workPlanPanel = new WorkPlanPanel(mainWindow, getProject());
		ActivityPropertiesPanel taskPropertiesPanel = new ActivityPropertiesPanel(getProject(), getMainWindow().getActions());
		workPlanManagementPanel = new WorkPlanManagementPanel(workPlanPanel, taskPropertiesPanel); 
		resourceManagementPanel = new ResourcePoolManagementPanel(getProject(), getMainWindow().getActions(), "");
		activitiesManagementPanel = new ActivityPoolManagementPanel(getProject(), getMainWindow().getActions());

		addTab(EAM.text("Work Plan"), workPlanManagementPanel);
		addScrollableTab(activitiesManagementPanel);
		addScrollableTab(resourceManagementPanel);
	}

	public WizardPanel createWizardPanel() throws Exception
	{
		return new WorkPlanWizardPanel();
	}

	public void deleteTabs() throws Exception
	{
		workPlanPanel.dispose();
		workPlanPanel = null;
		resourceManagementPanel.dispose();
		resourceManagementPanel = null;
		activitiesManagementPanel.dispose();
		activitiesManagementPanel = null;
	}
	
	public WorkPlanPanel getWorkPlanPanel()
	{
		return workPlanPanel;
	}
	
	public ActivityPoolManagementPanel getActivitiesManagementPanel()
	{
		return activitiesManagementPanel;
	}
	
	private void addWorkPlanDoersToMap()
	{
		
		addDoerToMap(ActionInsertActivity.class, new InsertActivity());
		addDoerToMap(ActionDeleteWorkPlanNode.class, new DeleteWorkPlanTreeNode());
		
		addDoerToMap(ActionCreateResource.class, new CreateResource());
		addDoerToMap(ActionDeleteResource.class, new DeleteResource());
		
		addDoerToMap(ActionTreeNodeUp.class, new TreeNodeUp());
		addDoerToMap(ActionTreeNodeDown.class, new TreeNodeDown());
		
		addDoerToMap(ActionViewPossibleResources.class, new ViewPossibleResources());
		addDoerToMap(ActionResourceListAdd.class, new ResourceListAdd());
		addDoerToMap(ActionResourceListModify.class, new ResourceListModify());
		addDoerToMap(ActionResourceListRemove.class, new ResourceListRemove());
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

	WorkPlanPanel workPlanPanel;
	ResourcePoolManagementPanel resourceManagementPanel;
	ActivityPoolManagementPanel activitiesManagementPanel;
	WorkPlanManagementPanel workPlanManagementPanel;
		
}
