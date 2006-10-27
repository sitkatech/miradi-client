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

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LookAndFeel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class MyThreatGirdPanel
{
	public MyThreatGirdPanel(ThreatMatrixView viewToUse,
			ThreatMatrixTableModel modelToUse, Project projectToUse)
			throws Exception
	{
		model = modelToUse;
		project = projectToUse;
		framework = project.getThreatRatingFramework();
	}

	public JScrollPane createThreatGridPanel() throws Exception
	{
		JTable rowHeaderTable = createRowHeaderTable();

		JTable threatTable = createThreatTable(rowHeaderTable);

		JTableHeader columnHeader = threatTable.getTableHeader();
		columnHeader.addMouseListener(new HeaderListener(columnHeader));

		JTableHeader rowHeader = rowHeaderTable.getTableHeader();
		JScrollPane scrollPane = createScrollPaneWithTableAndRowHeader(
				rowHeaderTable, threatTable, rowHeader);

		initializeTableData((DefaultTableModel) threatTable.getModel());

		return scrollPane;
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

	private JTable createThreatTable(JTable rowHeaderTable)
	{
		DefaultTableModel threatData = new DefaultTableModel();
		threatData.setColumnIdentifiers(getColumnsTargetHeaders());
		JTable threatTable = new JTable(threatData);

		threatData.setNumRows(rowHeaderTable.getRowCount());

		threatTable.setRowHeight(rowHeightForThreatTable);

		CustomTableCellRenderer customTableCellRenderer = new CustomTableCellRenderer(model,framework);
		threatTable.setDefaultRenderer(Object.class, customTableCellRenderer);

		threatTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		return threatTable;
	}


	private JTable createRowHeaderTable()
	{
		DefaultTableModel rowHeaderData = createRowHeaderDataModel();
		JTable rowHeaderTable = new JTable(rowHeaderData);

		rowHeaderTable.setRowHeight(rowHeightForThreatTable);
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
			calculateRowHeight(label);
		}
		rowNames.add("Summary Threat Rating");
		return rowNames;
	}

	private void calculateRowHeight(String label)
	{
		rowHeightForThreatTable = 100;
	}
	
	private Object[] getColumnsTargetHeaders()
	{
		Vector columnsNames = new Vector();
		for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
		{
			String label = createLabel(model.getTargetName(targetIndex));
			columnsNames.add(label);
		}
		columnsNames.add("Summary Threat Rating");
		return columnsNames.toArray();
	}

	private void initializeTableData(DefaultTableModel data) throws Exception
	{
		initializeThreatTargetRatingData(data);

		initializeTargetRowSummaryData(data);

		initializeThreatColumnSummaryData(data);
		
		ValueOption result = framework.getOverallProjectRating();
		data.setValueAt(result, model.getThreatCount(), model.getTargetCount());
	}


	private void initializeThreatTargetRatingData(DefaultTableModel data) throws Exception
	{
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
			{
				if(model.isActiveCell(threatIndex, targetIndex))
				{
					setThreatRowData(data,threatIndex,targetIndex);
				}
			}
		}
	}

	private void setThreatRowData(DefaultTableModel data, int threatIndex,
			int targetIndex) throws Exception
	{
		ThreatRatingBundle bundle = getBundle(threatIndex, targetIndex);
		ValueOption valueOption = framework.getBundleValue(bundle);
		data.setValueAt(valueOption, threatIndex, targetIndex);
		valueOption.getColor();

	}

	private void initializeThreatColumnSummaryData(DefaultTableModel data)
	{
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			ValueOption result = framework.getThreatThreatRatingValue(model
					.getThreatId(threatIndex));
			data.setValueAt(result, threatIndex, model.getTargetCount());
		}
	}

	private void initializeTargetRowSummaryData(DefaultTableModel data)
	{
		for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
		{
			ValueOption result = framework.getTargetThreatRatingValue(model
					.getTargetId(targetIndex));
			data.setValueAt(result, model.getThreatCount(), targetIndex);
		}
	}

	private ThreatRatingBundle getBundle(int threatIndex, int targetIndex)
			throws Exception
	{
		ModelNodeId threatId = model.getThreatId(threatIndex);
		ModelNodeId targetId = model.getTargetId(targetIndex);
		ThreatRatingBundle bundle = framework.getBundle(threatId, targetId);
		return bundle;
	}

	private String createLabel(String text)
	{
		return text;
	}

	ThreatMatrixTableModel model;

	Project project;

	ThreatRatingFramework framework;
	
	int rowHeightForThreatTable = 100;

}

class CustomTableCellRenderer extends DefaultTableCellRenderer
{
	public CustomTableCellRenderer(ThreatMatrixTableModel modelToUse,
			ThreatRatingFramework frameworktoUse)
	{
		model = modelToUse;
		framework = frameworktoUse;
		setHorizontalAlignment(CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component cell = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		ValueOption valueOption = (ValueOption)table.getValueAt(row, column);
		if (valueOption==null) 
		{ 
			cell.setBackground(Color.WHITE);  
		} else 
		{
			cell.setBackground(valueOption.getColor());
			cell.setFont(new Font(null,Font.BOLD,12));
		}

		cell.setForeground(Color.BLACK); 
		return cell;
	}

	ThreatRatingFramework framework;
	ThreatMatrixTableModel model;
}


class HeaderListener extends MouseAdapter
{
	HeaderListener(JTableHeader headerToUse)
	{
		header = headerToUse;
	}

	public void mousePressed(MouseEvent e)
	{
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
		// int col = header.columnAtPoint(e.getPoint());
		header.repaint();
	}
	
	JTableHeader header;

}
