/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.reports;

import java.util.HashMap;

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
		return EAM.text("Reports");
	}
	
	public int getRowCount()
	{	
		return buttonHashMap.size();
	}
	
	public Object getValueAt(int row, int column)
	{
		return getReportName(row);
	}

	public String getReportDirForRow(int row)
	{		
		return (buttonHashMap.values().toArray(new String[0]))[row];
	}
	
	private String getReportName(int row)
	{
		return (buttonHashMap.keySet().toArray(new String[0]))[row];
	}
	
	private void createReportMap()
	{
		buttonHashMap = new HashMap<String, String>();
		buttonHashMap.put("Conceptual Model Report", "/reports/AllConceptualModelsReport.jasper");
		buttonHashMap.put("Results Chains Report", "/reports/AllResultsChainsReport.jasper");
		buttonHashMap.put("Rare Report", "/reports/RareReport.jasper");
	}
	
	private HashMap<String, String> buttonHashMap;
	private static final int COLUMN_COUNT = 1;
}
