/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import org.conservationmeasures.eam.commands.CommandSwitchView;
import org.conservationmeasures.eam.exceptions.AlreadyInThatViewException;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.utils.MenuItemWithoutLocation;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.interview.InterviewView;

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
	
	private JMenu createProcessMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Step-by-step"));
		menu.add(createConceptualizeSubment());
		menu.add(createPlanActionsSubmenu());
		menu.add(createSubmenu("2.2. Plan Your Monitoring",
				new String[] {"A. Information Needs", "B. Monitoring and Evaluation (M&E) Plan"}));
		menu.add(createSubmenu("3. Implement Actions and Monitoring",
				new String[] {"Project Tracking Tools"}));
		menu.add(createSubmenu("4. Analyze",
				new String[] {"A. Analyze M&E Information", "B. Analyze Interventions", "C. Communicate with Project Team"}));
		menu.add(createSubmenu("5. Use/Adapt",
				new String[] {"A. Adapt Action and M&E Plans"}));
		menu.add(createSubmenu("6. Communicate",
				new String[] {"A. Dissemination Strategy"}));
		menu.add(createSubmenu("7. Iterate",
				new String[] {"A. Revisit Steps", "B. Create a Learning Environment"}));
		return menu;
	}

	private JMenu createPlanActionsSubmenu()
	{
		String objectivesText = "A. Goal and Objectives";
		String[] items = new String[] {"B. Activities", "C. Action Plan"};
		JMenu submenu = new JMenu("2.1. Plan Your Actions");
		JMenuItem objectivesItem = new JMenuItem(objectivesText);
		objectivesItem.addActionListener(new ObjectivesActionHandler());
		submenu.add(objectivesItem);
		for(int i = 0; i < items.length; ++i)
			submenu.add(items[i]);
		return submenu;
	}
	
	private JMenu createConceptualizeSubment()
	{
		String[] items = new String[] {"B. Understand the context"};
		JMenu submenu = new JMenu("1. Conceptualize");
		
		JMenuItem clarifyIssueItem = new JMenuItem("A. Be clear about issue");
		clarifyIssueItem.addActionListener(new ClarifyIssueActionHandler());
		submenu.add(clarifyIssueItem);
		
		for(int i = 0; i < items.length; ++i)
			submenu.add(items[i]);
		
		JMenuItem modelItem = new JMenuItem("C. Create a conceptual model");
		modelItem.addActionListener(new ModelActionHandler());
		submenu.add(modelItem);
		
		return submenu;
	}
	
	abstract class JumpHandler implements ActionListener
	{
		void jumpToInterviewStep(String stepName)
		{
			String viewName = InterviewView.getViewName();
			switchToView(viewName);
			
			// TODO: Re-implement with new interview wizard mechanism
//				CommandInterviewSetStep setStep = new CommandInterviewSetStep(stepName);
//				EAM.mainWindow.getProject().executeCommand(setStep);
		}

		void switchToView(String viewName)
		{
			try
			{
				CommandSwitchView switchView = new CommandSwitchView(viewName);
				EAM.mainWindow.getProject().executeCommand(switchView);
			}
			catch (AlreadyInThatViewException ignoreIt)
			{
			}
			catch(CommandFailedException e)
			{
				EAM.logWarning("Unable to switch views");
				EAM.logException(e);
			}
		}
		
	}
	
	class ClarifyIssueActionHandler extends JumpHandler
	{
		public void actionPerformed(ActionEvent event)
		{
			jumpToInterviewStep("P1aT2S1");
		}

	}

	class ObjectivesActionHandler extends JumpHandler
	{
		public void actionPerformed(ActionEvent event)
		{
			jumpToInterviewStep("P1aT2S2");
		}

	}
	
	class ModelActionHandler extends JumpHandler
	{
		public void actionPerformed(ActionEvent event)
		{
			switchToView(DiagramView.getViewName());
		}
		
	}

	private JMenu createSubmenu(String name, String[] items)
	{
		JMenu submenu = new JMenu(name);
		for(int i = 0; i < items.length; ++i)
			submenu.add(items[i]);
		return submenu;
	}

	private JMenu createHelpMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Help"));
		menu.add(new JMenuItem(actions.get(ActionAbout.class)));
		return menu;
	}
}
