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

	public void mousePressed(MouseEvent e)
	{
		sortColumn = threatGirdPanel.globalTthreatTable.columnAtPoint(e.getPoint());
	}

	public void mouseReleased(MouseEvent e)
	{
		
		sort(sortColumn);

		threatGirdPanel.revalidate();
		threatGirdPanel.repaint();
	}

	public abstract void sort(int sortColumnToUse);
	
	
	int sortColumn = 0;
	MyThreatGirdPanel threatGirdPanel;
	NonEditableThreatMatrixTableModel model;

}
