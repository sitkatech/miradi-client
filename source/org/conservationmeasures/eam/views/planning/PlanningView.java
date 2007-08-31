/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.dialogs.planning.PlanningCustomizationPanel;
import org.conservationmeasures.eam.dialogs.planning.PlanningTreeManagementPanel;
import org.conservationmeasures.eam.dialogs.planning.PlanningViewColumsLegendPanel;
import org.conservationmeasures.eam.dialogs.planning.PlanningViewRowsLegendPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.TabbedView;

import com.jhlabs.awt.BasicGridLayout;

public class PlanningView extends TabbedView
{
	public PlanningView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
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
		
		JScrollPane legendScrollPane = createScrollableLegendPanel();		
		JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		horizontalSplitPane.setRightComponent(managementPanelScrollPane);
		horizontalSplitPane.setLeftComponent(legendScrollPane);
		horizontalSplitPane.setDividerLocation(100);
		
		addTab(EAM.text("Planning"), horizontalSplitPane);
	}

	private JScrollPane createScrollableLegendPanel()
	{
		planningCustomizationPanel = new PlanningCustomizationPanel(getProject());
		rowsLegendPanel = new PlanningViewRowsLegendPanel(getMainWindow());
		columnsLegendPanel = new PlanningViewColumsLegendPanel(getMainWindow());
		JPanel legendPanel = new JPanel(new BasicGridLayout(2, 1));
		
		legendPanel.add(rowsLegendPanel.createTitleBar(EAM.text("Control Bar")));
		legendPanel.add(planningCustomizationPanel);
		legendPanel.add(rowsLegendPanel);
		legendPanel.add(columnsLegendPanel);
			
		return new JScrollPane(legendPanel);
	}

	public void deleteTabs() throws Exception
	{
		planningManagementPanel.dispose();
		planningManagementPanel = null;
		
		getProject().removeCommandExecutedListener(rowsLegendPanel);
		getProject().removeCommandExecutedListener(columnsLegendPanel);
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
	
	static public CodeList getMasterRowList()
	{
		CodeList masterRowList = new CodeList();
		masterRowList.add(Goal.OBJECT_NAME);
		masterRowList.add(Objective.OBJECT_NAME);
		masterRowList.add(Strategy.OBJECT_NAME);
		masterRowList.add(Task.ACTIVITY_NAME);
		masterRowList.add(Indicator.OBJECT_NAME);
		masterRowList.add(Task.METHOD_NAME);
		masterRowList.add(Task.OBJECT_NAME);

		return masterRowList;
	}
	
	static public CodeList getMasterColumnList()
	{
		CodeList masterColumnList = new CodeList();
		masterColumnList.add(Indicator.TAG_MEASUREMENT_SUMMARY);
		masterColumnList.add(Indicator.PSEUDO_TAG_METHODS); 
		masterColumnList.add(Indicator.PSEUDO_TAG_FACTOR); 
		masterColumnList.add(Indicator.TAG_PRIORITY);
		masterColumnList.add(Indicator.PSEUDO_TAG_STATUS_VALUE);
		masterColumnList.add(Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML);
		masterColumnList.add(Task.PSEUDO_TAG_COMBINED_EFFORT_DATES);
		masterColumnList.add(Task.PSEUDO_TAG_TASK_TOTAL);

		return masterColumnList;
	}
	
	PlanningTreeManagementPanel planningManagementPanel;
	PlanningViewRowsLegendPanel rowsLegendPanel;
	PlanningCustomizationPanel planningCustomizationPanel;
	PlanningViewColumsLegendPanel columnsLegendPanel;
}
