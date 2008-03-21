/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.summary;

import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpSummaryWizardDefineTeamMembers;
import org.miradi.dialogs.base.ObjectManagementPanel;
import org.miradi.icons.TeamIcon;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.utils.ExportableTableInterface;
import org.miradi.utils.SplitterPositionSaverAndGetter;

public class TeamManagementPanel extends ObjectManagementPanel
{
	public TeamManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, Actions actionsToUse) throws Exception
	{
		super(projectToUse, splitPositionSaverToUse, new TeamPoolTablePanel(projectToUse, actionsToUse),
				new TeamMemberPropertiesPanel(projectToUse));
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
		TeamPoolTable table = createTable();
		BufferedImage image = BufferedImageFactory.createImageFromTable(table);
		return image;
	}
	
	public boolean isExportableTableAvailable()
	{
		return true;
	}
	
	public ExportableTableInterface getExportableTable() throws Exception
	{
		return createTable();
	}

	private TeamPoolTable createTable()
	{
		return new TeamPoolTable(new TeamPoolTableModel(getProject()));
	}
	
	@Override
	public boolean isPrintable()
	{
		return true;
	}
	
	@Override
	public JComponent getPrintableComponent() throws Exception
	{
		return createTable();
	}
	
	@Override
	public Class getJumpActionClass()
	{
		return ActionJumpSummaryWizardDefineTeamMembers.class;
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Title|Team");
}
