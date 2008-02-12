/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.threatmatrix;

import java.util.Comparator;

import org.miradi.objects.ViewData;
import org.miradi.utils.IgnoreCaseStringComparator;


public class ThreatNameColumnHandler extends ColumnSortHandler
{
	public ThreatNameColumnHandler(ThreatGridPanel threatGirdPanelInUse)
	{
		super(threatGirdPanelInUse);
		sortToggle = true;
	}


	public void saveState(int sortColumn) throws Exception
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