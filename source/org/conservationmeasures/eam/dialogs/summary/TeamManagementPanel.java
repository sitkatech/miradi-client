/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.summary;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectManagementPanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.icons.TeamIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.BufferedImageFactory;
import org.conservationmeasures.eam.utils.ExportableTableInterface;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class TeamManagementPanel extends ObjectManagementPanel
{
	public TeamManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, Actions actionsToUse) throws Exception
	{
		super(projectToUse, splitPositionSaverToUse, new TeamPoolTablePanel(projectToUse, actionsToUse),
				new TeamMemberPropertiesPanel(projectToUse));

		//TODO change overview text
		add(new PanelTitleLabel(EAM.text("Team Management")), BorderLayout.BEFORE_FIRST_LINE);
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new TeamIcon();
	}
	
	public boolean isImageAvailable()
	{
		return true;
	}
	
	public BufferedImage getImage()
	{
		TeamPoolTable table = new TeamPoolTable(new TeamPoolTableModel(getProject()));
		BufferedImage image = BufferedImageFactory.createImageFromTable(table);
		return image;
	}
	
	public boolean isExportableTableAvailable()
	{
		return true;
	}
	
	public ExportableTableInterface getExportableTable() throws Exception
	{
		return new TeamPoolTable(new TeamPoolTableModel(getProject()));
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Title|Team");
}
