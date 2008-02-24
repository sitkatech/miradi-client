/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.reports;

import org.miradi.main.EAM;
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
	
	@Override
	public String getColumnName(int column)
	{
		return EAM.text("Report");
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
