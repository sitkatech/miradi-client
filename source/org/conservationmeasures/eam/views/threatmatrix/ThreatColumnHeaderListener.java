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

	public int[] sortByColumn(boolean  assending)
	{
		ThreatTableSorter tabelSorter = new ThreatTableSorter(threatGirdPanel.project, model);
		return tabelSorter.sortByColumn( sortColumn,  false);
	}
}
