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
package org.miradi.dialogs.threatrating;

import javax.swing.Icon;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.base.ObjectManagementPanel;
import org.miradi.dialogs.threatrating.properties.ThreatRatingMultiPropertiesPanel;
import org.miradi.dialogs.threatrating.upperPanel.ThreatRatingMultiTablePanel;
import org.miradi.dialogs.threatrating.upperPanel.ThreatRatingUpperPanel;
import org.miradi.icons.StressIcon;
import org.miradi.main.MainWindow;
import org.miradi.rtf.RtfManagementExporter;
import org.miradi.rtf.RtfWriter;
import org.miradi.utils.AbstractTableExporter;

public class ThreatRatingManagementPanel extends ObjectManagementPanel
{
	public static ThreatRatingManagementPanel create(MainWindow mainWindowToUse) throws Exception
	{
		ThreatRatingMultiTablePanel multiTablePanel = new ThreatRatingMultiTablePanel(mainWindowToUse);
		ObjectDataInputPanel propertiesPanel = new ThreatRatingMultiPropertiesPanel(mainWindowToUse, multiTablePanel);
		
		ThreatRatingUpperPanel tablePanel =  ThreatRatingUpperPanel.createThreatStressRatingListTablePanel(
				mainWindowToUse, multiTablePanel, propertiesPanel);
		
		return new ThreatRatingManagementPanel(mainWindowToUse, tablePanel, propertiesPanel);
	}

	public ThreatRatingManagementPanel(MainWindow splitPositionSaverToUse, ThreatRatingUpperPanel listTablePanel, ObjectDataInputPanel propertiesPanel) throws Exception
	{
		super(splitPositionSaverToUse,  listTablePanel, propertiesPanel);
		
		tableExporter = listTablePanel.getMultiTablePanel().getTableForExporting();
	}

	public String getSplitterDescription()
	{
		return getPanelDescription() + SPLITTER_TAG;
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new StressIcon();
	}
	
	public Class getJumpActionClass()
	{
		return null;
	}
	
	public AbstractTableExporter getTableExporter() throws Exception
	{
		return tableExporter;
	}
	
	public boolean isRtfExportable()
	{
		return true;
	}		

	public void exportRtf(RtfWriter writer) throws Exception
	{
		new RtfManagementExporter(getProject()).writeManagement(getTableExporter(), writer);
	}
		
	private static String PANEL_DESCRIPTION = "ThreatStressRating";
	private AbstractTableExporter tableExporter;
}
