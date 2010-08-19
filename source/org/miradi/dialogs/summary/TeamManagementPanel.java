/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.actions.jump.ActionJumpSummaryWizardDefineTeamMembers;
import org.miradi.dialogs.base.ObjectManagementPanel;
import org.miradi.icons.TeamIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.rtf.RtfManagementExporter;
import org.miradi.rtf.RtfWriter;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.utils.ImageTooLargeException;
import org.miradi.utils.ObjectTableModelExporter;
import org.miradi.utils.TableExporter;

public class TeamManagementPanel extends ObjectManagementPanel
{
	public TeamManagementPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse, new TeamPoolTablePanel(mainWindowToUse),
				new TeamMemberPropertiesPanel(mainWindowToUse.getProject()));
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Team");
	}
	
	@Override
	public Icon getIcon()
	{
		return new TeamIcon();
	}
	
	@Override
	public boolean isImageAvailable()
	{
		return true;
	}
	
	@Override
	public BufferedImage getImage() throws ImageTooLargeException
	{
		TeamPoolTable table = createTable();
		BufferedImage image = BufferedImageFactory.createImageFromTable(table);
		return image;
	}
	
	@Override
	public boolean isExportableTableAvailable()
	{
		return true;
	}
	
	@Override
	public TableExporter getTableExporter() throws Exception
	{
		return new ObjectTableModelExporter(createTableModel());
	}

	private TeamPoolTable createTable()
	{
		return new TeamPoolTable(getMainWindow(), createTableModel());
	}

	private TeamPoolTableModel createTableModel()
	{
		return new TeamPoolTableModel(getProject());
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
	
	@Override
	public boolean isRtfExportable()
	{
		return true;
	}
		
	@Override
	public void exportRtf(RtfWriter writer) throws Exception
	{
		new RtfManagementExporter(getProject()).writeManagement(getTableExporter(), writer);
	}
}
