/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import java.awt.HeadlessException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.conservationmeasures.eam.actions.ActionAbout;
import org.conservationmeasures.eam.actions.ActionAboutBenetech;
import org.conservationmeasures.eam.actions.ActionAboutCMP;
import org.conservationmeasures.eam.actions.ActionClose;
import org.conservationmeasures.eam.actions.ActionConfigureLayers;
import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCopyProjectTo;
import org.conservationmeasures.eam.actions.ActionCreateBendPoint;
import org.conservationmeasures.eam.actions.ActionCreateConceptualModel;
import org.conservationmeasures.eam.actions.ActionCreateResultsChain;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDatabasesDemo;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionDeleteBendPoint;
import org.conservationmeasures.eam.actions.ActionDeleteConceptualModel;
import org.conservationmeasures.eam.actions.ActionDeleteGroupBox;
import org.conservationmeasures.eam.actions.ActionDeleteResultsChain;
import org.conservationmeasures.eam.actions.ActionDiagramProperties;
import org.conservationmeasures.eam.actions.ActionExit;
import org.conservationmeasures.eam.actions.ActionExportProjectReportFile;
import org.conservationmeasures.eam.actions.ActionExportProjectXml;
import org.conservationmeasures.eam.actions.ActionExportTable;
import org.conservationmeasures.eam.actions.ActionExportZippedProjectFile;
import org.conservationmeasures.eam.actions.ActionGroupBoxAddFactor;
import org.conservationmeasures.eam.actions.ActionGroupBoxRemoveFactor;
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
import org.conservationmeasures.eam.actions.ActionInsertGroupBox;
import org.conservationmeasures.eam.actions.ActionInsertIntermediateResult;
import org.conservationmeasures.eam.actions.ActionInsertStrategy;
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.ActionInsertTextBox;
import org.conservationmeasures.eam.actions.ActionInsertThreatReductionResult;
import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionPasteWithoutLinks;
import org.conservationmeasures.eam.actions.ActionPreferences;
import org.conservationmeasures.eam.actions.ActionPrint;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionRenameConceptualModel;
import org.conservationmeasures.eam.actions.ActionRenameResultsChain;
import org.conservationmeasures.eam.actions.ActionReportsDemo;
import org.conservationmeasures.eam.actions.ActionSaveImageJPEG;
import org.conservationmeasures.eam.actions.ActionSelectAll;
import org.conservationmeasures.eam.actions.ActionSelectChain;
import org.conservationmeasures.eam.actions.ActionShowConceptualModel;
import org.conservationmeasures.eam.actions.ActionShowFullModelMode;
import org.conservationmeasures.eam.actions.ActionShowResultsChain;
import org.conservationmeasures.eam.actions.ActionShowSelectedChainMode;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.ActionZoomIn;
import org.conservationmeasures.eam.actions.ActionZoomOut;
import org.conservationmeasures.eam.actions.ActionZoomToFit;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.actions.jump.ActionJumpCloseTheLoop;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.EAMenuItem;
import org.conservationmeasures.eam.main.ViewSwitcher;
import org.conservationmeasures.eam.utils.MenuItemWithoutLocation;
import org.conservationmeasures.eam.views.umbrella.HelpButtonData;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiMenu;

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
		addMenuItem(actions, menu, ActionSaveImageJPEG.class, KeyEvent.VK_J);
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
		addMenuItem(actions, menu, ActionExportProjectXml.class, KeyEvent.VK_X);
		addMenuItem(actions, menu, ActionExportTable.class, KeyEvent.VK_T);
	
// This feature will have to be rewritten to work with the new Planning View
//		addMenuItem(actions, menu, ActionExportBudgetTableTree.class, KeyEvent.VK_F);
		menu.addSeparator();
		
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
		
		JMenuItem undo = addMenuItem(actions, menu, ActionUndo.class, KeyEvent.VK_U);
		setControlKeyAccelerator(undo, 'Z');
		JMenuItem redo = addMenuItem(actions, menu, ActionRedo.class, KeyEvent.VK_R);
		setControlKeyAccelerator(redo, 'Y');
		menu.addSeparator();
		
		JMenuItem cut = addMenuItem(actions, menu, ActionCut.class, KeyEvent.VK_T);
		setControlKeyAccelerator(cut, 'X');
		JMenuItem copy = addMenuItem(actions, menu, ActionCopy.class, KeyEvent.VK_C);
		setControlKeyAccelerator(copy, 'C');
		JMenuItem paste = addMenuItem(actions, menu, ActionPaste.class, KeyEvent.VK_P);
		setControlKeyAccelerator(paste, 'V');
		addMenuItem(actions, menu, ActionPasteWithoutLinks.class, -1);
		menu.addSeparator();
		
		addMenuItem(actions, menu, ActionDelete.class, KeyEvent.VK_D);
		JMenuItem selectAll = addMenuItem(actions, menu, ActionSelectAll.class, KeyEvent.VK_A);
		setControlKeyAccelerator(selectAll, 'A');
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
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertIntermediateResult.class),KeyEvent.VK_R));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertThreatReductionResult.class),KeyEvent.VK_R));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertTarget.class),KeyEvent.VK_T));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertTextBox.class), KeyEvent.VK_X));
		menu.addSeparator();
		menu.add(createGroupBoxMenu(actions));
		menu.addSeparator();
		addMenuItem(actions, menu, ActionInsertFactorLink.class, KeyEvent.VK_I);
		addMenuItem(actions, menu, ActionCreateBendPoint.class, KeyEvent.VK_B);
		addMenuItem(actions, menu, ActionDeleteBendPoint.class, KeyEvent.VK_DELETE);
		
		menu.addSeparator();
		addMenuItem(actions, menu, ActionShowConceptualModel.class, KeyEvent.VK_A);
		addMenuItem(actions, menu, ActionShowResultsChain.class, KeyEvent.VK_R);
		addMenuItem(actions, menu, ActionCreateResultsChain.class);
		addMenuItem(actions, menu, ActionRenameResultsChain.class);
		addMenuItem(actions, menu, ActionDeleteResultsChain.class);
		
		menu.addSeparator();
		addMenuItem(actions, menu, ActionCreateConceptualModel.class);
		addMenuItem(actions, menu, ActionRenameConceptualModel.class);
		addMenuItem(actions, menu, ActionDeleteConceptualModel.class);
		
		menu.addSeparator();
		addMenuItem(actions, menu, ActionDiagramProperties.class);
		
		return menu;
	}

	private JMenu createGroupBoxMenu(Actions actions)
	{
		UiMenu groupBoxMenu = new UiMenu(EAM.text("Menu|Group Box"));	
		groupBoxMenu.setMnemonic(KeyEvent.VK_G);		
		groupBoxMenu.add(createJMenuItemCenterLocation(actions.get(ActionInsertGroupBox.class), KeyEvent.VK_G));
		groupBoxMenu.add(createJMenuItemCenterLocation(actions.get(ActionGroupBoxAddFactor.class), KeyEvent.VK_R));
		groupBoxMenu.add(createJMenuItemCenterLocation(actions.get(ActionGroupBoxRemoveFactor.class), KeyEvent.VK_P));
		groupBoxMenu.add(createJMenuItemCenterLocation(actions.get(ActionDeleteGroupBox.class), KeyEvent.VK_D));
			
		return groupBoxMenu;
	}
		
	private JMenu createViewMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|View"));
		menu.setMnemonic(KeyEvent.VK_V);
		
		Action[] viewSwitchActions = ViewSwitcher.getViewSwitchActions(actions);
		for(int i = 0; i < viewSwitchActions.length; ++i)
			menu.add(viewSwitchActions[i]);
		menu.addSeparator();
// NOTE: Slide show disabled for 1.0.6 release because it is not ready yet
//		addMenuItem(actions, menu, ActionToggleSlideShowPanel.class, KeyEvent.VK_E);
//		addMenuItem(actions, menu, ActionSlideShowViewer.class, KeyEvent.VK_S);
//		menu.addSeparator();
		JMenuItem zoomIn = addMenuItem(actions, menu, ActionZoomIn.class, KeyEvent.VK_I);
		setControlKeyAccelerator(zoomIn, '=');
		JMenuItem zoomOut = addMenuItem(actions, menu, ActionZoomOut.class, KeyEvent.VK_O);
		setControlKeyAccelerator(zoomOut, '-');
		JMenuItem zoomToFit = addMenuItem(actions, menu, ActionZoomToFit.class, KeyEvent.VK_Z);
		setControlKeyAccelerator(zoomToFit, '0');

		menu.addSeparator();
		addMenuItem(actions, menu, ActionConfigureLayers.class, KeyEvent.VK_C);
		addMenuItem(actions, menu, ActionShowSelectedChainMode.class, KeyEvent.VK_S);
		addMenuItem(actions, menu, ActionShowFullModelMode.class, KeyEvent.VK_F);
		return menu;
	}
	
	private JMenu createProcessMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Step-by-Step"));
		menu.setMnemonic(KeyEvent.VK_S);
		
		menu.add(new ProcessMenu1(actions));
		menu.add(new ProcessMenu2(actions));
		menu.add(new ProcessMenu3(actions));
		menu.add(new ProcessMenu4(actions));
		menu.add(new ProcessMenu5(actions));
		menu.add(new JMenuItem(actions.get(ActionJumpCloseTheLoop.class)));
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
	
	
	private JMenuItem addMenuItem(Actions actions, JMenu menu, Class class1)
	{
		EAMenuItem menuItemNewProject = new EAMenuItem(actions.get(class1));
		menu.add(menuItemNewProject);
		return menuItemNewProject; 
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
	
	private void setControlKeyAccelerator(JMenuItem menuItem, char keyLetter)
	{
		menuItem.setAccelerator(KeyStroke.getKeyStroke(keyLetter, InputEvent.CTRL_DOWN_MASK));
	}
}
