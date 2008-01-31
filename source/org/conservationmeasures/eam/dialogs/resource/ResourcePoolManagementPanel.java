/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.resource;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectPoolManagementPanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.icons.ProjectResourceIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.BufferedImageFactory;
import org.conservationmeasures.eam.utils.ExportableTableInterface;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class ResourcePoolManagementPanel extends ObjectPoolManagementPanel
{
	public ResourcePoolManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, Actions actionsToUse, String overviewText) throws Exception
	{
		super(projectToUse, splitPositionSaverToUse, new ResourcePoolTablePanel(projectToUse, actionsToUse),
				new ResourcePropertiesPanel(projectToUse, BaseId.INVALID));

		add(new PanelTitleLabel(overviewText), BorderLayout.BEFORE_FIRST_LINE);
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new ProjectResourceIcon();
	}
	
	@Override
	public boolean isImageAvailable()
	{
		return true;
	}
	
	@Override
	public BufferedImage getImage()
	{
		ResourcePoolTable table = new ResourcePoolTable(new ResourcePoolTableModel(getProject()));
		BufferedImage image = BufferedImageFactory.createImageFromTable(table);
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
		return new ResourcePoolTable(new ResourcePoolTableModel(getProject()));
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Title|Resources"); 
}