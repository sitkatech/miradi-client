/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.reports;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.main.EAM;


public class ReportSelectionTableModel extends AbstractTableModel
{
	public ReportSelectionTableModel()
	{
		createReportMap();
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
	
	private void createReportMap()
	{
		reports = new Report[] {
				new Report("Conceptual Model Report", "/reports/AllConceptualModelsReport.jasper"),
				new Report("Results Chains Report", "/reports/AllResultsChainsReport.jasper"),
				new Report("Rare Report", "/reports/RareReport.jasper"),		
		};
	}
	
	private Report[] reports;
	private static final int COLUMN_COUNT = 1;
}
