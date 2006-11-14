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
		{
			saveSortState(sortToggle, ViewData.SORT_SUMMARY);
			return;
		}
		String columnBaseIdToSort = mainTableModel.getTargets()[sortColumn].getId().toString();
		saveSortState(sortToggle, columnBaseIdToSort);
	}



	public Comparator getComparator(String currentSortBy)
	{
		if (currentSortBy.equals(ViewData.SORT_SUMMARY)) 
			return getComparator(mainTableModel.getColumnCount());

		ModelNodeId nodeId = new ModelNodeId(new Integer(currentSortBy).intValue());
		int sortColumn= mainTableModel.findTargetIndexById(nodeId);
		return getComparator(sortColumn);
	}

	
	
	public Comparator getComparator(int sortColumn)
	{
		if(isSummaryColumn(sortColumn, mainTableModel))
			return new SummaryColumnComparator(mainTableModel);
		return new ComparableNode(sortColumn,mainTableModel);
	}

	
	private boolean isSummaryColumn(int sortColumn, NonEditableThreatMatrixTableModel modelToSort) 
	{
		return (sortColumn == modelToSort.getColumnCount()-1);
	}
	
	
	public void setToggle(boolean sortOrder) 
	{
		sortToggle = !sortOrder;
	}
	

	boolean sortToggle;

}
