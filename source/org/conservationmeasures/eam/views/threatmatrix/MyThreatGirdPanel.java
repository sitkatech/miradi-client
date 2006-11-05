/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
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
		NonEditableRowHeaderTableModel newRowHeaderData = new NonEditableRowHeaderTableModel(model);
		JTable rowHeaderTable = createRowHeaderTable(newRowHeaderData);

		globalTthreatTable = createThreatTable();

		JTableHeader columnHeader = globalTthreatTable.getTableHeader();
		columnHeader.addMouseListener(new ThreatColumnHeaderListener(this));

		JTableHeader rowHeader = rowHeaderTable.getTableHeader();
		rowHeader.addMouseListener(new TargetRowHeaderListener(this));
		
		JScrollPane scrollPane = createScrollPaneWithTableAndRowHeader(
				rowHeaderTable, globalTthreatTable, rowHeader);

		return scrollPane;
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

	private JTable createThreatTable()
	{
		NonEditableThreatMatrixTableModel threatData = model;
		
		ThreatMatrixTable threatTable = new ThreatMatrixTable(threatData);

		threatTable.setIntercellSpacing(new Dimension(0, 0));
		
		setThreatTableColumnWidths(threatTable);
		threatTable.setRowHeight(60);

		ListSelectionModel selectionModel = threatTable.getSelectionModel();
		threatTable.setRowSelectionAllowed(false);
		threatTable.setColumnSelectionAllowed(true);
		threatTable.setCellSelectionEnabled(true);
		CellSelectionListener selectionListener = new CellSelectionListener(threatTable,this);
		selectionModel.addListSelectionListener(selectionListener);
		
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
			columnToAdjust.setHeaderRenderer(new TargetRowHeaderRenderer());
			columnToAdjust.setPreferredWidth(150);
			columnToAdjust.setWidth(150);
			columnToAdjust.setResizable(true);
			columnToAdjust.setMinWidth(50);
			columnToAdjust.setMaxWidth(400);
		}
	}



	public JTable createRowHeaderTable(NonEditableRowHeaderTableModel rowHeaderDataToUSe )
	{
		rowHeaderData = rowHeaderDataToUSe;
		JTable rowHeaderTable = new JTable(rowHeaderData);

		rowHeaderTable.getTableHeader().setResizingAllowed(true);
		rowHeaderTable.getTableHeader().setReorderingAllowed(false);
		rowHeaderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
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
		rowHeaderTable.getTableHeader().setDefaultRenderer( new TargetRowHeaderRenderer());
		rowHeaderTable.setDefaultRenderer(Object.class, new ThreatRowHeaderRenderer());
	}


	public Vector getColumnTargetHeaders()
	{
		Vector columnsNames = new Vector();
		for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
		{
			ConceptualModelNode targetNode = createNodeTargetLabel(targetIndex);
			columnsNames.add(targetNode);
		}
		columnsNames.add(EAM.text("Summary Target Rating"));
		return columnsNames;
	}


	private ConceptualModelNode createNodeTargetLabel(int targetIndex)
	{	
		return model.getTargetNode(targetIndex);
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
		this.repaint();
	}

	NonEditableThreatMatrixTableModel model;
	ThreatMatrixView view;
	Project project;
	ThreatRatingFramework framework;
	ThreatRatingBundle highlightedBundle;
	JTable globalTthreatTable;
	NonEditableRowHeaderTableModel rowHeaderData;
}

class CellSelectionListener implements ListSelectionListener
{
	public CellSelectionListener(ThreatMatrixTable threatTableInUse, MyThreatGirdPanel threatGirdPanelInUse) {
		threatTable = threatTableInUse;
		threatGirdPanel = threatGirdPanelInUse;
	}

	public void valueChanged(ListSelectionEvent e)
	{
		if (threatTable.getSelectedRow() >= 0) 
		{
			int row = threatTable.getSelectedRow();
			int column = threatTable.getSelectedModelColumn();

			if(((NonEditableThreatMatrixTableModel) threatTable.getModel())
					.isBundleTableCellABundle(row, column))
				notifyComponents(row, column);
			else
				notifyComponentsClearSelection();

			unselectToForceFutureNotifications(row, threatTable.getSelectedColumn());
		}
	}

	
	private void unselectToForceFutureNotifications(int row, int column)
	{
		threatTable.changeSelection(row, column, true,false);
	}
	
	private void notifyComponentsClearSelection()
	{
		try
		{
			threatGirdPanel.view.selectBundle(null);
		}
		catch(Exception ex)
		{
			EAM.logException(ex);
		}
	}

	private void notifyComponents(int row, int column)
	{
		try
		{
			NonEditableThreatMatrixTableModel model = (NonEditableThreatMatrixTableModel)threatTable.getModel();
			ThreatRatingBundle threatRatingBundle = model.getBundle(row, column);
			threatGirdPanel.view.selectBundle(threatRatingBundle);
		}
		// TODO: must add errDialog call....need to see how to call when on the swing event thread
		catch(Exception ex)
		{
			EAM.logException(ex);
		}
	}
	
	
	
	ThreatMatrixTable threatTable;
	MyThreatGirdPanel threatGirdPanel;
}




