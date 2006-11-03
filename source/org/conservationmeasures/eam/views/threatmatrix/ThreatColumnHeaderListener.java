/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.conservationmeasures.eam.objects.ConceptualModelNode;


public class ThreatColumnHeaderListener extends ColumnHeaderListener
{
	public ThreatColumnHeaderListener(MyThreatGirdPanel threatGirdPanelInUse)
	{
		super(threatGirdPanelInUse);
	}
	
	public  boolean toggle() {
		sortToggle = !sortToggle;
		return sortToggle;
	}
	
	public void sort(int sortColumnToUse) 
	{
		
		NonEditableThreatMatrixTableModel modelToSort = 
			(NonEditableThreatMatrixTableModel)threatGirdPanel.globalTthreatTable.getModel();
		
		ConceptualModelNode[] threatList = modelToSort.getDirectThreats();

		Comparator comparator = null;
		if(sortColumnToUse == modelToSort.getColumnCount()-1)
			comparator = new SummaryColumnComparator(modelToSort);
		else
			comparator = new ComparableNode(sortColumnToUse,modelToSort);

		Arrays.sort(threatList, comparator);
		
		if ( toggle() )  
			threatList = reverseSort(threatList);

		modelToSort.setThreatRows(threatList);
	}


	private ConceptualModelNode[] reverseSort(ConceptualModelNode[] threatList)
	{
		ArrayList list = new ArrayList(Arrays.asList(threatList));
		Collections.reverse(list);
		threatList = (ConceptualModelNode[]) list.toArray(new ConceptualModelNode[0]);
		return threatList;
	}
	

	boolean sortToggle;

}
