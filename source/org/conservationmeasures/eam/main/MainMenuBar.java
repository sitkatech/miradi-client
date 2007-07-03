/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import java.awt.HeadlessException;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.conservationmeasures.eam.actions.ActionAbout;
import org.conservationmeasures.eam.actions.ActionAboutBenetech;
import org.conservationmeasures.eam.actions.ActionAboutCMP;
import org.conservationmeasures.eam.actions.ActionClose;
import org.conservationmeasures.eam.actions.ActionConfigureLayers;
import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCopyProjectTo;
import org.conservationmeasures.eam.actions.ActionCreateBendPoint;
import org.conservationmeasures.eam.actions.ActionCreateResultsChain;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDatabasesDemo;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionDeleteBendPoint;
import org.conservationmeasures.eam.actions.ActionDeleteResultsChain;
import org.conservationmeasures.eam.actions.ActionExit;
import org.conservationmeasures.eam.actions.ActionExportBudgetTableTree;
import org.conservationmeasures.eam.actions.ActionExportProjectReportFile;
import org.conservationmeasures.eam.actions.ActionExportZippedProjectFile;
import org.conservationmeasures.eam.actions.ActionHelpAdaptiveManagement;
import org.conservationmeasures.eam.actions.ActionHelpAgileSoftware;
import org.conservationmeasures.eam.actions.ActionHelpButtonExamples;
import org.conservationmeasures.eam.actions.ActionHelpButtonMoreInfo;
import org.conservationmeasures.eam.actions.ActionHelpButtonSupport;
import org.conservationmeasures.eam.actions.ActionHelpButtonWorkshop;
import org.conservationmeasures.eam.actions.ActionHelpCMPStandards;
import org.conservationmeasures.eam.actions.ActionHelpComingAttractions;
import org.conservationmeasures.eam.actions.ActionHelpCredits;
import org.conservationmeasures.eam.actions.ActionImportZippedProjectFile;
import org.conservationmeasures.eam.actions.ActionInsertContributingFactor;
import org.conservationmeasures.eam.actions.ActionInsertDirectThreat;
import org.conservationmeasures.eam.actions.ActionInsertDraftStrategy;
import org.conservationmeasures.eam.actions.ActionInsertFactorLink;
import org.conservationmeasures.eam.actions.ActionInsertStrategy;
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.ActionInsertTextBox;
import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionPasteWithoutLinks;
import org.conservationmeasures.eam.actions.ActionPreferences;
import org.conservationmeasures.eam.actions.ActionPrint;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionReportsDemo;
import org.conservationmeasures.eam.actions.ActionSaveImage;
import org.conservationmeasures.eam.actions.ActionSelectAll;
import org.conservationmeasures.eam.actions.ActionSelectChain;
import org.conservationmeasures.eam.actions.ActionShowConceptualModel;
import org.conservationmeasures.eam.actions.ActionShowFullModelMode;
import org.conservationmeasures.eam.actions.ActionShowResultsChain;
import org.conservationmeasures.eam.actions.ActionShowSelectedChainMode;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.ActionZoomIn;
import org.conservationmeasures.eam.actions.ActionZoomOut;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.actions.jump.ActionJumpAdaptAndMonitorPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeData;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeProjectCapacity;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeResourcesFeasibilityAndRisk;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpArticulateCoreAssumptions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAssessStakeholders;
import org.conservationmeasures.eam.actions.jump.ActionJumpCloseTheLoop;
import org.conservationmeasures.eam.actions.jump.ActionJumpCommunicateResults;
import org.conservationmeasures.eam.actions.jump.ActionJumpCreate;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineAudiences;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopCharter;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardDefineTargetsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardIdentifyDirectThreatStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardIdentifyIndirectThreatStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardProjectScopeStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardResultsChainStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardReviewModelAndAdjustStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardVisionStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDocument;
import org.conservationmeasures.eam.actions.jump.ActionJumpFinancialOverviewStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpGroundTruthRevise;
import org.conservationmeasures.eam.actions.jump.ActionJumpImplementPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringWizardDefineIndicatorsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringWizardFocusStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpPlanDataStorage;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDraftStrategiesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpRefinePlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpScheduleOverviewStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectChainStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpShare;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanDevelopGoalStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpStrategicPlanDevelopObjectivesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpSummaryWizardDefineProjectLeader;
import org.conservationmeasures.eam.actions.jump.ActionJumpSummaryWizardDefineTeamMembers;
import org.conservationmeasures.eam.actions.jump.ActionJumpTargetViabilityMethodChoiceStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpThreatMatrixOverviewStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanAssignResourcesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanDevelopActivitiesAndTasksStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpWorkPlanDevelopMethodsAndTasksStep;
import org.conservationmeasures.eam.utils.MenuItemWithoutLocation;
import org.conservationmeasures.eam.utils.MiradiResourceImageIcon;
import org.conservationmeasures.eam.views.umbrella.HelpButtonData;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

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
		menu.setMnemonic(KeyEvent.VK_F);
		
		addMenuItem(actions, menu, ActionNewProject.class, KeyEvent.VK_N);
		addMenuItem(actions, menu, ActionCopyProjectTo.class, KeyEvent.VK_A);
		addMenuItem(actions, menu, ActionSaveImage.class, KeyEvent.VK_J);
		addMenuItem(actions, menu, ActionClose.class, KeyEvent.VK_C);
		menu.addSeparator();
		addMenuItem(actions, menu, ActionPrint.class, KeyEvent.VK_P);
		menu.addSeparator();

		menu.add(createExportMenu(actions));
		menu.add(createImportMenu(actions));
		menu.addSeparator();
		addMenuItem(actions, menu, ActionExit.class, KeyEvent.VK_E);
		return menu;
	}

	private JMenu createExportMenu(Actions actions)
	{
		JMenu menu = new JMenu("Export");
		menu.setMnemonic(KeyEvent.VK_E);
		
		addMenuItem(actions, menu, ActionExportZippedProjectFile.class, KeyEvent.VK_E);
		addMenuItem(actions, menu, ActionExportProjectReportFile.class, KeyEvent.VK_P);
		
		addMenuItem(actions, menu, ActionExportBudgetTableTree.class, KeyEvent.VK_F);
		
		JMenuItem item = addMenuItem(actions, menu,ActionReportsDemo.class, KeyEvent.VK_D);
		item.putClientProperty(HelpButtonData.class, new HelpButtonData(UmbrellaView.class, HelpButtonData.DEMO_AND_DATABASES, HelpButtonData.IMPORT_AND_EXPORT_HTML));

		item = addMenuItem(actions, menu, ActionDatabasesDemo.class, KeyEvent.VK_D);
		item.putClientProperty(HelpButtonData.class, new HelpButtonData(UmbrellaView.class, HelpButtonData.DEMO, HelpButtonData.IMPORT_AND_EXPORT_HTML));
		
		return menu;
	}
	
	private JMenu createImportMenu(Actions actions)
	{
		JMenu menu = new JMenu("Import");
		menu.setMnemonic(KeyEvent.VK_I);
		
		addMenuItem(actions, menu, ActionImportZippedProjectFile.class, KeyEvent.VK_P);
		
		JMenuItem item = addMenuItem(actions, menu, ActionDatabasesDemo.class, KeyEvent.VK_D);
		item.putClientProperty(HelpButtonData.class, new HelpButtonData(UmbrellaView.class, HelpButtonData.DEMO, HelpButtonData.IMPORT_AND_EXPORT_HTML));

		return menu;
	}	
	
	private JMenu createEditMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Edit"));
		menu.setMnemonic(KeyEvent.VK_E);
		
		addMenuItem(actions, menu, ActionUndo.class, KeyEvent.VK_U);
		addMenuItem(actions, menu, ActionRedo.class, KeyEvent.VK_R);
		menu.addSeparator();
		
		addMenuItem(actions, menu, ActionCut.class, KeyEvent.VK_T);
		addMenuItem(actions, menu, ActionCopy.class, KeyEvent.VK_C);
		addMenuItem(actions, menu, ActionPaste.class, KeyEvent.VK_P);
		addMenuItem(actions, menu, ActionPasteWithoutLinks.class, -1);
		menu.addSeparator();
		
		addMenuItem(actions, menu, ActionDelete.class, KeyEvent.VK_D);
		addMenuItem(actions, menu, ActionSelectAll.class, KeyEvent.VK_A);
		addMenuItem(actions, menu, ActionSelectChain.class, KeyEvent.VK_H);
		menu.addSeparator();
		addMenuItem(actions, menu, ActionPreferences.class, KeyEvent.VK_P);
		return menu;
	}
	
	private JMenu createInsertMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Actions"));
		menu.setMnemonic(KeyEvent.VK_I);
		
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertDraftStrategy.class),KeyEvent.VK_D));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertStrategy.class),KeyEvent.VK_S));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertContributingFactor.class),KeyEvent.VK_C));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertDirectThreat.class),KeyEvent.VK_D));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertTarget.class),KeyEvent.VK_T));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertTextBox.class), KeyEvent.VK_X));
		
		menu.addSeparator();
		addMenuItem(actions, menu, ActionInsertFactorLink.class, KeyEvent.VK_I);
		addMenuItem(actions, menu, ActionCreateBendPoint.class, KeyEvent.VK_B);
		addMenuItem(actions, menu, ActionDeleteBendPoint.class, KeyEvent.VK_DELETE);
		
		menu.addSeparator();
		addMenuItem(actions, menu, ActionShowConceptualModel.class, KeyEvent.VK_A);
		addMenuItem(actions, menu, ActionShowResultsChain.class, KeyEvent.VK_S);
		addMenuItem(actions, menu, ActionCreateResultsChain.class, KeyEvent.VK_C);
		addMenuItem(actions, menu, ActionDeleteResultsChain.class, KeyEvent.VK_D);
		
		return menu;
	}

	
	private JMenu createViewMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|View"));
		menu.setMnemonic(KeyEvent.VK_V);
		
		Action[] viewSwitchActions = ViewSwitcher.getViewSwitchActions(actions);
		for(int i = 0; i < viewSwitchActions.length; ++i)
			menu.add(viewSwitchActions[i]);
		menu.addSeparator();
		addMenuItem(actions, menu, ActionZoomIn.class, KeyEvent.VK_I);
		addMenuItem(actions, menu, ActionZoomOut.class, KeyEvent.VK_O);
		menu.addSeparator();
		addMenuItem(actions, menu, ActionConfigureLayers.class, KeyEvent.VK_C);
		addMenuItem(actions, menu, ActionShowSelectedChainMode.class, KeyEvent.VK_O);
		addMenuItem(actions, menu, ActionShowFullModelMode.class, KeyEvent.VK_F);
		
		return menu;
	}
	
	private JMenu createMenu1(Actions actions)
	{
		JMenu menu1 = new JMenu("1. Conceptualize Your Project");
		menu1.setMnemonic(KeyEvent.VK_C);
		
		JMenu menu1a = new JMenu(ProcessSteps.PROCESS_STEP_1A);
		menu1a.setMnemonic(KeyEvent.VK_D);
		
		addMenuItem(actions, menu1a, ActionJumpSummaryWizardDefineTeamMembers.class, KeyEvent.VK_S);
		addMenuItem(actions, menu1a, ActionJumpSummaryWizardDefineProjectLeader.class, KeyEvent.VK_D);
		addMenuItem(actions, menu1a, ActionJumpDevelopCharter.class, KeyEvent.VK_D);
	
		JMenu menu1b = new JMenu(ProcessSteps.PROCESS_STEP_1B);
		menu1b.setMnemonic(KeyEvent.VK_D);
		
		addMenuItem(actions, menu1b, ActionJumpDiagramWizardProjectScopeStep.class, KeyEvent.VK_D);
		addMenuItem(actions, menu1b, ActionJumpDiagramWizardVisionStep.class, KeyEvent.VK_E);
		addMenuItem(actions, menu1b, ActionJumpDiagramWizardDefineTargetsStep.class, KeyEvent.VK_I);
		addMenuItem(actions, menu1b, ActionJumpTargetViabilityMethodChoiceStep.class, KeyEvent.VK_D);
		
		JMenu menu1c = new JMenu(ProcessSteps.PROCESS_STEP_1C);
		menu1c.setMnemonic(KeyEvent.VK_U);
		
		addMenuItem(actions, menu1c, ActionJumpDiagramWizardIdentifyDirectThreatStep.class, KeyEvent.VK_I);
		addMenuItem(actions, menu1c, ActionJumpThreatMatrixOverviewStep.class, KeyEvent.VK_R);
		
		JMenu menu1d = new JMenu(ProcessSteps.PROCESS_STEP_1D);
		menu1d.setMnemonic(KeyEvent.VK_M);
		
		addMenuItem(actions, menu1d, ActionJumpDiagramWizardIdentifyIndirectThreatStep.class, KeyEvent.VK_I);
		addMenuItem(actions, menu1d, ActionJumpAssessStakeholders.class, KeyEvent.VK_A);
		addMenuItem(actions, menu1d, ActionJumpAnalyzeProjectCapacity.class, KeyEvent.VK_A);
		
		addMenuItem(actions, menu1d, ActionJumpArticulateCoreAssumptions.class, KeyEvent.VK_A);
		addMenuItem(actions, menu1d, ActionJumpDiagramWizardReviewModelAndAdjustStep.class, KeyEvent.VK_C);
		addMenuItem(actions, menu1d, ActionJumpGroundTruthRevise.class, KeyEvent.VK_G);
		
		menu1.add(menu1a);
		menu1.add(menu1b);
		menu1.add(menu1c);
		menu1.add(menu1d);
		return menu1;
	}
	
	private JMenu createMenu2(Actions actions)
	{
		JMenu menu2 = new JMenu("2. Plan Your Actions");
		menu2.setMnemonic(KeyEvent.VK_P);
		
		JMenu menu2a = new JMenu(ProcessSteps.PROCESS_STEP_2A);
		menu2a.setMnemonic(KeyEvent.VK_D);
		
		addMenuItem(actions, menu2a, ActionJumpStrategicPlanDevelopGoalStep.class, KeyEvent.VK_D);
		addMenuItem(actions, menu2a, ActionJumpStrategicPlanDevelopObjectivesStep.class, KeyEvent.VK_D);
		
		JMenu menu2b = new JMenu(ProcessSteps.PROCESS_STEP_2B);
		menu2b.setMnemonic(KeyEvent.VK_S);
		
		addMenuItem(actions, menu2b, ActionJumpSelectChainStep.class, KeyEvent.VK_I);
		addMenuItem(actions, menu2b, ActionJumpRankDraftStrategiesStep.class, KeyEvent.VK_R);
		addMenuItem(actions, menu2b, ActionJumpDiagramWizardResultsChainStep.class, KeyEvent.VK_R);
		addMenuItem(actions, menu2b, ActionJumpAnalyzeResourcesFeasibilityAndRisk.class, KeyEvent.VK_A);

		menu2.add(menu2a);
		menu2.add(menu2b);
		return menu2;
	}
	
	private JMenu createMenu3(Actions actions)
	{
		JMenu menu3 = new JMenu("3. Plan Your Monitoring");
		menu3.setMnemonic(KeyEvent.VK_P);
		
		JMenu menu3a = new JMenu(ProcessSteps.PROCESS_STEP_3A);
		menu3a.setMnemonic(KeyEvent.VK_F);
		
		addMenuItem(actions, menu3a, ActionJumpMonitoringWizardFocusStep.class, KeyEvent.VK_D);
		addMenuItem(actions, menu3a, ActionJumpDefineAudiences.class, KeyEvent.VK_D);
		
		JMenu menu3b = new JMenu(ProcessSteps.PROCESS_STEP_3B);
		menu3b.setMnemonic(KeyEvent.VK_D);
		
		addMenuItem(actions, menu3b, ActionJumpMonitoringWizardDefineIndicatorsStep.class, KeyEvent.VK_D);
		addMenuItem(actions, menu3b, ActionJumpPlanDataStorage.class, KeyEvent.VK_P);

		menu3.add(menu3a);
		menu3.add(menu3b);
		return menu3;
	}
	
	private JMenu createMenu4(Actions actions)
	{
		JMenu menu4 = new JMenu("4. Implement Actions and Monitoring");
		menu4.setMnemonic(KeyEvent.VK_I);
		

		JMenu menu4a = new JMenu(ProcessSteps.PROCESS_STEP_4A);
		menu4a.setMnemonic(KeyEvent.VK_D);
		menu4a.setIcon(new MiradiResourceImageIcon("icons/blankicon.png"));
		
		addMenuItem(actions, menu4a, ActionJumpWorkPlanDevelopActivitiesAndTasksStep.class, KeyEvent.VK_A);
		addMenuItem(actions, menu4a, ActionJumpWorkPlanDevelopMethodsAndTasksStep.class, KeyEvent.VK_M);
		addMenuItem(actions, menu4a, ActionJumpWorkPlanAssignResourcesStep.class, KeyEvent.VK_T);
		addMenuItem(actions, menu4a, ActionJumpScheduleOverviewStep.class, KeyEvent.VK_D);
		addMenuItem(actions, menu4a, ActionJumpFinancialOverviewStep.class, KeyEvent.VK_D);

		menu4.add(menu4a);
		
		addMenuItem(actions, menu4, ActionJumpImplementPlans.class, KeyEvent.VK_I);
		addMenuItem(actions, menu4, ActionJumpRefinePlans.class, KeyEvent.VK_R);

		return menu4;
	}
	
	private JMenu createMenu5(Actions actions)
	{
		JMenu menu5 = new JMenu("5. Analyze");
		menu5.setMnemonic(KeyEvent.VK_A);
		
		addMenuItem(actions, menu5, ActionJumpAnalyzeData.class, KeyEvent.VK_A);
		addMenuItem(actions, menu5, ActionJumpAnalyzeStrategies.class, KeyEvent.VK_A);
		addMenuItem(actions, menu5, ActionJumpCommunicateResults.class, KeyEvent.VK_C);
		
		return menu5;
	}
	
	private JMenu createMenu6(Actions actions)
	{
		JMenu menu6 = new JMenu("6. Use/Adapt");
		menu6.setMnemonic(KeyEvent.VK_U);
		addMenuItem(actions, menu6, ActionJumpAdaptAndMonitorPlans.class, KeyEvent.VK_A);
		return menu6;
	}
	
	private JMenu createMenu7(Actions actions)
	{
		JMenu menu7 = new JMenu("7. Capture and Share Learning");
		menu7.setMnemonic(KeyEvent.VK_C);
		
		addMenuItem(actions, menu7, ActionJumpDocument.class, KeyEvent.VK_D);
		addMenuItem(actions, menu7, ActionJumpShare.class, KeyEvent.VK_S);
		addMenuItem(actions, menu7, ActionJumpCreate.class, KeyEvent.VK_C);
		
		return menu7;
	}

	private JMenuItem createMenu8(Actions actions)
	{
		return new JMenuItem(actions.get(ActionJumpCloseTheLoop.class));
	}
	
	private JMenu createProcessMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Step-by-Step"));
		menu.setMnemonic(KeyEvent.VK_S);
		
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
		menu.setMnemonic(KeyEvent.VK_H);
		
		JMenuItem item  = addMenuItem(actions, menu, ActionHelpButtonMoreInfo.class, KeyEvent.VK_I);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(HelpButtonData.MORE_INFO, HelpButtonData.MORE_INFO_HTML));
		
		item = addMenuItem(actions, menu, ActionHelpButtonExamples.class, KeyEvent.VK_E);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(HelpButtonData.EXAMPLES, HelpButtonData.EXAMPLES_HTML));
		
		item  = addMenuItem(actions, menu, ActionHelpButtonWorkshop.class, KeyEvent.VK_W);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(HelpButtonData.WORKSHOP, HelpButtonData.WORKSHOP_HTML));
		
		item  = addMenuItem(actions, menu, ActionHelpButtonSupport.class, KeyEvent.VK_P);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(UmbrellaView.class, HelpButtonData.SUPPORT, HelpButtonData.SUPPORT_HTML));
		
		menu.addSeparator();
		
		item  = addMenuItem(actions, menu, ActionHelpCMPStandards.class, KeyEvent.VK_O);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(UmbrellaView.class,HelpButtonData.CMP_STANDARDS, HelpButtonData.CMP_STANDARDS_HTML));


		item  = addMenuItem(actions, menu, ActionHelpAdaptiveManagement.class, KeyEvent.VK_M);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(UmbrellaView.class,HelpButtonData.ADAPTIVE_MANAGEMENT, HelpButtonData.ADAPTIVE_MANAGEMENT_HTML));

		
		item  = addMenuItem(actions, menu, ActionHelpAgileSoftware.class, KeyEvent.VK_S);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(UmbrellaView.class,HelpButtonData.AGILE_SOFTWARE, HelpButtonData.AGILE_SOFTWARE_HTML));

		menu.addSeparator();
		
		item  = addMenuItem(actions, menu, ActionHelpComingAttractions.class, KeyEvent.VK_T);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(UmbrellaView.class, HelpButtonData.COMING_ATTACTIONS, HelpButtonData.COMING_ATTRACTIONS_HTML));

		
		item  = addMenuItem(actions, menu, ActionHelpCredits.class, KeyEvent.VK_R);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(UmbrellaView.class,HelpButtonData.CREDITS, HelpButtonData.CREDITS_HTML));
		
		item  = addMenuItem(actions, menu, ActionAboutBenetech.class, KeyEvent.VK_B);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(UmbrellaView.class,HelpButtonData.ABOUT_BENETECH, HelpButtonData.ABOUT_BENETECH_HTML));

		item  = addMenuItem(actions, menu, ActionAboutCMP.class, KeyEvent.VK_C);
		item.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(UmbrellaView.class,HelpButtonData.ABOUT_CMP, HelpButtonData.ABOUT_CMP_HTML));

		menu.addSeparator();
		
		addMenuItem(actions, menu, ActionAbout.class, KeyEvent.VK_A);
		
		
		return menu;
	}
	
	
	private JMenuItem addMenuItem(Actions actions, JMenu menu, Class class1, int mnemonic)
	{
		EAMenuItem menuItemNewProject = new EAMenuItem(actions.get(class1), mnemonic);
		menu.add(menuItemNewProject);
		return menuItemNewProject; 
	}
	
	
	private JMenuItem createJMenuItemCenterLocation(EAMAction action, int mnemonic)
	{
		JMenuItem centeredLocationAction = new MenuItemWithoutLocation(action);
		centeredLocationAction.setMnemonic(mnemonic);
		return centeredLocationAction;
	}
}
