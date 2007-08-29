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

import org.conservationmeasures.eam.dialogs.planning.PlanningTreeManagementPanel;
import org.conservationmeasures.eam.dialogs.planning.PlanningViewColumsLegendPanel;
import org.conservationmeasures.eam.dialogs.planning.PlanningViewRowsLegendPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
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
		
		JScrollPane legendScrollPane = createScrollableLengedPanel();		
		JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		horizontalSplitPane.setRightComponent(managementPanelScrollPane);
		horizontalSplitPane.setLeftComponent(legendScrollPane);
		horizontalSplitPane.setDividerLocation(100);
		
		addTab(EAM.text("Planning"), horizontalSplitPane);
	}

	private JScrollPane createScrollableLengedPanel()
	{
		PlanningViewRowsLegendPanel rowsLegendPanel = new PlanningViewRowsLegendPanel(getMainWindow());
		PlanningViewColumsLegendPanel columnsLegendPanel = new PlanningViewColumsLegendPanel(getMainWindow());
		JPanel legendPanel = new JPanel(new BasicGridLayout(2, 1));
		
		legendPanel.add(rowsLegendPanel.createTitleBar(EAM.text("Control Bar")));
		legendPanel.add(rowsLegendPanel);
		legendPanel.add(columnsLegendPanel);
			
		return new JScrollPane(legendPanel);
	}

	public void deleteTabs() throws Exception
	{
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
