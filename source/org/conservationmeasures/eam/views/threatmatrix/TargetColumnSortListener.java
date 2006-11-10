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
	public TargetColumnSortListener(ThreatGirdPanel threatGirdPanelInUse)
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
		NonEditableThreatMatrixTableModel modelToSort = 
			(NonEditableThreatMatrixTableModel)threatGirdPanel.getThreatMatrixTable().getModel();
		
		ConceptualModelNode[] threatList = modelToSort.getDirectThreats();
		
		Comparator comparator = getComparator(currentSortBy, modelToSort);
		
		Arrays.sort(threatList, comparator);
		
		if (currentSortDirection.equals(ViewData.SORT_ASCENDING)) 
			threatList = reverseSort(threatList);
		
		modelToSort.setThreatRows(threatList);
	}


	
	public void sort(int sortColumn) 
	{
		NonEditableThreatMatrixTableModel modelToSort = 
			(NonEditableThreatMatrixTableModel)threatGirdPanel.getThreatMatrixTable().getModel();
		
		ConceptualModelNode[] threatList = modelToSort.getDirectThreats();

		Comparator comparator = getComparator(sortColumn, modelToSort);

		Arrays.sort(threatList, comparator);
		
		if ( toggle() )  
			threatList = reverseSort(threatList);

		modelToSort.setThreatRows(threatList);
		
		saveState(sortColumn, modelToSort);
	}


	private void saveState(int sortColumn, NonEditableThreatMatrixTableModel modelToSort)
	{
		if(isSummaryColumn(sortColumn, modelToSort))
			saveSortState(sortToggle, ViewData.SORT_SUMMARY);
		else
			saveSortState(sortToggle, modelToSort.getTargets()[sortColumn]);
	}

	

	private Comparator getComparator(String currentSortBy, NonEditableThreatMatrixTableModel modelToSort)
	{
		Comparator comparator = null;
		if (currentSortBy.equals(ViewData.SORT_SUMMARY)) 
			comparator = getComparator(modelToSort.getColumnCount(),modelToSort);
		else
		{
			ModelNodeId nodeId = new ModelNodeId(new Integer(currentSortBy).intValue());
			int sortColumn= modelToSort.findTargetIndexById(nodeId);
			comparator = getComparator(sortColumn, modelToSort);
		}
		return comparator;
	}

	
	
	private Comparator getComparator(int sortColumn, NonEditableThreatMatrixTableModel modelToSort)
	{
		Comparator comparator;
		if(isSummaryColumn(sortColumn, modelToSort))
			comparator = new SummaryColumnComparator(modelToSort);
		else
			comparator = new ComparableNode(sortColumn,modelToSort);
		return comparator;
	}

	
	private boolean isSummaryColumn(int sortColumn, NonEditableThreatMatrixTableModel modelToSort) 
	{
	 return (sortColumn == modelToSort.getColumnCount()-1);
	}
	

	boolean sortToggle;

}
