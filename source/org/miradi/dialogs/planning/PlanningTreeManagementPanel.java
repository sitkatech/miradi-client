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
package org.miradi.dialogs.planning;

import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreePropertiesPanel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.icons.PlanningIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.utils.ExportableTableInterface;
import org.miradi.utils.RtfWriter;

public class PlanningTreeManagementPanel extends ObjectListManagementPanel
{
	public PlanningTreeManagementPanel(MainWindow mainWindowToUse, PlanningTreeTablePanel planningTreeTablePanel, PlanningTreePropertiesPanel planningTreePropertiesPanel) throws Exception
	{
		super(mainWindowToUse, planningTreeTablePanel, planningTreePropertiesPanel);
		mainWindow = mainWindowToUse;
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	@Override
	public Icon getIcon()
	{
		return new PlanningIcon();
	}
	
	@Override
	public boolean isImageAvailable()
	{
		return true;
	}
	
	@Override
	public BufferedImage getImage() throws Exception
	{
		JComponent panel = getPrintableComponent();
		BufferedImage image = BufferedImageFactory.createImageFromComponent(panel);
		return image;
	}

	@Override
	public boolean isExportableTableAvailable()
	{
		return true;
	}
	
	public ExportableTableInterface getExportableTable() throws Exception
	{
		PlanningTreeTablePanel panel = PlanningTreeTablePanel.createPlanningTreeTablePanelWithoutButtons(mainWindow);
		ExportableTableInterface table = panel.getTableForExporting();
		panel.dispose();
		
		return table;
	}

	@Override
	public JComponent getPrintableComponent() throws Exception
	{
		return PlanningTreeTablePanel.createPrintablePlanningTreeTablePanel(mainWindow);
	}
	
	public boolean isRtfExportable()
	{
		return true;
	}		

	//TODO this method will be refactored and moved somehwere else.  
	// This method was a proof of concept for rtf tables
	public void exportRtf(RtfWriter writer) throws Exception
	{
		ExportableTableInterface exportableTable = getExportableTable();
		
		String rowHeaderContent = "{";
		String rowHeaderFormatting = "{\\trowd \\trgaph \\trhdr \\intbl ";
		for (int headerIndex = 0; headerIndex < exportableTable.getColumnCount(); ++headerIndex)
		{
			rowHeaderContent += exportableTable.getHeaderFor(headerIndex) + " \\cell ";
			rowHeaderFormatting += " \\cellx ";
		}
		rowHeaderContent += "}";
		rowHeaderFormatting += " \\row }";
		
		writer.writeln(rowHeaderContent);
		writer.writeln(rowHeaderFormatting);
		
		for (int row = 0; row < exportableTable.getRowCount(); ++row)
		{
			String rowContent = "{";
			String rowFormating = "{\\trowd \\trgaph \\intbl ";
			for (int column = 0; column < exportableTable.getColumnCount(); ++column)
			{
				rowContent += exportableTable.getValueAt(row, column) +" \\cell ";
				rowFormating += " \\cellx ";	
			}
			rowContent += "}";
			rowFormating += " \\row }";
			
			writer.writeln(rowContent);
			writer.writeln(rowFormating);
		}
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Tab|Planning");
	
	private MainWindow mainWindow;
}
