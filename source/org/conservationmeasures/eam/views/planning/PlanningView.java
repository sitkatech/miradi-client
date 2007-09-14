/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionAddAssignment;
import org.conservationmeasures.eam.actions.ActionCreatePlanningViewConfiguration;
import org.conservationmeasures.eam.actions.ActionDeletePlanningViewConfiguration;
import org.conservationmeasures.eam.actions.ActionDeletePlanningViewTreeNode;
import org.conservationmeasures.eam.actions.ActionRemoveAssignment;
import org.conservationmeasures.eam.actions.ActionRenamePlanningViewConfiguration;
import org.conservationmeasures.eam.actions.ActionTreeNodeDown;
import org.conservationmeasures.eam.actions.ActionTreeNodeUp;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.planning.PlanningTreeManagementPanel;
import org.conservationmeasures.eam.dialogs.planning.PlanningTreeTable;
import org.conservationmeasures.eam.dialogs.planning.PlanningTreeTablePanel;
import org.conservationmeasures.eam.dialogs.planning.legend.PlanningViewControlPanel;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningTreePropertiesPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.budget.AddAssignmentDoer;
import org.conservationmeasures.eam.views.budget.RemoveAssignmentDoer;
import org.conservationmeasures.eam.views.planning.doers.CreatePlanningViewConfigurationDoer;
import org.conservationmeasures.eam.views.planning.doers.DeletePlanningViewConfigurationDoer;
import org.conservationmeasures.eam.views.planning.doers.RenamePlanningViewConfigurationDoer;
import org.conservationmeasures.eam.views.planning.doers.TreeNodeDeleteDoer;
import org.conservationmeasures.eam.views.planning.doers.TreeNodeDownDoer;
import org.conservationmeasures.eam.views.planning.doers.TreeNodeUpDoer;

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
		PlanningTreeTablePanel planningTreeTablePanel = PlanningTreeTablePanel.createPlanningTreeTablePanel(getMainWindow());
		PlanningTreeTable treeAsObjectPicker = (PlanningTreeTable) planningTreeTablePanel.getTree();
		PlanningTreePropertiesPanel planningTreePropertiesPanel = new PlanningTreePropertiesPanel(getMainWindow(), ORef.INVALID, treeAsObjectPicker);
		planningManagementPanel = new PlanningTreeManagementPanel(getMainWindow(), planningTreeTablePanel, planningTreePropertiesPanel);
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
		addDoerToMap(ActionRemoveAssignment.class, new RemoveAssignmentDoer());
		addDoerToMap(ActionAddAssignment.class, new AddAssignmentDoer());
		addDoerToMap(ActionTreeNodeUp.class, new TreeNodeUpDoer());
		addDoerToMap(ActionTreeNodeDown.class, new TreeNodeDownDoer());
		addDoerToMap(ActionDeletePlanningViewTreeNode.class, new TreeNodeDeleteDoer());
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
