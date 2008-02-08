/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.reports;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.GenericDefaultTableModel;


public class ReportSelectionTableModel extends GenericDefaultTableModel
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
			createReportMap();
		
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
				new Report("Full Project Report", "/reports/FullProjectReport.jasper"),
				new Report("Rare Report", "/reports/RareReport.jasper"),
				new Report("Conceptual Model Report", "/reports/AllConceptualModelsReport.jasper"),
				new Report("Results Chains Report", "/reports/AllResultsChainsReport.jasper"),
		};
	}
	
	private Report[] reports;
	private static final int COLUMN_COUNT = 1;
}
