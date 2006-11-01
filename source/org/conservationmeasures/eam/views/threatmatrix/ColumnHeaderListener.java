/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

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
	
		rowCount = model.getRowCount();
		columnCount = model.getColumnCount();
		
		newModel.setRowCount(rowCount);
		newModel.setColumnCount(model.getColumnCount());
		
		newRowHeaderData.setRowCount(rowCount);
		newRowHeaderData.setColumnCount(1);
		
		int[] rows = sortByColumn();
		
		buildTheatTableAndRowHeaderDataModels(newModel, newRowHeaderData, rows);
		
		setUpThreatTableUiForNewModel(newModel, newRowHeaderData);

		threatGirdPanel.revalidate();
		threatGirdPanel.repaint();
	}


	private void setUpThreatTableUiForNewModel(NonEditableThreatMatrixTableModel newModel, DefaultTableModel newRowHeaderData)
	{
		int[] oldWidths = getThreatTableColumnWidths(threatGirdPanel.globalTthreatTable);
		threatGirdPanel.globalTthreatTable.setModel(newModel);
		newModel.setColumnIdentifiers(threatGirdPanel.getColumnTargetHeaders());
		setThreatTableColumnWidths(threatGirdPanel.globalTthreatTable, oldWidths);
		JTable newRowHeaderTable = threatGirdPanel.createRowHeaderTable(newRowHeaderData);
		threatGirdPanel.scrollPane.setRowHeaderView(newRowHeaderTable);
	}


	private void buildTheatTableAndRowHeaderDataModels(NonEditableThreatMatrixTableModel newModel, DefaultTableModel newRowHeaderData, int[] rows)
	{
		for (int rowIndex = 0; rowIndex<rowCount; ++rowIndex) {
			for (int columnIndex = 0; columnIndex<columnCount; ++columnIndex) 
			{
				newModel.setValueAt(model.realDataGetValueAt(rows[rowIndex], columnIndex),rowIndex,columnIndex);
			}
			newRowHeaderData.setValueAt(threatGirdPanel.rowHeaderData.getValueAt(rows[rowIndex], 0)  ,rowIndex,0);
		}
	}
	
	public int[] getThreatTableColumnWidths(JTable threatTable) {
		
		Enumeration columns = threatTable.getColumnModel().getColumns();
		int[] oldWidths = new int[threatTable.getColumnCount()];
		while(columns.hasMoreElements())
		{
			TableColumn columnToAdjust = (TableColumn)columns.nextElement();
			oldWidths[columnToAdjust.getModelIndex()] = columnToAdjust.getWidth();
		}
		return oldWidths;
	}
	
	public void setThreatTableColumnWidths(JTable threatTable, int[] oldWidths)
	{
		Enumeration columns = threatTable.getColumnModel().getColumns();
		while(columns.hasMoreElements())
		{
			TableColumn columnToAdjust = (TableColumn)columns.nextElement();
			columnToAdjust.setHeaderRenderer(new TargetRowHeaderRenderer());
			columnToAdjust.setPreferredWidth(oldWidths[columnToAdjust.getModelIndex()]);
			columnToAdjust.setWidth(oldWidths[columnToAdjust.getModelIndex()]);
			columnToAdjust.setResizable(true);
			columnToAdjust.setMinWidth(50);
			columnToAdjust.setMaxWidth(400);
		}
	}
	
	public int[] sortByColumn() 
	{
		int newRowCount = model.getRowCount()-summaryRow;
		ArrayList columnData = new ArrayList();

		for (int rowIndex = 0; rowIndex<newRowCount; ++rowIndex) 
		{
				columnData.add(createComparable(rowIndex));
		}

		Collections.sort(columnData);
		
		if ( getToggle() ) 
			Collections.reverse(columnData);

		int rows[] = new int[newRowCount + summaryRow];
		
		for (int rowIndex = 0; rowIndex<newRowCount; ++rowIndex) 
		{
				rows[rowIndex] = getOldRow(columnData.get(rowIndex));
		}
		rows[newRowCount] = newRowCount;
		
		return rows;
	}
	
	public abstract Comparable createComparable(int rowIndex);
	
	public abstract int getOldRow(Object object);
	
	public abstract boolean getToggle();
	
	int summaryRow = 1;
	int rowCount = 0;
	int columnCount = 0;
	int sortColumn = 0;
	MyThreatGirdPanel threatGirdPanel;
	NonEditableThreatMatrixTableModel model;

}
