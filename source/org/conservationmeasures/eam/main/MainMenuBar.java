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
import org.conservationmeasures.eam.actions.ActionExportZipFile;
import org.conservationmeasures.eam.actions.ActionImportTncCapWorkbook;
import org.conservationmeasures.eam.actions.ActionImportZipFile;
import org.conservationmeasures.eam.actions.ActionInsertFactorLink;
import org.conservationmeasures.eam.actions.ActionInsertDirectThreat;
import org.conservationmeasures.eam.actions.ActionInsertDraftStrategy;
import org.conservationmeasures.eam.actions.ActionInsertContributingFactor;
import org.conservationmeasures.eam.actions.ActionInsertStrategy;
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.actions.ActionProjectSaveAs;
import org.conservationmeasures.eam.actions.ActionShowFullModelMode;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionPasteWithoutLinks;
import org.conservationmeasures.eam.actions.ActionPreferences;
import org.conservationmeasures.eam.actions.ActionPrint;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionSaveImage;
import org.conservationmeasures.eam.actions.ActionSelectAll;
import org.conservationmeasures.eam.actions.ActionSelectChain;
import org.conservationmeasures.eam.actions.ActionShowSelectedChainMode;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.ActionZoomIn;
import org.conservationmeasures.eam.actions.ActionZoomOut;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.actions.jump.ActionJumpActivitiesAndActionPlan;
import org.conservationmeasures.eam.actions.jump.ActionJumpAdaptAndMonitorPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeData;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeInterventions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeProjectCapacity;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeResourcesFeasibilityAndRisk;
import org.conservationmeasures.eam.actions.jump.ActionJumpArticulateCoreAssumptions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAssessStakeholders;
import org.conservationmeasures.eam.actions.jump.ActionJumpCloseTheLoop;
import org.conservationmeasures.eam.actions.jump.ActionJumpCommunicateResults;
import org.conservationmeasures.eam.actions.jump.ActionJumpCreate;
import org.conservationmeasures.eam.actions.jump.ActionJumpCreateModel;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineAudiences;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineIndicators;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineScope;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineTasks;
import org.conservationmeasures.eam.actions.jump.ActionJumpDescribeTargets;
import org.conservationmeasures.eam.actions.jump.ActionJumpDesignateLeader;
import org.conservationmeasures.eam.actions.jump.ActionJumpDetermineNeeds;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopActivities;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopBudgets;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopCharter;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopObjectives;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopTargetGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpDocument;
import org.conservationmeasures.eam.actions.jump.ActionJumpEstablishVision;
import org.conservationmeasures.eam.actions.jump.ActionJumpGroundTruthRevise;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyIndirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyTargets;
import org.conservationmeasures.eam.actions.jump.ActionJumpImplementPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpPlanDataStorage;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDraftStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpRefinePlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpResultsChains;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectAppropriateMethods;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectTeam;
import org.conservationmeasures.eam.actions.jump.ActionJumpShare;
import org.conservationmeasures.eam.actions.jump.ActionJumpShorttermPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpTeamRoles;
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
		menu.add(new JMenuItem(actions.get(ActionProjectSaveAs.class)));
		menu.add(new JMenuItem(actions.get(ActionClose.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionPrint.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionSaveImage.class)));
		menu.add(createExportSetupMenu());
		menu.add(createExportMenu(actions));
		menu.add(createImportMenu(actions));
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
	

	
	private JMenu createExportMenu(Actions actions)
	{
		JMenu menu = new JMenu("Export");
		menu.add(new JMenuItem(actions.get(ActionExportZipFile.class)));
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
	
	private JMenu createImportMenu(Actions actions)
	{
		JMenu menu = new JMenu("Import");
		menu.add(new JMenuItem(actions.get(ActionImportZipFile.class)));
		menu.add(new JMenuItem(actions.get(ActionImportTncCapWorkbook.class)));
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
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertDraftStrategy.class)));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertStrategy.class)));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertContributingFactor.class)));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertDirectThreat.class)));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertTarget.class)));
		menu.addSeparator();
		menu.add(new JMenuItem(actions.get(ActionInsertFactorLink.class)));
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
		menu.add(new JMenuItem(actions.get(ActionShowSelectedChainMode.class)));
		menu.add(new JMenuItem(actions.get(ActionShowFullModelMode.class)));
		return menu;
	}
	
	private JMenu createMenu1(Actions actions)
	{
		JMenu menu1 = new JMenu("1. Conceptualize Your Project");
		
		JMenu menu1a = new JMenu("1A. Define initial project team");
		menu1a.add(actions.get(ActionJumpSelectTeam.class));
		menu1a.add(actions.get(ActionJumpDesignateLeader.class));
		menu1a.add(actions.get(ActionJumpDevelopCharter.class));
	
		JMenu menu1b = new JMenu("1B. Define clear and common purpose");
		menu1b.add(actions.get(ActionJumpDefineScope.class));
		menu1b.add(actions.get(ActionJumpEstablishVision.class));
		menu1b.add(actions.get(ActionJumpIdentifyTargets.class));
		menu1b.add(actions.get(ActionJumpDescribeTargets.class));
		
		JMenu menu1c = new JMenu("1C. Understand project context");
		menu1c.add(actions.get(ActionJumpIdentifyDirectThreats.class));
		menu1c.add(actions.get(ActionJumpRankDirectThreats.class));
		menu1c.add(actions.get(ActionJumpIdentifyIndirectThreats.class)); 
		menu1c.add(actions.get(ActionJumpAssessStakeholders.class));
		menu1c.add(actions.get(ActionJumpAnalyzeProjectCapacity.class));
		
		JMenu menu1d = new JMenu("1D. Model project situation");
		menu1d.add(actions.get(ActionJumpArticulateCoreAssumptions.class));
		menu1d.add(actions.get(ActionJumpCreateModel.class));
		menu1d.add(actions.get(ActionJumpGroundTruthRevise.class));
		
		menu1.add(menu1a);
		menu1.add(menu1b);
		menu1.add(menu1c);
		menu1.add(menu1d);
		return menu1;
	}
	
	private JMenu createMenu2(Actions actions)
	{
		JMenu menu2 = new JMenu("2. Plan Your Actions");
		
		JMenu menu2a = new JMenu("2A. Develop clear goal and objectives");
		menu2a.add(actions.get(ActionJumpDevelopTargetGoals.class));
		menu2a.add(actions.get(ActionJumpIdentifyStrategies.class));
		menu2a.add(actions.get(ActionJumpDevelopObjectives.class));
		
		JMenu menu2b = new JMenu("2B. Strategically select activities");
		menu2b.add(actions.get(ActionJumpRankDraftStrategies.class));
		menu2b.add(actions.get(ActionJumpResultsChains.class));
		menu2b.add(actions.get(ActionJumpActivitiesAndActionPlan.class));
		menu2b.add(actions.get(ActionJumpAnalyzeResourcesFeasibilityAndRisk.class));
		
		menu2.add(menu2a);
		menu2.add(menu2b);
		return menu2;
	}
	
	private JMenu createMenu3(Actions actions)
	{
		JMenu menu3 = new JMenu("3. Plan Your Monitoring");
		
		JMenu menu3a = new JMenu("3A. Focus your monitoring");
		menu3a.add(actions.get(ActionJumpDetermineNeeds.class));
		menu3a.add(actions.get(ActionJumpDefineAudiences.class));
		
		JMenu menu3b = new JMenu("3B. Develop a formal monitoring plan");
		menu3b.add(actions.get(ActionJumpDefineIndicators.class));
		menu3b.add(actions.get(ActionJumpSelectAppropriateMethods.class));
		menu3b.add(actions.get(ActionJumpPlanDataStorage.class));
		
		menu3.add(menu3a);
		menu3.add(menu3b);
		return menu3;
	}
	
	private JMenu createMenu4(Actions actions)
	{
		JMenu menu4 = new JMenu("4. Implement Actions and Monitoring");
		
		JMenu menu4a = new JMenu("4A. Develop detailed short-term work plans");
		menu4a.add(actions.get(ActionJumpShorttermPlans.class));
		menu4a.add(actions.get(ActionJumpDevelopActivities.class));
		menu4a.add(actions.get(ActionJumpDefineTasks.class));
		menu4a.add(actions.get(ActionJumpDevelopBudgets.class));
		menu4a.add(actions.get(ActionJumpTeamRoles.class));
		
		menu4.add(menu4a);
		menu4.add(actions.get(ActionJumpImplementPlans.class));
		menu4.add(actions.get(ActionJumpRefinePlans.class));
		return menu4;
	}
	
	private JMenu createMenu5(Actions actions)
	{
		JMenu menu5 = new JMenu("5. Analyze");
		
		menu5.add(actions.get(ActionJumpAnalyzeData.class));
		menu5.add(actions.get(ActionJumpAnalyzeInterventions.class));
		menu5.add(actions.get(ActionJumpCommunicateResults.class));
		return menu5;
	}
	
	private JMenu createMenu6(Actions actions)
	{
		JMenu menu6 = new JMenu("6. Use/Adapt");
		menu6.add(actions.get(ActionJumpAdaptAndMonitorPlans.class));
		return menu6;
	}
	
	private JMenu createMenu7(Actions actions)
	{
		JMenu menu7 = new JMenu("7. Capture and Share Learning");
		
		menu7.add(actions.get(ActionJumpDocument.class));
		menu7.add(actions.get(ActionJumpShare.class));
		menu7.add(actions.get(ActionJumpCreate.class));
		
		return menu7;
	}

	private JMenuItem createMenu8(Actions actions)
	{
		return new JMenuItem(actions.get(ActionJumpCloseTheLoop.class));
	}
	
	private JMenu createProcessMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Step-by-step"));
		menu.add(createMenu1(actions));
		menu.add(createMenu2(actions));
		menu.add(createMenu3(actions));
		menu.add(createMenu4(actions));
		menu.add(createMenu5(actions));
		menu.add(createMenu6(actions));
		menu.add(createMenu7(actions));
		menu.add(createMenu8(actions));
		return menu;
	}

	private JMenu createHelpMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Help"));
		menu.add(new JMenuItem(actions.get(ActionAbout.class)));
		return menu;
	}
}
