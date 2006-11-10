/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Arrays;
import java.util.Comparator;

import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ViewData;


public class TargetColumnSortListener extends ColumnSortListener
{
	public TargetColumnSortListener(ThreatGridPanel threatGirdPanelInUse)
	{
		super(threatGirdPanelInUse);
		sortToggle = false;
	}
	
	public  boolean toggle() {
		sortToggle = !sortToggle;
		return sortToggle;
	}
	
	
	public  void sort(String currentSortBy, String currentSortDirection) 
	{
		ConceptualModelNode[] threatList = mainTableModel.getDirectThreats();
		
		Comparator comparator = getComparator(currentSortBy);
		
		Arrays.sort(threatList, comparator);
		
		if (currentSortDirection.equals(ViewData.SORT_ASCENDING)) 
			threatList = reverseSort(threatList);
		
		mainTableModel.setThreatRows(threatList);
	}


	
	public void sort(int sortColumn) 
	{
		ConceptualModelNode[] threatList = mainTableModel.getDirectThreats();

		Comparator comparator = getComparator(sortColumn);

		Arrays.sort(threatList, comparator);
		
		if ( toggle() )  
			threatList = reverseSort(threatList);

		mainTableModel.setThreatRows(threatList);
		
		saveState(sortColumn);
	}


	private void saveState(int sortColumn)
	{
		if(isSummaryColumn(sortColumn, mainTableModel))
			saveSortState(sortToggle, ViewData.SORT_SUMMARY);
		else
			saveSortState(sortToggle, mainTableModel.getTargets()[sortColumn]);
	}

	

	private Comparator getComparator(String currentSortBy)
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

	
	
	private Comparator getComparator(int sortColumn)
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
