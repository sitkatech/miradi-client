package org.conservationmeasures.eam.views.workplan;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.actions.ActionDeleteActivity;
import org.conservationmeasures.eam.actions.ActionInsertActivity;
import org.conservationmeasures.eam.actions.ActionTreeNodeDown;
import org.conservationmeasures.eam.actions.ActionTreeNodeUp;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.strategicplan.ActivitiesPanel;
import org.conservationmeasures.eam.views.strategicplan.ResourceManagementPanel;
import org.conservationmeasures.eam.views.umbrella.ModifyResource;
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
		addTab(EAM.text("Resources"), new ResourceManagementPanel(this));
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
	//TODO remove when transition is over
	/*public void selectObject(EAMObject objectToSelect)
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
	}*/
	
	//TODO remove when transition is over
	//public ModifyActivity getModifyActivityDoer()
	//{
	//	return modifyActivityDoer;
	//}
	
	public ModifyResource getModifyResourceDoer()
	{
		return modifyResourceDoer;
	}

	private void addWorkPlanDoersToMap()
	{
		//TODO remove when transition is over
		//modifyActivityDoer = new ModifyActivity();
		modifyResourceDoer = new ModifyResource();
		
		addDoerToMap(ActionInsertActivity.class, new InsertActivity());
		//TODO remove when transition is over
		//addDoerToMap(ActionModifyActivity.class, modifyActivityDoer);
		addDoerToMap(ActionDeleteActivity.class, new DeleteActivity(this));
		
		//TODO remove when transition is over
		//addDoerToMap(ActionCreateResource.class, new CreateResource());
		//addDoerToMap(ActionModifyResource.class, getModifyResourceDoer());
		//addDoerToMap(ActionDeleteResource.class, new DeleteResource());
		
		//addDoerToMap(ActionCreateObjective.class, new CreateObjective());
		//addDoerToMap(ActionModifyObjective.class, new ModifyObjective());
		//addDoerToMap(ActionDeleteObjective.class, new DeleteObjective());
		
		//addDoerToMap(ActionCreateGoal.class, new CreateGoal());
		//addDoerToMap(ActionModifyGoal.class, new ModifyGoal());
		//addDoerToMap(ActionDeleteGoal.class, new DeleteGoal());

		addDoerToMap(ActionTreeNodeUp.class, new TreeNodeUp());
		addDoerToMap(ActionTreeNodeDown.class, new TreeNodeDown());
	}

	//TODO remove when transition is over
	//ObjectiveManagementPanel objectivePanel;
	//GoalManagementPanel goalPanel;
	
	//TODO remove when transition is over
	//ModifyActivity modifyActivityDoer;
	ModifyResource modifyResourceDoer;
	
	WorkPlanPanel workPlanPanel;
	MainWindow mainWindow;
}
