/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.viability;

import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpTargetViability3Step;
import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.dialogs.indicator.DirectIndicatorPropertiesPanel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreePropertiesPanel;
import org.miradi.icons.IndicatorIcon;
import org.miradi.icons.KeyEcologicalAttributeIcon;
import org.miradi.ids.FactorId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.utils.ExportableTableInterface;
import org.miradi.utils.SplitterPositionSaverAndGetter;

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
	
	@Override
	public Icon getIcon()
	{
		return icon;
	}
	
	@Override
	public boolean isImageAvailable()
	{
		return true;
	}
	
	@Override
	public BufferedImage getImage() throws Exception
	{
		BufferedImage image = BufferedImageFactory.createImageFromTable(getTreeTable());
		return image;
	}
	
	@Override
	public boolean isExportableTableAvailable()
	{
		return true;
	}
	
	@Override
	public ExportableTableInterface getExportableTable() throws Exception
	{
		return getTreeTable();
	}

	private TargetViabilityTreeTable getTreeTable() throws Exception
	{
		ViabilityTreeModel model = new ViabilityTreeModel(new ViabilityRoot(getProject()));
		TargetViabilityTreeTable treeTable = new TargetViabilityTreeTable(getProject(), model);
		treeTable.restoreTreeState();
		return treeTable;
	}
	
	@Override
	public boolean isPrintable()
	{
		return true;
	}
	
	@Override
	public JComponent getPrintableComponent() throws Exception
	{
		return getTreeTable();
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
