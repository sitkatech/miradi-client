/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import java.awt.image.BufferedImage;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpTargetViability3Step;
import org.conservationmeasures.eam.dialogs.base.ObjectListManagementPanel;
import org.conservationmeasures.eam.dialogs.indicator.DirectIndicatorPropertiesPanel;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningTreePropertiesPanel;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.icons.KeyEcologicalAttributeIcon;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.BufferedImageFactory;
import org.conservationmeasures.eam.utils.ExportableTableInterface;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class TargetViabilityTreeManagementPanel extends ObjectListManagementPanel
{
	public TargetViabilityTreeManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, FactorId nodeId, Actions actions) throws Exception
	{
		super(projectToUse, splitPositionSaverToUse, TargetViabililtyTreePanel.createTargetViabilityPanel(EAM.getMainWindow(), projectToUse, nodeId),
				new TargetViabilityTreePropertiesPanel(projectToUse, actions));
		panelDescription = PANEL_DESCRIPTION_VIABILITY;
		icon = new KeyEcologicalAttributeIcon();
	}
	
	public TargetViabilityTreeManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, Actions actions) throws Exception
	{
		super(projectToUse, splitPositionSaverToUse, TargetViabililtyTreePanel.createTargetViabilityPoolPanel(EAM.getMainWindow(), projectToUse),
				new TargetViabilityTreePropertiesPanel(projectToUse, actions));
		panelDescription = PANEL_DESCRIPTION_VIABILITY;
		icon = new KeyEcologicalAttributeIcon();

	}
	
	public TargetViabilityTreeManagementPanel(MainWindow mainWindowToUse, ORef factorRef, Actions actions) throws Exception
	{
		super(mainWindowToUse, TargetViabililtyTreePanel.createFactorIndicatorPanel(mainWindowToUse, factorRef, mainWindowToUse.getProject()),
				new DirectIndicatorPropertiesPanel(mainWindowToUse.getProject(), mainWindowToUse.getActions(), ORef.INVALID));
		panelDescription = PANEL_DESCRIPTION_INDICATORS;
		icon = new IndicatorIcon();
	}  
	
	//TODO should use this contructor instead of the constructor that creates DirectIndicatorPropertiesPanel, would be better to have a PlanningTreePropertiesPanel
	public TargetViabilityTreeManagementPanel(MainWindow mainWindowToUse, TargetViabililtyTreePanel treePanel, PlanningTreePropertiesPanel propertiesPanel) throws Exception
	{
		super(mainWindowToUse, treePanel, propertiesPanel);
		panelDescription = PANEL_DESCRIPTION_INDICATORS;
		icon = new IndicatorIcon();
	}
	
	
	public static TargetViabilityTreeManagementPanel createTargetViabilityTreeManagementPanel(MainWindow mainWindowToUse, ORef factorRef) throws Exception
	{
		TargetViabililtyTreePanel treePanel = TargetViabililtyTreePanel.createFactorIndicatorPanel(mainWindowToUse, factorRef, mainWindowToUse.getProject());
		PlanningTreePropertiesPanel propertiesPanel = new PlanningTreePropertiesPanel(mainWindowToUse, ORef.INVALID, treePanel.getTree());
		
		return new TargetViabilityTreeManagementPanel(mainWindowToUse, treePanel, propertiesPanel);
	}
	
	public String getSplitterDescription()
	{
		return PANEL_DESCRIPTION_VIABILITY + SPLITTER_TAG;
	}

	public String getPanelDescription()
	{
		return panelDescription;
	}
	
	public Icon getIcon()
	{
		return icon;
	}
	
	public boolean isImageAvailable()
	{
		return true;
	}
	
	public BufferedImage getImage() throws Exception
	{
		ViabilityTreeModel model = new ViabilityTreeModel(new ViabilityRoot(getProject()));
		TargetViabilityTreeTable treeTable = new TargetViabilityTreeTable(getProject(), model);
		treeTable.restoreTreeState();
		BufferedImage image = BufferedImageFactory.createImageFromTable(treeTable);
		return image;
	}
	
	public boolean isExportableTableAvailable()
	{
		return true;
	}
	
	public ExportableTableInterface getExportableTable() throws Exception
	{
		ViabilityTreeModel model = new ViabilityTreeModel(new ViabilityRoot(getProject()));
		TargetViabilityTreeTable treeTable = new TargetViabilityTreeTable(getProject(), model);
		treeTable.restoreTreeState();
		
		return treeTable;
	}
	
	public Class getJumpActionClass()
	{
		return ActionJumpTargetViability3Step.class;
	}
	
	private String panelDescription;
	private Icon icon;
	
	private static String PANEL_DESCRIPTION_VIABILITY = EAM.text("Tab|Viability"); 
	private static String PANEL_DESCRIPTION_INDICATORS = EAM.text("Tab|Indicators"); 
}
