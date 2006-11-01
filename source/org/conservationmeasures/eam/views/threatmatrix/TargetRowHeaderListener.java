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

	
	public void sort(int sortColumnToUse) 
	{
	}
	
	public  boolean getToggle() 
	{
		sortToggle = !sortToggle;
		return sortToggle;
		
	}
	
	boolean sortToggle;

}