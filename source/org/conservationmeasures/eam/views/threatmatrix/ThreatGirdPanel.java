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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class ThreatGirdPanel extends JPanel
{
	public ThreatGirdPanel(ThreatMatrixView viewToUse,
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
		JTable rowTable = createRowHeaderTable(newRowHeaderData);

		ThreatMatrixTable table = createThreatTable(model);

		JTableHeader columnHeader = table.getTableHeader();
		threatColumnSortListener = new ThreatColumnSortListener(this);
		columnHeader.addMouseListener(threatColumnSortListener);

		JTableHeader rowHeader = rowTable.getTableHeader();
		targetColumnSortListener = new TargetColumnSortListener(this);
		rowHeader.addMouseListener(targetColumnSortListener);
		
		JScrollPane scrollPane = createScrollPaneWithTableAndRowHeader(
				rowTable, table);

		threatTable = table;
		rowHeaderTable = rowTable;
		
		return scrollPane;
	}
	
	public void establishPriorSortState() throws Exception  
	{
		String currentSortBy = getProject().getViewData(
				getProject().getCurrentView()).getData(
				ViewData.TAG_CURRENT_SORT_BY);
		
		boolean hastPriorSortBy = currentSortBy.length() != 0;
		if(hastPriorSortBy)
		{
			String currentSortDirection = getProject().getViewData(
					getProject().getCurrentView()).getData(
					ViewData.TAG_CURRENT_SORT_DIRECTION);

			if(currentSortBy.equals(ViewData.SORT_TARGETS))
				targetColumnSortListener.sort(currentSortBy,
						currentSortDirection);
			else
				targetColumnSortListener.sort(currentSortBy,
						currentSortDirection);
		}
	}
	
	private JScrollPane createScrollPaneWithTableAndRowHeader(JTable rowHeaderTableToUse,
			JTable table)
	{
		JTableHeader rowHeader = rowHeaderTableToUse.getTableHeader();
		
		JScrollPane newScrollPane = new JScrollPane(table);
		newScrollPane.setRowHeaderView(rowHeaderTableToUse);
		newScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowHeader);
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
		
		setColumnWidths(table,ABOUT_TWO_INCHES);
		table.setRowHeight(ABOUT_ONE_INCH);

		ListSelectionModel selectionModel = table.getSelectionModel();
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		CellSelectionListener selectionListener = new CellSelectionListener(table,this);
		selectionModel.addListSelectionListener(selectionListener);
		
		CustomTableCellRenderer customTableCellRenderer = new CustomTableCellRenderer(this);
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
			columnToAdjust.setHeaderRenderer(new TableHeaderRenderer());
			columnToAdjust.setPreferredWidth(width);
			columnToAdjust.setWidth(width);
			columnToAdjust.setResizable(true);
		}
	}



	private JTable createRowHeaderTable(NonEditableRowHeaderTableModel rowHeaderDataToUSe )
	{
		NonEditableRowHeaderTableModel rowHeaderData = rowHeaderDataToUSe;
		JTable rowHeaderTableToUse = new ThreatMatrixRowHeaderTable(rowHeaderData);

		rowHeaderTableToUse.getTableHeader().setResizingAllowed(true);
		rowHeaderTableToUse.getTableHeader().setReorderingAllowed(false);
		rowHeaderTableToUse.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		setColumnWidths(rowHeaderTableToUse, ABOUT_ONE_AND_HALF_INCH);
		rowHeaderTableToUse.setIntercellSpacing(new Dimension(0, 0));
		rowHeaderTableToUse.setRowHeight(ABOUT_ONE_INCH);

		setDefaultRowHeaderRenderer(rowHeaderTableToUse);
	
		LookAndFeel.installColorsAndFont(rowHeaderTableToUse, "TableHeader.background",
				"TableHeader.foreground", "TableHeader.font");

		return rowHeaderTableToUse;
	}

	
	private void setDefaultRowHeaderRenderer(JTable rowHeaderTable)
	{
		rowHeaderTable.setDefaultRenderer(Object.class, new TableHeaderRenderer());
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
	
	
	public JTable getRowHeaderTable() 
	{
		return rowHeaderTable;
	}
	
	private ThreatMatrixView view;
	private ThreatRatingBundle highlightedBundle;
	private ThreatMatrixTable threatTable;
	private ThreatColumnSortListener threatColumnSortListener;
	private TargetColumnSortListener targetColumnSortListener;
	private JTable rowHeaderTable;
	
	int ABOUT_ONE_INCH = 60;
	int ABOUT_TWO_INCHES = 150;
	int ABOUT_ONE_AND_HALF_INCH = 80;
	
}



