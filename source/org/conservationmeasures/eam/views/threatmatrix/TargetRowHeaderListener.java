/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Arrays;
import java.util.Comparator;

import org.conservationmeasures.eam.objects.ConceptualModelNode;




public class TargetRowHeaderListener extends ColumnHeaderListener
{
	public TargetRowHeaderListener(MyThreatGirdPanel threatGirdPanelInUse)
	{
		super(threatGirdPanelInUse);
	}

	
	public void sort(int sortColumnToUse) 
	{
		
		NonEditableThreatMatrixTableModel modelToSort = 
			(NonEditableThreatMatrixTableModel)threatGirdPanel.globalTthreatTable.getModel();
		
		ConceptualModelNode[] threatList = modelToSort.getDirectThreats();
		
		Comparator comparator = new IgnoreCaseStringComparator();
		
		Arrays.sort(threatList, comparator );
		
		modelToSort.setThreatRows(threatList);
		
		if ( getToggle() )  
		{

		}
	}
	
	public  boolean getToggle() 
	{
		sortToggle = !sortToggle;
		return sortToggle;
		
	}
	
	boolean sortToggle;

}