/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Arrays;
import java.util.Comparator;

import org.conservationmeasures.eam.objects.ConceptualModelNode;


public class ThreatColumnHeaderListener extends ColumnHeaderListener
{
	public ThreatColumnHeaderListener(MyThreatGirdPanel threatGirdPanelInUse)
	{
		super(threatGirdPanelInUse);
	}

	
	public  boolean getToggle() {
		sortToggle = !sortToggle;
		return sortToggle;
	}
	
	public void sort(int sortColumnToUse) 
	{
		
		NonEditableThreatMatrixTableModel modelToSort = 
			(NonEditableThreatMatrixTableModel)threatGirdPanel.globalTthreatTable.getModel();
		
		ConceptualModelNode[] threatList = modelToSort.getDirectThreats();
		
		System.out.println("Rows="+threatList.length);
		
		Comparator comparator = new ComparableNode(sortColumnToUse, modelToSort );
		
		Arrays.sort(threatList, comparator );
		
		modelToSort.setThreatRows(threatList);
		
		if ( getToggle() )  
		{

		}
	}
	

	boolean sortToggle;

}
