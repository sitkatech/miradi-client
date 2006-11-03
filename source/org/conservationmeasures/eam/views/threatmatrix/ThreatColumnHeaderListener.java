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


public class ThreatColumnHeaderListener extends ColumnHeaderListener
{
	public ThreatColumnHeaderListener(MyThreatGirdPanel threatGirdPanelInUse)
	{
		super(threatGirdPanelInUse);
		sortToggle = new boolean[threatGirdPanelInUse.globalTthreatTable.getRowCount()];
	}
	
	public  boolean toggle(int sortColumn) {
		sortToggle[sortColumn] = !sortToggle[sortColumn];
		return sortToggle[sortColumn];
	}
	
	public void sort(int sortColumn) 
	{
		
		NonEditableThreatMatrixTableModel modelToSort = 
			(NonEditableThreatMatrixTableModel)threatGirdPanel.globalTthreatTable.getModel();
		
		ConceptualModelNode[] threatList = modelToSort.getDirectThreats();

		Comparator comparator = null;
		if(sortColumn == modelToSort.getColumnCount()-1)
			comparator = new SummaryColumnComparator(modelToSort);
		else
			comparator = new ComparableNode(sortColumn,modelToSort);

		Arrays.sort(threatList, comparator);
		
		if ( toggle(sortColumn) )  
			threatList = reverseSort(threatList);

		modelToSort.setThreatRows(threatList);
	}


	private ConceptualModelNode[] reverseSort(ConceptualModelNode[] threatList)
	{
		Vector list = new Vector(Arrays.asList(threatList));
		Collections.reverse(list);
		threatList = (ConceptualModelNode[]) list.toArray(new ConceptualModelNode[0]);
		return threatList;
	}
	

	boolean sortToggle[];

}
