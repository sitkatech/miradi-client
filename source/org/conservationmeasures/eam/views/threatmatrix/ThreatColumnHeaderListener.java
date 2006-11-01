/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;


public class ThreatColumnHeaderListener extends ColumnHeaderListener
{
	public ThreatColumnHeaderListener(MyThreatGirdPanel threatGirdPanelInUse)
	{
		super(threatGirdPanelInUse);
	}

	public  Comparable createComparable(int rowIndex)
	{
		return new ColumnObject(rowIndex, threatGirdPanel.globalTthreatTable.getValueAt(rowIndex,sortColumn));
	}
	
	public  int getOldRow(Object object) 
	{
		return((ColumnObject) object).getOldRow();
	}
	
	public  boolean getToggle() {
		sortToggle = !sortToggle;
		return sortToggle;
		
	}
	
	boolean sortToggle;

}
