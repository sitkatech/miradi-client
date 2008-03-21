/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
