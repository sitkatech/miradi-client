/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Comparator;

import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objects.ViewData;


public class TargetColumnSortListener extends ColumnSortListener
{
	public TargetColumnSortListener(ThreatGridPanel threatGirdPanelInUse)
	{
		super(threatGirdPanelInUse);
		sortToggle = false;
	}
	
	public  boolean toggle() 
	{
		sortToggle = !sortToggle;
		return sortToggle;
	}
	

	public void saveState(int sortColumn)
	{
		if(isSummaryColumn(sortColumn, mainTableModel))
			saveSortState(sortToggle, ViewData.SORT_SUMMARY);
		else
			saveSortState(sortToggle, mainTableModel.getTargets()[sortColumn]);
	}

	

	public Comparator getComparator(String currentSortBy)
	{
		Comparator comparator = null;
		if (currentSortBy.equals(ViewData.SORT_SUMMARY)) 
			comparator = getComparator(mainTableModel.getColumnCount());
		else
		{
			ModelNodeId nodeId = new ModelNodeId(new Integer(currentSortBy).intValue());
			int sortColumn= mainTableModel.findTargetIndexById(nodeId);
			comparator = getComparator(sortColumn);
		}
		return comparator;
	}

	
	
	public Comparator getComparator(int sortColumn)
	{
		Comparator comparator;
		if(isSummaryColumn(sortColumn, mainTableModel))
			comparator = new SummaryColumnComparator(mainTableModel);
		else
			comparator = new ComparableNode(sortColumn,mainTableModel);
		return comparator;
	}

	
	private boolean isSummaryColumn(int sortColumn, NonEditableThreatMatrixTableModel modelToSort) 
	{
		return (sortColumn == modelToSort.getColumnCount()-1);
	}
	

	boolean sortToggle;

}
