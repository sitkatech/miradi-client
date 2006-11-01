/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;



public class TargetRowHeaderListener extends ColumnHeaderListener
{
	public TargetRowHeaderListener(MyThreatGirdPanel threatGirdPanelInUse)
	{
		super(threatGirdPanelInUse);
	}

	public  Comparable createComparable(int rowIndex)
	{
		return new IgnoreCaseStringComparator(rowIndex, threatGirdPanel.rowHeaderData.getValueAt(rowIndex,0));
	}
	
	public  int getOldRow(Object object) 
	{
		return((IgnoreCaseStringComparator) object).getOldRow();
	}
	
	public  boolean getToggle() {
		sortToggle = !sortToggle;
		return sortToggle;
		
	}
	
	boolean sortToggle;

}