/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.HeadlessException;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.conservationmeasures.eam.actions.ActionAbout;
import org.conservationmeasures.eam.actions.ActionClose;
import org.conservationmeasures.eam.actions.ActionConfigureLayers;
import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionExit;
import org.conservationmeasures.eam.actions.ActionInsertCluster;
import org.conservationmeasures.eam.actions.ActionInsertConnection;
import org.conservationmeasures.eam.actions.ActionInsertDirectThreat;
import org.conservationmeasures.eam.actions.ActionInsertDraftIntervention;
import org.conservationmeasures.eam.actions.ActionInsertIndirectFactor;
import org.conservationmeasures.eam.actions.ActionInsertIntervention;
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.actions.ActionNormalDiagramMode;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionPasteWithoutLinks;
import org.conservationmeasures.eam.actions.ActionPreferences;
import org.conservationmeasures.eam.actions.ActionPrint;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionSaveImage;
import org.conservationmeasures.eam.actions.ActionSelectAll;
import org.conservationmeasures.eam.actions.ActionSelectChain;
import org.conservationmeasures.eam.actions.ActionStrategyBrainstormMode;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.ActionZoomIn;
import org.conservationmeasures.eam.actions.ActionZoomOut;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.utils.MenuItemWithoutLocation;

public class MainMenuBar extends JMenuBar
{
	public MainMenuBar(Actions actions) throws HeadlessException
	{
		add(createFileMenu(actions));
		add(createEditMenu(actions));
		add(createInsertMenu(actions));
		add(createViewMenu(actions));
		add(createProcessMenu(actions));
		add(createHelpMenu(actions));
	}

	private JMenu createFileMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|File"));
		menu.add(new JMenuItem(actions.get(ActionNewProject.class)));
		menu.add(new JMenuItem(actions.get(ActionClose.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionPrint.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionSaveImage.class)));
		menu.add(createExportSetupMenu());
		menu.add(createExportMenu());
		menu.add(createImportMenu());
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionExit.class)));
		return menu;
	}

	private JMenu createExportSetupMenu()
	{
		JMenu menu = new JMenu("Configure Export");
		menu.add("Entire Project");
		menu.add("Specific Views");
		menu.add("Restrict Data");
		menu.add("Encryption Level");
		return menu;
	}
	
	private JMenu createExportMenu()
	{
		JMenu menu = new JMenu("Export");
		menu.add("CMP Learning Center");
		menu.add(createExportOrgDatabaseSubmenu());
		menu.add(createExportDonorReportsSubmenu());
		menu.add(createExportOtherSubmenu());
		return menu;
	}
	
	private JMenu createExportOrgDatabaseSubmenu()
	{
		JMenu menu = new JMenu("Organization Database");
		menu.add("WWF Track");
		menu.add("TNC Project Database");
		menu.add("WCS Accounting System");
		return menu;
	}
	
	private JMenu createExportDonorReportsSubmenu()
	{
		JMenu menu = new JMenu("Donor Report");
		menu.add("GEF Format");
		menu.add("Moore Format");
		menu.add("CGBD Common Format");
		return menu;
	}
	
	private JMenu createExportOtherSubmenu()
	{
		JMenu menu = new JMenu("Other Format");
		menu.add("Diagram to MS Visio");
		menu.add("Workplan to MS Project");
		menu.add("Budget to MS Excel");
		menu.add("Map to ARC");
		menu.add("Generic XML");
		return menu;
	}
	
	private JMenu createImportMenu()
	{
		JMenu menu = new JMenu("Import");
		menu.add(createImportProjectSubmenu());
		menu.add(createImportMapSubmenu());
		menu.add(createImportDiagramSubmenu());
		menu.add(createImportWorkplanSubmenu());
		return menu;
	}
	
	private JMenu createImportProjectSubmenu()
	{
		JMenu menu = new JMenu("Conservation Project");
		menu.add("TNC CAP Workbook");
		return menu;
	}
	
	private JMenu createImportMapSubmenu()
	{
		JMenu menu = new JMenu("Maps");
		menu.add("ARC Shape Files");
		return menu;
	}
	
	private JMenu createImportDiagramSubmenu()
	{
		JMenu menu = new JMenu("Diagrams");
		menu.add("MS Visio");
		return menu;
	}
	
	private JMenu createImportWorkplanSubmenu()
	{
		JMenu menu = new JMenu("Workplans");
		menu.add("MS Project");
		return menu;
	}
	
	private JMenu createEditMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Edit"));
		menu.add(new JMenuItem(actions.get(ActionUndo.class)));
		menu.add(new JMenuItem(actions.get(ActionRedo.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionCut.class)));
		menu.add(new JMenuItem(actions.get(ActionCopy.class)));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionPaste.class)));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionPasteWithoutLinks.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionDelete.class)));
		menu.add(new JMenuItem(actions.get(ActionSelectAll.class)));
		menu.add(new JMenuItem(actions.get(ActionSelectChain.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionPreferences.class)));
		return menu;
	}
	
	private JMenu createInsertMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Insert"));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertDraftIntervention.class)));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertIntervention.class)));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertIndirectFactor.class)));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertDirectThreat.class)));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertTarget.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionInsertConnection.class)));
		menu.addSeparator();
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertCluster.class)));
		return menu;
	}
	
	private JMenuItem createJMenuItemCenterLocation(EAMAction action)
	{
		JMenuItem centeredLocationAction = new MenuItemWithoutLocation(action);
		return centeredLocationAction;
	}
	
	private JMenu createViewMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|View"));
		Action[] viewSwitchActions = ViewSwitcher.getViewSwitchActions(actions);
		for(int i = 0; i < viewSwitchActions.length; ++i)
			menu.add(viewSwitchActions[i]);
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionZoomIn.class)));
		menu.add(new JMenuItem(actions.get(ActionZoomOut.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionConfigureLayers.class)));
		menu.add(new JMenuItem(actions.get(ActionStrategyBrainstormMode.class)));
		menu.add(new JMenuItem(actions.get(ActionNormalDiagramMode.class)));
		return menu;
	}
	
	private JMenu createMenu1(Actions actions)
	{
		JMenu menu1 = new JMenu("1. Conceptualize Your Project");
		
		JMenu menu1a = new JMenu("1A. Define initial project team");
		menu1a.add("Select initial project team");
		menu1a.add("Designate a project leader");
		menu1a.add("Develop a charter for the project");
	
		JMenu menu1b = new JMenu("1B. Define clear and common purpose");
		menu1b.add("Define the scope of your project");
		menu1b.add("Establish a clear and common vision");
		menu1b.add("Identify and prioritize targets");
		menu1b.add("Describe status of targets");
		
		JMenu menu1c = new JMenu("1C. Understand project context");
		menu1c.add("Identify direct threats");
		menu1c.add("Rank direct threats");
		menu1c.add("Identify indirect threats & opportunities"); 
		menu1c.add("Assess stakeholders");
		menu1c.add("Analyze project capacity");
		
		JMenu menu1d = new JMenu("1D. Model project situation");
		menu1d.add("Articulate your core assumptions");
		menu1d.add("Create a model");
		menu1d.add("Ground truth and revise model");
		
		menu1.add(menu1a);
		menu1.add(menu1b);
		menu1.add(menu1c);
		menu1.add(menu1d);
		return menu1;
	}
	
	private JMenu createMenu2()
	{
		JMenu menu2 = new JMenu("2. Plan Your Actions");
		
		JMenu menu2a = new JMenu("2A. Develop clear goal and objectives");
		menu2a.add("Develop goals for each target");
		menu2a.add("Identify “key factors” & draft strategies");
		menu2a.add("Develop objectives");
		
		JMenu menu2b = new JMenu("2B. Strategically select activities");
		menu2b.add("Rank draft strategies");
		menu2b.add("Develop results chains for key strategies");
		menu2b.add("Develop activities and action plan");
		menu2b.add("Analyze resources, feasibility, and risk");
		
		menu2.add(menu2a);
		menu2.add(menu2b);
		return menu2;
	}
	
	private JMenu createMenu3()
	{
		JMenu menu3 = new JMenu("3. Plan Your Monitoring");
		
		JMenu menu3a = new JMenu("3A. Focus your monitoring");
		menu3a.add("Determine what you need to know");
		menu3a.add("Clearly define audiences ");
		
		JMenu menu3b = new JMenu("3B. Develop a formal monitoring plan");
		menu3b.add("Define indicators");
		menu3b.add("Select appropriate methods");
		menu3b.add("Plan for data storage");
		
		menu3.add(menu3a);
		menu3.add(menu3b);
		return menu3;
	}
	
	private JMenu createMenu4()
	{
		JMenu menu4 = new JMenu("4. Implement Actions and Monitoring");
		
		JMenu menu4a = new JMenu("4A. Develop detailed short-term work plans");
		menu4a.add("Develop short-term work plans ");
		menu4a.add("Develop Gantt chart and/or calendar of project activities");
		menu4a.add("Clearly define monitoring tasks");
		menu4a.add("Develop project budgets");
		menu4a.add("Agree upon team roles");
		
		menu4.add(menu4a);
		menu4.add("4B. Implement work plans");
		menu4.add("4C. Refine work plans on ongoing basis");
		return menu4;
	}
	
	private JMenu createMenu5()
	{
		JMenu menu5 = new JMenu("5. Analyze");
		
		menu5.add("5A. Analyze your data");
		menu5.add("5B. Analyze interventions");
		menu5.add("5C. Communicate results within project team");
		return menu5;
	}
	
	private JMenu createMenu6()
	{
		JMenu menu6 = new JMenu("6. Use/Adapt");
		menu6.add("6A. Adapt your action and monitoring plans");
		return menu6;
	}
	
	private JMenu createMenu7()
	{
		JMenu menu7 = new JMenu("7. Capture and Share Learning");
		
		menu7.add("7A. Document what you learn");
		menu7.add("7B. Share what you learn");
		menu7.add("7C. Create a learning environment");
		
		return menu7;
	}

	private JMenuItem createMenu8()
	{
		return new JMenuItem("Close the Loop");
	}
	
	private JMenu createProcessMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Step-by-step"));
		menu.add(createMenu1(actions));
		menu.add(createMenu2());
		menu.add(createMenu3());
		menu.add(createMenu4());
		menu.add(createMenu5());
		menu.add(createMenu6());
		menu.add(createMenu7());
		menu.add(createMenu8());
		return menu;
	}

	private JMenu createHelpMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Help"));
		menu.add(new JMenuItem(actions.get(ActionAbout.class)));
		return menu;
	}
}
