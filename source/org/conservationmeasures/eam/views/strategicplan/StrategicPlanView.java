package org.conservationmeasures.eam.views.strategicplan;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.conservationmeasures.eam.actions.ActionCreateIndicator;
import org.conservationmeasures.eam.actions.ActionCreateObjective;
import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteActivity;
import org.conservationmeasures.eam.actions.ActionDeleteIndicator;
import org.conservationmeasures.eam.actions.ActionDeleteObjective;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
import org.conservationmeasures.eam.actions.ActionInsertActivity;
import org.conservationmeasures.eam.actions.ActionModifyActivity;
import org.conservationmeasures.eam.actions.ActionModifyIndicator;
import org.conservationmeasures.eam.actions.ActionModifyObjective;
import org.conservationmeasures.eam.actions.ActionModifyResource;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.CreateIndicator;
import org.conservationmeasures.eam.views.umbrella.CreateObjective;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class StrategicPlanView extends UmbrellaView
{
	public StrategicPlanView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new StrategicPlanToolBar(mainWindowToUse.getActions()));
		setLayout(new BorderLayout());
		
		tabs = new JTabbedPane();
		add(tabs, BorderLayout.CENTER);
		tabs.addChangeListener(new TabChangeListener());

		addStrategicPlanDoersToMap();

	}
	
	class TabChangeListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent e)
		{
			currentTab = tabs.getSelectedIndex();
		}
		
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Strategic Plan";
	}

	public void becomeActive() throws Exception
	{
		int mostRecentTabIndex = currentTab;
		
		tabs.removeAll();

		strategicPlanPanel = StrategicPlanPanel.createForProject(getMainWindow());
		objectivePanel = new ObjectiveManagementPanel(getMainWindow());
		indicatorManagementPanel = new IndicatorManagementPanel(getMainWindow());
		resourcePanel = new ResourceManagementPanel(getMainWindow());

		tabs.add(EAM.text("Strategic Plan"), strategicPlanPanel);
		tabs.add(EAM.text("Objectives"), objectivePanel);
		tabs.add(EAM.text("Indicators"), indicatorManagementPanel);
		tabs.add(EAM.text("Resources"), resourcePanel);
		
		tabs.setSelectedIndex(mostRecentTabIndex);
	}

	public void becomeInactive() throws Exception
	{
		if(strategicPlanPanel != null)
		{
			strategicPlanPanel.close();
		}
	}
	
	public StrategicPlanPanel getStrategicPlanPanel()
	{
		return strategicPlanPanel;
	}
	
	public ResourceManagementPanel getResourcePanel()
	{
		return resourcePanel;
	}
	
	public IndicatorManagementPanel getIndicatorManagementPanel()
	{
		return indicatorManagementPanel;
	}
	
	public ObjectiveManagementPanel getObjectivePanel()
	{
		return objectivePanel;
	}
	
	public StratPlanObject getSelectedObject()
	{
		if(strategicPlanPanel == null)
			return null;
		return strategicPlanPanel.getSelectedObject();
	}
	
	public ModifyActivity getModifyActivityDoer()
	{
		return modifyActivityDoer;
	}
	
	public ModifyResource getModifyResourceDoer()
	{
		return modifyResourceDoer;
	}

	private void addStrategicPlanDoersToMap()
	{
		modifyActivityDoer = new ModifyActivity(this);
		modifyResourceDoer = new ModifyResource(this);
		
		addDoerToMap(ActionInsertActivity.class, new InsertActivity(this));
		addDoerToMap(ActionModifyActivity.class, modifyActivityDoer);
		addDoerToMap(ActionDeleteActivity.class, new DeleteActivity(this));
		
		addDoerToMap(ActionCreateResource.class, new CreateResource());
		addDoerToMap(ActionModifyResource.class, getModifyResourceDoer());
		addDoerToMap(ActionDeleteResource.class, new DeleteResource());
		
		addDoerToMap(ActionCreateIndicator.class, new CreateIndicator());
		addDoerToMap(ActionModifyIndicator.class, new ModifyObjective());
		addDoerToMap(ActionDeleteIndicator.class, new DeleteIndicator());

		addDoerToMap(ActionCreateObjective.class, new CreateObjective());
		addDoerToMap(ActionModifyObjective.class, new ModifyObjective());
		addDoerToMap(ActionDeleteObjective.class, new DeleteObjective());
	}
	
	JTabbedPane tabs;
	int currentTab;
	StrategicPlanPanel strategicPlanPanel;
	ResourceManagementPanel resourcePanel;
	IndicatorManagementPanel indicatorManagementPanel;
	ObjectiveManagementPanel objectivePanel;
	ModifyActivity modifyActivityDoer;
	ModifyResource modifyResourceDoer;
}

