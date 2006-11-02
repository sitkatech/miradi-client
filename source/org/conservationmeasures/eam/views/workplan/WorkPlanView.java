package org.conservationmeasures.eam.views.workplan;

import java.awt.BorderLayout;
import java.awt.Component;

import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteActivity;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
import org.conservationmeasures.eam.actions.ActionInsertActivity;
import org.conservationmeasures.eam.actions.ActionModifyActivity;
import org.conservationmeasures.eam.actions.ActionTreeNodeDown;
import org.conservationmeasures.eam.actions.ActionTreeNodeUp;
import org.conservationmeasures.eam.dialogs.NewResourceManagementPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.strategicplan.ModifyActivity;
import org.conservationmeasures.eam.views.umbrella.CreateResource;
import org.conservationmeasures.eam.views.umbrella.DeleteResource;
import org.conservationmeasures.eam.views.umbrella.ObjectManagementPanel;
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
		addTab(EAM.text("Work Plan"), workPlanPanel);
		addTab(EAM.text("Resources"), new NewResourceManagementPanel(getMainWindow()));
		addTab(EAM.text("Activities"), new ActivitiesPanel(this));
	}

	public WizardPanel createWizardPanel() throws Exception
	{
		return new WorkPlanWizardPanel();
	}

	public void deleteTabs() throws Exception
	{
		workPlanPanel = null;
	}
	
	public WorkPlanPanel getWorkPlanPanel()
	{
		return workPlanPanel;
	}

	public void selectObject(EAMObject objectToSelect)
	{
		Component tab = getCurrentTabContents();
		if(tab.equals(getWorkPlanPanel()))
		{
			getWorkPlanPanel().selectObject(objectToSelect);
		}
		else
		{
			ObjectManagementPanel panel = (ObjectManagementPanel)tab;
			panel.selectObject(objectToSelect);
		}
	}
	
	public ModifyActivity getModifyActivityDoer()
	{
		return modifyActivityDoer;
	}
	
	private void addWorkPlanDoersToMap()
	{
		modifyActivityDoer = new ModifyActivity();
		
		addDoerToMap(ActionInsertActivity.class, new InsertActivity());
		addDoerToMap(ActionModifyActivity.class, modifyActivityDoer);
		addDoerToMap(ActionDeleteActivity.class, new DeleteActivity(this));
		
		addDoerToMap(ActionCreateResource.class, new CreateResource());
		addDoerToMap(ActionDeleteResource.class, new DeleteResource());
		
		addDoerToMap(ActionTreeNodeUp.class, new TreeNodeUp());
		addDoerToMap(ActionTreeNodeDown.class, new TreeNodeDown());
	}
	
	ModifyActivity modifyActivityDoer;
	
	WorkPlanPanel workPlanPanel;
	MainWindow mainWindow;
}
