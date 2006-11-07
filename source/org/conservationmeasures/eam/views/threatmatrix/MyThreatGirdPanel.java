/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class MyThreatGirdPanel extends JPanel
{
	public MyThreatGirdPanel(ThreatMatrixView viewToUse,
			NonEditableThreatMatrixTableModel modelToUse)
			throws Exception
	{
		super(new BorderLayout());
		view = viewToUse;
		add(createThreatGridPanel(modelToUse));
	}

	public JScrollPane createThreatGridPanel(NonEditableThreatMatrixTableModel model) throws Exception
	{
		NonEditableRowHeaderTableModel newRowHeaderData = new NonEditableRowHeaderTableModel(model);
		JTable rowHeaderTable = createRowHeaderTable(newRowHeaderData);

		threatTable = createThreatTable(model);

		JTableHeader columnHeader = threatTable.getTableHeader();
		columnHeader.addMouseListener(new ThreatColumnHeaderListener(this));

		JTableHeader rowHeader = rowHeaderTable.getTableHeader();
		rowHeader.addMouseListener(new TargetRowHeaderListener(this));
		
		JScrollPane scrollPane = createScrollPaneWithTableAndRowHeader(
				rowHeaderTable, threatTable, rowHeader);

		return scrollPane;
	}
	
	
	private JScrollPane createScrollPaneWithTableAndRowHeader(JTable rowHeaderTable,
			JTable table, JTableHeader rowHeader)
	{
		JScrollPane newScrollPane = new JScrollPane(table);
		newScrollPane.setRowHeaderView(rowHeaderTable);
		newScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowHeader);
		newScrollPane
				.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
		newScrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		newScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		return newScrollPane;
	}

	
	private ThreatMatrixTable createThreatTable(NonEditableThreatMatrixTableModel model)
	{
		NonEditableThreatMatrixTableModel threatData = model;
		
		ThreatMatrixTable table = new ThreatMatrixTable(threatData);

		table.setIntercellSpacing(new Dimension(0, 0));
		
		setColumnWidths(table,150);
		table.setRowHeight(60);

		ListSelectionModel selectionModel = table.getSelectionModel();
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		CellSelectionListener selectionListener = new CellSelectionListener(table,this);
		selectionModel.addListSelectionListener(selectionListener);
		
		CustomTableCellRenderer customTableCellRenderer = new CustomTableCellRenderer();
		table.setDefaultRenderer(Object.class, customTableCellRenderer);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		return table;
	}


	private void setColumnWidths(JTable table, int width)
	{
		Enumeration columns = table.getColumnModel().getColumns();
		while(columns.hasMoreElements())
		{
			TableColumn columnToAdjust = (TableColumn)columns.nextElement();
			columnToAdjust.setHeaderRenderer(new TargetRowHeaderRenderer());
			columnToAdjust.setPreferredWidth(width);
			columnToAdjust.setWidth(width);
			columnToAdjust.setResizable(true);
		}
	}



	private JTable createRowHeaderTable(NonEditableRowHeaderTableModel rowHeaderDataToUSe )
	{
		NonEditableRowHeaderTableModel rowHeaderData = rowHeaderDataToUSe;
		JTable rowHeaderTable = new JTable(rowHeaderData);

		rowHeaderTable.getTableHeader().setResizingAllowed(false);
		rowHeaderTable.getTableHeader().setReorderingAllowed(false);
		rowHeaderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		setColumnWidths(rowHeaderTable, 80);
		rowHeaderTable.setIntercellSpacing(new Dimension(0, 0));
		rowHeaderTable.setRowHeight(60);
		
		Dimension d = rowHeaderTable.getPreferredScrollableViewportSize();
		d.width = rowHeaderTable.getPreferredSize().width;
		rowHeaderTable.setPreferredScrollableViewportSize(d);
		
		setDefaultRowHeaderRenderer(rowHeaderTable);
	
		LookAndFeel.installColorsAndFont(rowHeaderTable, "TableHeader.background",
				"TableHeader.foreground", "TableHeader.font");

		return rowHeaderTable;
	}

	
	private void setDefaultRowHeaderRenderer(JTable rowHeaderTable)
	{
		rowHeaderTable.setDefaultRenderer(Object.class, new ThreatRowHeaderRenderer());
	}
	
	
	public ThreatRatingBundle getSelectedBundle()
	{
		return highlightedBundle;
	}

	
	public void selectBundle(ThreatRatingBundle bundle) throws Exception
	{
		highlightedBundle = bundle;
		refreshCell(bundle);
	}
	
	
	private void refreshCell(ThreatRatingBundle bundle) throws Exception
	{
		this.repaint();
	}

	
	public Project getProject() 
	{
		return view.getProject();
	}
	
	
	public ThreatRatingFramework getThreatRatingFramework() 
	{
		return view.getThreatRatingFramework();
	}
	
	
	public ThreatMatrixView getThreatMatrixView() 
	{
		return view;
	}
	
	
	public ThreatMatrixTable getThreatMatrixTable() 
	{
		return threatTable;
	}
	
	ThreatMatrixView view;
	ThreatRatingBundle highlightedBundle;
	ThreatMatrixTable threatTable;
}



