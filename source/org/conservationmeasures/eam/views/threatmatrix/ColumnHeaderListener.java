/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class ColumnHeaderListener  extends MouseAdapter
{
	public ColumnHeaderListener(MyThreatGirdPanel threatGirdPanelInUse)
	{
		threatGirdPanel = threatGirdPanelInUse;
	}

	public void mouseClicked(MouseEvent e) 
	{
		threatGirdPanel.globalTthreatTable.clearSelection();
		
		int clickedColumn = threatGirdPanel.globalTthreatTable.columnAtPoint(e.getPoint());
		int sortColumn = threatGirdPanel.globalTthreatTable.getColumnModel().getColumn(clickedColumn).getModelIndex();
		
		sort(sortColumn);

		threatGirdPanel.revalidate();
		threatGirdPanel.repaint();
	}

	public abstract void sort(int sortColumnToUse);
	
	MyThreatGirdPanel threatGirdPanel;
	NonEditableThreatMatrixTableModel model;

}
