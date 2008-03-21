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
package org.miradi.views.reports;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.miradi.main.AppPreferences;
import org.miradi.utils.TableWithColumnWidthSaver;

public class ReportSelectionTable extends TableWithColumnWidthSaver
{
	public ReportSelectionTable(ReportSelectionTableModel model)
	{
		super(model);
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setBackground(AppPreferences.getControlPanelBackgroundColor());
		setColumnWidth(0, 250);
	}
	
	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		Dimension size = super.getPreferredScrollableViewportSize();
		size.width = getPreferredSize().width;
		size.height = getPreferredSize().height;
		return size;
	}
	
	public String getReportDirForRow(int row)
	{
		ReportSelectionTableModel model = (ReportSelectionTableModel)getModel();
		return model.getReportDirForRow(row);
	}

	
	@Override
	public String getUniqueTableIdentifier()
	{
		return "ReportSelectionTable";
	}
}
