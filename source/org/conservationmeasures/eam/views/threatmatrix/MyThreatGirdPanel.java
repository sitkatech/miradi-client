/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class MyThreatGirdPanel extends JPanel
{
	public MyThreatGirdPanel(ThreatMatrixView viewToUse,
			NonEditableThreatMatrixTableModel modelToUse, Project projectToUse)
			throws Exception
	{
		model = modelToUse;
		project = projectToUse;
		framework = project.getThreatRatingFramework();
		view = viewToUse;
		add(createThreatGridPanel());
	}

	public JScrollPane createThreatGridPanel() throws Exception
	{
		JTable rowHeaderTable = createRowHeaderTable();

		globalTthreatTable = createThreatTable(rowHeaderTable.getRowCount());

		setRowHeaderHeight(rowHeaderTable, globalTthreatTable);
		
		JTableHeader columnHeader = globalTthreatTable.getTableHeader();
		columnHeader.addMouseListener(new HeaderListener(columnHeader));

		JTableHeader rowHeader = rowHeaderTable.getTableHeader();
		JScrollPane scrollPane = createScrollPaneWithTableAndRowHeader(
				rowHeaderTable, globalTthreatTable, rowHeader);

		initializeTableData((DefaultTableModel) globalTthreatTable.getModel());

		return scrollPane;
	}

	private void setRowHeaderHeight(JTable rowHeaderTable, JTable threatTable)
	{
		int rowHeightForThreatTable = calculateRowHeight(rowHeaderTable.getModel());
		rowHeaderTable.setRowHeight(rowHeightForThreatTable);
		threatTable.setRowHeight(rowHeightForThreatTable);
	}

	private JScrollPane createScrollPaneWithTableAndRowHeader(JTable rowHeaderTable,
			JTable threatTable, JTableHeader rowHeader)
	{
		JScrollPane scrollPane = new JScrollPane(threatTable);
		scrollPane.setRowHeaderView(rowHeaderTable);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowHeader);
		scrollPane
				.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		return scrollPane;
	}

	private JTable createThreatTable(int rowCount)
	{
		DefaultTableModel threatData = getNonEdditableTableModel();
		threatData.setColumnIdentifiers(getColumnsTargetHeaders());
		JTable threatTable = new JTable(threatData);

		ListSelectionModel selectionModel = threatTable.getSelectionModel();
		threatTable.setRowSelectionAllowed(false);
		threatTable.setColumnSelectionAllowed(true);
		threatTable.setCellSelectionEnabled(true);
		CellSelectionListener selectionListener = new CellSelectionListener(threatTable,this);
		selectionModel.addListSelectionListener(selectionListener);
		
		threatData.setNumRows(rowCount);
		CustomTableCellRenderer customTableCellRenderer = new CustomTableCellRenderer();
		threatTable.setDefaultRenderer(Object.class, customTableCellRenderer);

		threatTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		return threatTable;
	}


	private DefaultTableModel getNonEdditableTableModel()
	{
		DefaultTableModel threatData = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		return model;
	}


	private JTable createRowHeaderTable()
	{
		DefaultTableModel rowHeaderData = createRowHeaderDataModel();
		JTable rowHeaderTable = new JTable(rowHeaderData);

		rowHeaderTable.setIntercellSpacing(new Dimension(0, 0));
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
		rowHeaderTable.setDefaultRenderer(Object.class, new RowHeaderRenderer());
	}


	private DefaultTableModel createRowHeaderDataModel()
	{
		DefaultTableModel rowHeaderData = new DefaultTableModel(0, 1);
		Vector rowNames = getRowThreatHeaders();

		for(int k = 0; k < rowNames.size(); k++)
		{
			Object[] row = new Object[] { rowNames.get(k) };
			rowHeaderData.addRow(row);
		}
		return rowHeaderData;
	}

	private Vector getRowThreatHeaders()
	{
		Vector rowNames = new Vector();
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			String label = createLabel(model.getThreatName(threatIndex));
			rowNames.add(label);
		}
		rowNames.add(EAM.text("Summary Threat Rating"));
		return rowNames;
	}
	//TODO: must add logic to calc row hieght based on lenght of user threat header names
	private int calculateRowHeight(TableModel rowHeaderData)
	{
		return 100;
	}
	
	private Vector getColumnsTargetHeaders()
	{
		Vector columnsNames = new Vector();
		for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
		{
			String label = createLabel(model.getTargetName(targetIndex));
			columnsNames.add(label);
		}
		columnsNames.add(EAM.text("Summary Target Rating"));
		return columnsNames;
	}

	private void initializeTableData(DefaultTableModel data) throws Exception
	{
		initializeThreatTargetRatingData(data);

		initializeTargetSummaryData(data);

		initializeThreatSummaryData(data);
		
		initializeOverallProjectRating(data);
	}


	private void initializeOverallProjectRating(DefaultTableModel data)
	{
		ValueOption result = framework.getOverallProjectRating();
		data.setValueAt(result, model.getThreatCount(), model.getTargetCount());
	}


	private void initializeThreatTargetRatingData(TableModel data) throws Exception
	{
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
			{
				if(model.isActiveCell(threatIndex, targetIndex))
				{
					//ValueOption valueOption = getBundleValue(threatIndex, targetIndex);
					ThreatRatingBundle bundle = getBundle(threatIndex, targetIndex);
					data.setValueAt(bundle, threatIndex, targetIndex);
				}
				else 
				{
					ValueOption valueOption =  new ValueOption( new BaseId(-1), "", -1 , Color.WHITE);
					data.setValueAt(valueOption, threatIndex, targetIndex);
				}
			}
		}
	}

	private ValueOption getBundleValue(int threatIndex, int targetIndex) throws Exception
	{
		ThreatRatingBundle bundle = getBundle(threatIndex, targetIndex);
		ValueOption valueOption = framework.getBundleValue(bundle);
		return valueOption;
	}

	
	private void initializeThreatSummaryData(TableModel data)
	{
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			ValueOption result = framework.getThreatThreatRatingValue(model
					.getThreatId(threatIndex));
			data.setValueAt(result, threatIndex, model.getTargetCount());
		}
	}

	private void initializeTargetSummaryData(TableModel data)
	{
		for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
		{
			ValueOption result = framework.getTargetThreatRatingValue(model
					.getTargetId(targetIndex));
			data.setValueAt(result, model.getThreatCount(), targetIndex);
		}
	}

	public ThreatRatingBundle getBundle(int threatIndex, int targetIndex)
			throws Exception
	{
		ModelNodeId threatId = model.getThreatId(threatIndex);
		ModelNodeId targetId = model.getTargetId(targetIndex);
		ThreatRatingBundle bundle = framework.getBundle(threatId, targetId);
		return bundle;
	}
	
	// TODO: should be removed once we are satisfied with row and column header display
	private String createLabel(String text)
	{
		return text;
	}
	
	public void bundleWasClicked(ThreatRatingBundle bundle) throws Exception
	{
		view.selectBundle(bundle);
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
	
	public void refreshCell(ThreatRatingBundle bundle) throws Exception
	{

//TODO:	loop over model target and threat ids for match on NodId to get column and row
//		bundle.getTargetId();
//		model.getTargetId(0); 
//		ValueOption valueOption = getBundleValue(threatIndex, targetIndex);
//		setCellValue(data,valueOption,threatIndex,targetIndex);
		initializeThreatSummaryData(globalTthreatTable.getModel());
		initializeTargetSummaryData(globalTthreatTable.getModel());
		initializeThreatTargetRatingData(globalTthreatTable.getModel());
		globalTthreatTable.revalidate();
		globalTthreatTable.repaint();
	}

	NonEditableThreatMatrixTableModel model;
	ThreatMatrixView view;
	Project project;
	ThreatRatingFramework framework;
	ThreatRatingBundle highlightedBundle;
	JTable globalTthreatTable;
}

class CellSelectionListener implements ListSelectionListener
{
	public CellSelectionListener(JTable threatTableInUse, MyThreatGirdPanel threatGirdPanelInUse) {
		threatTable = threatTableInUse;
		threatGirdPanel = threatGirdPanelInUse;
	}
	public void valueChanged(ListSelectionEvent e)
	{
		if (threatTable.getSelectedRow() < 0) 
			return;
		
		int row = threatTable.getSelectedRow();
		int column = threatTable.getSelectedColumn();
		
		unselectToForceFutureNotifications(row, column);
		
		if ( isCellOutSideRealDataBounds(row, column)) 
			return;
		
		notifyComponents(row, column);
	}

	
	private void unselectToForceFutureNotifications(int row, int column)
	{
		threatTable.changeSelection(row, column, true,false);
	}

	
	private boolean isCellOutSideRealDataBounds(int row, int column)
	{
		if (isOutsideMatrixBounds(row, column)) 
			return true;
		
		if (!isValidThreatTargetPair(row, column))
			return true;
		
		return false;
	}

	
	private boolean isValidThreatTargetPair(int row, int column)
	{
		ValueOption valueOption = (ValueOption)threatTable.getModel().getValueAt(row, column);
		return (valueOption.getNumericValue()!=-1);
	}

	
	private boolean isOutsideMatrixBounds(int row, int column)
	{
		return row==threatTable.getRowCount()-1 || 
			 column ==threatTable.getColumnCount()-1;
	}

	private void notifyComponents(int row, int column)
	{
		try
		{
			ThreatRatingBundle threatRatingBundle = threatGirdPanel.getBundle(row, column);
			threatGirdPanel.view.selectBundle(threatRatingBundle);
		}
		// TODO: must add errDialog call....need to see how to call when on the swing event thread
		catch(Exception ex)
		{
			EAM.logException(ex);
		}
	}
	
	JTable threatTable;
	MyThreatGirdPanel threatGirdPanel;
}


class CustomTableCellRenderer extends DefaultTableCellRenderer
{
	public CustomTableCellRenderer()
	{
		setHorizontalAlignment(CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component cell = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
		
		cell.setBackground( ((ValueOption)value).getColor() );
		cell.setFont(new Font(null,Font.BOLD,12));
		cell.setForeground(Color.BLACK); 
		
		return cell;
	}

}


class HeaderListener extends MouseAdapter
{
	HeaderListener(JTableHeader headerToUse)
	{
		header = headerToUse;
	}

	public void mousePressed(MouseEvent e)
	{
		//TODO: add sort logic here
		// int col = header.columnAtPoint(e.getPoint());
		// int sortCol = header.getTable().convertColumnIndexToModel(col);
		header.repaint();

		if(header.getTable().isEditing())
		{
			header.getTable().getCellEditor().stopCellEditing();
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		header.repaint();
	}
	
	JTableHeader header;

}
