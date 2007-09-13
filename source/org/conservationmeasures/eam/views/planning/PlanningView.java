/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionCreatePlanningViewConfiguration;
import org.conservationmeasures.eam.actions.ActionDeletePlanningViewConfiguration;
import org.conservationmeasures.eam.actions.ActionRenamePlanningViewConfiguration;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.planning.PlanningTreeManagementPanel;
import org.conservationmeasures.eam.dialogs.planning.PlanningViewControlPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.views.TabbedView;

public class PlanningView extends TabbedView
{
	public PlanningView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		addPlanningViewDoersToMap();
	}

	public void becomeActive() throws Exception
	{
		super.becomeActive();
		planningManagementPanel.updateSplitterLocation();
	}
	
	public void createTabs() throws Exception
	{
		planningManagementPanel = new PlanningTreeManagementPanel(getMainWindow());
		JScrollPane managementPanelScrollPane = new JScrollPane(planningManagementPanel);
		
		controlPanel = new PlanningViewControlPanel(getMainWindow());
		JScrollPane controlPanelScroller = new FastScrollPane(controlPanel);
		controlPanelScroller.validate();

		JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		horizontalSplitPane.setLeftComponent(controlPanelScroller);
		horizontalSplitPane.setRightComponent(managementPanelScrollPane);
		// FIXME: Should remember previous split position...this is a HACK!
		horizontalSplitPane.setDividerLocation(controlPanel.getPreferredSize().width + 30);
		
		addTab(EAM.text("Planning"), horizontalSplitPane);
	}

	public void deleteTabs() throws Exception
	{
		planningManagementPanel.dispose();
		planningManagementPanel = null;
		
		controlPanel.dispose();
	}

	public String cardName()
	{
		return getViewName();
	}

	static public String getViewName()
	{
		return Project.PLANNING_VIEW_NAME;
	}

	public JToolBar createToolBar()
	{
		return new PlanningToolBar(getActions());
	}
	
	private void addPlanningViewDoersToMap()
	{
		addDoerToMap(ActionCreatePlanningViewConfiguration.class, new CreatePlanningViewConfigurationDoer());
		addDoerToMap(ActionDeletePlanningViewConfiguration.class, new DeletePlanningViewConfigurationDoer());
		addDoerToMap(ActionRenamePlanningViewConfiguration.class, new RenamePlanningViewConfigurationDoer());
	}
	
	public static boolean isRowOrColumnChangingCommand(CommandSetObjectData cmd)
	{
		if(cmd.getObjectType() == ViewData.getObjectType())
		{
			if(cmd.getFieldTag().equals(ViewData.TAG_PLANNING_STYLE_CHOICE))
				return true;
			if(cmd.getFieldTag().equals(ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE))
				return true;
			if(cmd.getFieldTag().equals(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF))
				return true;
		}
		
		if(cmd.getObjectType() == PlanningViewConfiguration.getObjectType())
		{
			if(cmd.getFieldTag().equals(PlanningViewConfiguration.TAG_COL_CONFIGURATION))
				return true;
			if(cmd.getFieldTag().equals(PlanningViewConfiguration.TAG_ROW_CONFIGURATION))
				return true;
		}
		
		return false;
	}

	static public CodeList getMasterRowList()
	{
		CodeList masterRowList = new CodeList();
		masterRowList.add(ConceptualModelDiagram.OBJECT_NAME);
		masterRowList.add(ResultsChainDiagram.OBJECT_NAME);
		masterRowList.add(Goal.OBJECT_NAME);
		masterRowList.add(Objective.OBJECT_NAME);
		masterRowList.add(Strategy.OBJECT_NAME);
		masterRowList.add(Task.ACTIVITY_NAME);
		masterRowList.add(Indicator.OBJECT_NAME);
		masterRowList.add(Task.METHOD_NAME);
		masterRowList.add(Task.OBJECT_NAME);

		return masterRowList;
	}
	
	public static boolean isCustomizationStyle(ViewData viewData)
	{
		ORef planningViewConfigurationRef = ORef.createFromString(viewData.getData(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF));
		if (planningViewConfigurationRef.isInvalid())
			return false;
		
		return viewData.getData(ViewData.TAG_PLANNING_STYLE_CHOICE).equals(PlanningView.CUSTOMIZABLE_RADIO_CHOICE);
	}
	
	public static final String STRATEGIC_PLAN_RADIO_CHOICE = "StrategicPlanRadioChoice";
	public static final String MONITORING_PLAN_RADIO_CHOICE = "MonitoringPlanRadioChoice";
	public static final String WORKPLAN_PLAN_RADIO_CHOICE = "WorkPlanPlanRadioChoice";
	public static final String SINGLE_LEVEL_RADIO_CHOICE = "SingleLevelRadioChoice";
	public static final String CUSTOMIZABLE_RADIO_CHOICE = "CustomizableRadioChoice";
	
	public static final String SINGLE_LEVEL_COMBO = "SingleLevelCombo";
	public static final String CUSTOMIZABLE_COMBO = "CostomizableCombo";
	
	PlanningTreeManagementPanel planningManagementPanel;
	PlanningViewControlPanel controlPanel;
}
