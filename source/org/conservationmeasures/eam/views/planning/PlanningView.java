/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.dialogs.planning.PlanningTreeManagementPanel;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class PlanningView extends UmbrellaView
{
	public PlanningView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	public void becomeActive() throws Exception
	{
		super.becomeActive();
		planningManagementPanel = new PlanningTreeManagementPanel(getMainWindow());
		
		PlanningViewLegendPanel legendPanel = new PlanningViewLegendPanel();
		JScrollPane legendScrollPane = new JScrollPane(legendPanel);		
		JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JScrollPane managementPanelScrollPane = new JScrollPane(planningManagementPanel);
		horizontalSplitPane.setRightComponent(managementPanelScrollPane);
		horizontalSplitPane.setLeftComponent(legendScrollPane);
		horizontalSplitPane.setDividerLocation(100);
		
		add(horizontalSplitPane);
		planningManagementPanel.updateSplitterLocation();
	}
	
	public void becomeInactive() throws Exception
	{
		super.becomeInactive();
		
		planningManagementPanel.dispose();
		planningManagementPanel = null;
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
	
	PlanningTreeManagementPanel planningManagementPanel;
}
