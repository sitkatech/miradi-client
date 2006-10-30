/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
		model = ((NonEditableThreatMatrixTableModel)threatGirdPanel.globalTthreatTable.getModel());
		NonEditableThreatMatrixTableModel newModel = new NonEditableThreatMatrixTableModel(threatGirdPanel.project);
		DefaultTableModel newRowHeaderData = new NonEditableRowHeaderTableModel(0,1);
	
		int rowCount = model.getRowCount();
		int columnCount = model.getColumnCount();
		newModel.setRowCount(rowCount);
		newModel.setColumnCount(model.getColumnCount());
		
		newRowHeaderData.setRowCount(rowCount);
		newRowHeaderData.setColumnCount(1);
		
		int[] rows = sortByColumn(false);
		
		for (int rowIndex = 0; rowIndex<rowCount; ++rowIndex) {
			for (int columnIndex = 0; columnIndex<columnCount; ++columnIndex) 
			{
				newModel.setValueAt(model.realDataGetValueAt(rows[rowIndex], columnIndex),rowIndex,columnIndex);
			}
			newRowHeaderData.setValueAt(threatGirdPanel.rowHeaderData.getValueAt(rows[rowIndex], 0)  ,rowIndex,0);
		}
					
		newModel.setColumnIdentifiers(threatGirdPanel.getColumnTargetHeaders());
		threatGirdPanel.globalTthreatTable.setModel(newModel);
		
		JTable newRowHeaderTable = threatGirdPanel.createRowHeaderTable(newRowHeaderData);
		threatGirdPanel.scrollPane.setRowHeaderView(newRowHeaderTable);

		threatGirdPanel.revalidate();
		threatGirdPanel.repaint();
	}
	
	public abstract int[] sortByColumn(boolean  assending);
	
	
	int sortColumn = 0;
	MyThreatGirdPanel threatGirdPanel;
	NonEditableThreatMatrixTableModel model;

}
