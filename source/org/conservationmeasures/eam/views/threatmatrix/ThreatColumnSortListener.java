/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Arrays;
import java.util.Comparator;

import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ViewData;


public class ThreatColumnSortListener extends ColumnSortListener
{
	public ThreatColumnSortListener(ThreatGirdPanel threatGirdPanelInUse)
	{
		super(threatGirdPanelInUse);
		sortToggle = true;
	}

	
	public  boolean toggle() {
		sortToggle = !sortToggle;
		return sortToggle;
	}
	
	
	public void sort(String currentSortBy, String currentSortDirection) 
	{
		ConceptualModelNode[] threatList = mainTableModel.getDirectThreats();
		
		Comparator comparator = getComparator();
			
		Arrays.sort(threatList, comparator);
		
		if (currentSortDirection.equals(ViewData.SORT_ASCENDING)) 
			threatList = reverseSort(threatList);
		
		mainTableModel.setThreatRows(threatList);
	}
	
	
	public void sort(int sortColumn) 
	{
		ConceptualModelNode[] threatList = mainTableModel.getDirectThreats();
		
		Comparator comparator = getComparator();
		
		Arrays.sort(threatList, comparator );
		
		if ( toggle() )  
			threatList = reverseSort(threatList);

		mainTableModel.setThreatRows(threatList);
		
		saveState(sortColumn);
		
	}


	private void saveState(int sortColumn)
	{
		saveSortState(sortToggle, ViewData.SORT_THREATS);
	}


	private Comparator getComparator()
	{
		return  new IgnoreCaseStringComparator();
	}

	
	boolean sortToggle;

}