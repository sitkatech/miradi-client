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

	public int[] sortByColumn(boolean  assending)
	{
		ThreatTableSorter tabelSorter = 
			new ThreatTableSorter(threatGirdPanel.project, threatGirdPanel.rowHeaderData, "ROWHEADER");
		return tabelSorter.sortByColumn( sortColumn,  assending);
	}
}