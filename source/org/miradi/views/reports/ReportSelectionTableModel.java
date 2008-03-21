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

import org.miradi.utils.GenericDefaultTableModel;


public abstract class ReportSelectionTableModel extends GenericDefaultTableModel
{
	public ReportSelectionTableModel()
	{
		super();
	}
	
	public int getColumnCount()
	{
		return COLUMN_COUNT;
	}
	
	public int getRowCount()
	{	
		if (reports == null)
			reports = getAvailableReports();
		
		return reports.length;
	}
	
	public Object getValueAt(int row, int column)
	{
		return reports[row];
	}

	public String getReportDirForRow(int row)
	{		
		return reports[row].getFileName();
	}
	
	abstract protected Report[] getAvailableReports();
	
	private Report[] reports;
	private static final int COLUMN_COUNT = 1;
}
