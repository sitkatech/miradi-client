/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Comparator;

import org.conservationmeasures.eam.objects.ViewData;


public class ThreatColumnSortListener extends ColumnSortListener
{
	public ThreatColumnSortListener(ThreatGridPanel threatGirdPanelInUse)
	{
		super(threatGirdPanelInUse);
		sortToggle = true;
	}

	public  boolean toggle() 
	{
		sortToggle = !sortToggle;
		return sortToggle;
	}
	

	public void saveState(int sortColumn)
	{
		saveSortState(sortToggle, ViewData.SORT_THREATS);
	}

	
	public Comparator getComparator(String currentSortBy)
	{
		return getComparator(0);
	}
	
	
	public Comparator getComparator(int sortColumn)
	{
		return  new IgnoreCaseStringComparator();
	}

	
	boolean sortToggle;




}