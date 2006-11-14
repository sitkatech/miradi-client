/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Comparator;

import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;


public class ThreatColumnSortListener extends ColumnSortListener
{
	public ThreatColumnSortListener(ThreatGridPanel threatGirdPanelInUse)
	{
		super(threatGirdPanelInUse);
		sortToggle = true;
	}


	public void saveState(int sortColumn)
	{
		saveSortState(sortToggle, ViewData.SORT_THREATS);
	}

	
	public Comparator getComparator(int sortColumn)
	{
		return  new IgnoreCaseStringComparator();
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