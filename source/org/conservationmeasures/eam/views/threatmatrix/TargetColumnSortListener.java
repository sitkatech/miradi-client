/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ViewData;


public class TargetColumnSortListener extends ColumnSortListener
{
	public TargetColumnSortListener(ThreatGirdPanel threatGirdPanelInUse)
	{
		super(threatGirdPanelInUse);
		sortToggle = true;
	}

	
	public  boolean toggle() {
		sortToggle = !sortToggle;
		return sortToggle;
	}
	
	
	public static void sort(ThreatGirdPanel threatGirdPanel, String currentSortBy, String currentSortDirection) 
	{
		NonEditableThreatMatrixTableModel modelToSort = 
			(NonEditableThreatMatrixTableModel)threatGirdPanel.getThreatMatrixTable().getModel();
		
		ConceptualModelNode[] threatList = modelToSort.getDirectThreats();
		
		Arrays.sort(threatList, getComparator());
		
		if (currentSortDirection.equals(ViewData.SORT_ASCENDING)) 
			threatList = reverseSort(threatList);
		
		modelToSort.setThreatRows(threatList);
	}
	
	
	public void sort(int sortColumn) 
	{
		
		NonEditableThreatMatrixTableModel modelToSort = 
			(NonEditableThreatMatrixTableModel)threatGirdPanel.getThreatMatrixTable().getModel();
		
		ConceptualModelNode[] threatList = modelToSort.getDirectThreats();
		
		Comparator comparator = getComparator();
		
		Arrays.sort(threatList, comparator );
		
		if ( toggle() )  
			threatList = reverseSort(threatList);

		modelToSort.setThreatRows(threatList);
		
		saveState(sortColumn,null);
		
	}


	private void saveState(int sortColumn,  NonEditableThreatMatrixTableModel modelToSort)
	{
		saveSortState(sortToggle, ViewData.SORT_TARGETS);
	}


	private static Comparator getComparator()
	{
		Comparator comparator;
		comparator = new IgnoreCaseStringComparator();
		return comparator;
	}


	private static ConceptualModelNode[] reverseSort(ConceptualModelNode[] threatList)
	{
		Vector list = new Vector(Arrays.asList(threatList));
		Collections.reverse(list);
		threatList = (ConceptualModelNode[]) list.toArray(new ConceptualModelNode[0]);
		return threatList;
	}

	
	boolean sortToggle;

}