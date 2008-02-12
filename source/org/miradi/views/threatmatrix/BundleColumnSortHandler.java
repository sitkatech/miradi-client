/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.threatmatrix;

import java.util.Comparator;

import org.miradi.objects.ViewData;


public class BundleColumnSortHandler extends ColumnSortHandler
{
	public BundleColumnSortHandler(ThreatGridPanel threatGirdPanelInUse)
	{
		super(threatGirdPanelInUse);
		sortToggle = false;
	}


	public void saveState(int sortColumn) throws Exception
	{
		ThreatMatrixTableModel mainTableModel = (ThreatMatrixTableModel)threatGridPanel.getThreatMatrixTable().getModel();
		if(isSummaryColumn(sortColumn, mainTableModel))
		{
			saveSortState(sortToggle, ViewData.SORT_SUMMARY);
			return;
		}
		String columnBaseIdToSort = mainTableModel.getTargets()[sortColumn].getId().toString();
		saveSortState(sortToggle, columnBaseIdToSort);
	}

	
	public Comparator getComparator(int sortColumn)
	{
		ThreatMatrixTableModel mainTableModel = (ThreatMatrixTableModel)threatGridPanel.getThreatMatrixTable().getModel();
		if(isSummaryColumn(sortColumn, mainTableModel))
			return new SummaryColumnComparator(mainTableModel);
		return new FactorComparator(sortColumn,mainTableModel);
	}

	
	private boolean isSummaryColumn(int sortColumn, ThreatMatrixTableModel modelToSort) 
	{
		return (sortColumn == threatGridPanel.getThreatMatrixTable().getSummaryColumn());
	}
	
	
	public void setToggle(boolean sortOrder) 
	{
		sortToggle = !sortOrder;
	}
	
	
	public  boolean getToggle() 
	{
		sortToggle = !sortToggle;
		return sortToggle;
	}
	
	
	boolean sortToggle;

}
