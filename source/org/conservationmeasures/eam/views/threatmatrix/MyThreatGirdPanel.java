/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
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
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
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
		super(new BorderLayout());
		model = modelToUse;
		project = projectToUse;
		framework = project.getThreatRatingFramework();
		view = viewToUse;
		add(createThreatGridPanel());
	}

	public JScrollPane createThreatGridPanel() throws Exception
	{
		JTable rowHeaderTable = createRowHeaderTable(createRowHeaderDataModel());

		globalTthreatTable = createThreatTable(rowHeaderTable.getRowCount());

		setRowHeight(globalTthreatTable);
		
		JTableHeader columnHeader = globalTthreatTable.getTableHeader();
		columnHeader.addMouseListener(new ThreatColumnHeaderListener(this));

		JTableHeader rowHeader = rowHeaderTable.getTableHeader();
		rowHeader.addMouseListener(new TargetRowHeaderListener(this));
		
		
		scrollPane = createScrollPaneWithTableAndRowHeader(
				rowHeaderTable, globalTthreatTable, rowHeader);

		initializeTableData((DefaultTableModel) globalTthreatTable.getModel());

		return scrollPane;
	}

	private void setRowHeight(JTable table)
	{
		int rowHeightForThreatTable = calculateRowHeight(table.getModel());
		table.setRowHeight(rowHeightForThreatTable);
	}

	private JScrollPane createScrollPaneWithTableAndRowHeader(JTable rowHeaderTable,
			JTable threatTable, JTableHeader rowHeader)
	{
		JScrollPane newScrollPane = new JScrollPane(threatTable);
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

	private JTable createThreatTable(int rowCount)
	{
		DefaultTableModel threatData = model;
		columnHeaderData = getColumnTargetHeaders();
		threatData.setColumnIdentifiers(columnHeaderData);
		
		JTable threatTable = new JTable(threatData);
		threatTable.getTableHeader().setReorderingAllowed(false);
		
		setThreatTableColumnWidths(threatTable);

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


	public void setThreatTableColumnWidths(JTable threatTable)
	{
		Enumeration columns = threatTable.getColumnModel().getColumns();
		while(columns.hasMoreElements())
		{
			TableColumn columnToAdjust = (TableColumn)columns.nextElement();
			columnToAdjust.setPreferredWidth(150);
			columnToAdjust.setWidth(150);
		}
	}



	public JTable createRowHeaderTable(DefaultTableModel rowHeaderDataToUSe )
	{
		rowHeaderData = rowHeaderDataToUSe;
		JTable rowHeaderTable = new JTable(rowHeaderData);
		rowHeaderTable.getTableHeader().setReorderingAllowed(false);
		rowHeaderTable.setIntercellSpacing(new Dimension(0, 0));
		Dimension d = rowHeaderTable.getPreferredScrollableViewportSize();
		d.width = rowHeaderTable.getPreferredSize().width;
		rowHeaderTable.setPreferredScrollableViewportSize(d);
		
		setDefaultRowHeaderRenderer(rowHeaderTable);
		rowHeaderTable.addMouseListener(new RowHeaderListener(rowHeaderTable));
		
		setRowHeight(rowHeaderTable);
		
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
		DefaultTableModel newRowHeaderData = new NonEditableRowHeaderTableModel(0,1);
		Vector rowNames = getRowThreatHeaders();

		for(int k = 0; k < rowNames.size(); k++)
		{
			Object[] row = new Object[] { rowNames.get(k) };
			newRowHeaderData.addRow(row);
		}
		
		newRowHeaderData.setColumnIdentifiers(createRowColumnHeaderName());
		
		return newRowHeaderData;
	}


	private Vector createRowColumnHeaderName()
	{
		Vector RowColumnHeaderName = new Vector();
		RowColumnHeaderName.add(EAM.text("THREATS"));
		return RowColumnHeaderName;
	}

	public Vector getRowThreatHeaders()
	{
		Vector rowNames = new Vector();
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			ComparableNode comparableThreatNode = createNodeThreatLabel(threatIndex);
			rowNames.add(comparableThreatNode);
		}
		rowNames.add(new ComparableNode(EAM.text("Summary Threat Rating"), model.getThreatCount()));
		return rowNames;
	}
	
	
	
	private ComparableNode createNodeThreatLabel(int index)
	{	
		ConceptualModelNode[] conceptualModelNode = model.getDirectThreats();
		return new ComparableNode(index, conceptualModelNode[index]);
	}
	

	public Vector getColumnTargetHeaders()
	{
		Vector columnsNames = new Vector();
		for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
		{
			ComparableNode comparableThreatNode = createNodeTargetLabel(targetIndex);
			columnsNames.add(comparableThreatNode);
		}
		columnsNames.add(new ComparableNode(EAM.text("Summary Target Rating"),model.getTargetCount()));
		return columnsNames;
	}


	private ComparableNode createNodeTargetLabel(int index)
	{	
		ConceptualModelNode[] conceptualModelNode = model.getTargets();
		return new ComparableNode(index, conceptualModelNode[index]);
	}
	
	
	//TODO: must add logic to calc row hieght based on lenght of user threat header names
	private int calculateRowHeight(TableModel rowHeaderDataToUse)
	{
		return 60;
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
				Object value = null;
				if(model.isActiveCell(threatIndex, targetIndex))
				{
					value = model.getBundle(threatIndex, targetIndex);
				}
				else 
				{
					value =  new ValueOption( new BaseId(-1), "", -1 , Color.WHITE);
				}
				data.setValueAt(value, threatIndex, targetIndex);
			}
		}
	}

	
	private void initializeThreatSummaryData(TableModel data)
	{
		for(int rowIndex = 0; rowIndex < model.getThreatCount(); ++rowIndex)
		{
			ComparableNode comparableNode = (ComparableNode) rowHeaderData.getValueAt(rowIndex, 0);
			int threatIndex = comparableNode.getIndex();
			if (threatIndex!=-1) 
			{
				ValueOption result = framework.getThreatThreatRatingValue(model.getThreatId(threatIndex));
				data.setValueAt(result, rowIndex, model.getTargetCount());
			}
		}
	}

	private void initializeTargetSummaryData(TableModel data)
	{
		for(int columnIndex = 0; columnIndex < model.getTargetCount(); ++columnIndex)
		{
			ComparableNode comparableNode = (ComparableNode) columnHeaderData.get(columnIndex);
			int taretIndex = comparableNode.getIndex();
			if(taretIndex != -1)
			{
				ValueOption result = framework.getTargetThreatRatingValue(model.getTargetId(taretIndex));
				data.setValueAt(result, model.getThreatCount(),columnIndex);
			}
		}
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
		((NonEditableThreatMatrixTableModel)globalTthreatTable.getModel()).setBundle(bundle);
		initializeThreatSummaryData(globalTthreatTable.getModel());
		initializeTargetSummaryData(globalTthreatTable.getModel());

		globalTthreatTable.revalidate();
		globalTthreatTable.repaint();
	}

	NonEditableThreatMatrixTableModel model;
	ThreatMatrixView view;
	Project project;
	ThreatRatingFramework framework;
	ThreatRatingBundle highlightedBundle;
	JTable globalTthreatTable;
	JScrollPane scrollPane;
	DefaultTableModel rowHeaderData;
	Vector columnHeaderData;
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
		
		if (((NonEditableThreatMatrixTableModel)threatTable.getModel()).isBundle(row, column) )
			notifyComponents(row, column);
	}

	
	private void unselectToForceFutureNotifications(int row, int column)
	{
		threatTable.changeSelection(row, column, true,false);
	}

	
	private void notifyComponents(int row, int column)
	{
		try
		{
			NonEditableThreatMatrixTableModel model = (NonEditableThreatMatrixTableModel)threatTable.getModel();
			ThreatRatingBundle threatRatingBundle = (ThreatRatingBundle)model.realDataGetValueAt(row, column);
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



class RowHeaderListener extends MouseAdapter
{
	RowHeaderListener(JTable threatTableIn)
	{
		threatTable = threatTableIn;
	}

	public void mousePressed(MouseEvent e)
	{
		sortColumn = threatTable.columnAtPoint(e.getPoint());
	}

	public void mouseReleased(MouseEvent e)
	{
		if (sortColumn != threatTable.getColumnCount()) 
		{
		//	((NonEditableThreatMatrixTableModel)threatTable.getModel()).sort("ROWHEADER", "ASSENDING" ,sortColumn);
			threatTable.revalidate();
			threatTable.repaint();
		}
	}
	
	int sortColumn = 0;
	JTable threatTable;

}