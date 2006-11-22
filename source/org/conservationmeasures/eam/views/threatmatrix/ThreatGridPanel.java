/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class ThreatGridPanel extends JPanel
{
	public ThreatGridPanel(ThreatMatrixView viewToUse,
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
		rowHeaderTable = rowTable;
		
		ThreatMatrixTable table = createThreatTable(model);
		threatTable = table;
		
		JTableHeader columnHeader = table.getTableHeader();
		targetColumnSortListener = new BundleColumnSortHandler(this);
		columnHeader.addMouseListener(targetColumnSortListener);
		columnHeader.addMouseMotionListener(targetColumnSortListener);

		JTableHeader rowHeader = rowTable.getTableHeader();
		threatColumnSortListener = new ThreatNameColumnHandler(this);
		rowHeader.addMouseListener(threatColumnSortListener);
		
		JScrollPane scrollPane = createScrollPaneWithTableAndRowHeader(
				rowTable, table);
		
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

			boolean sortOrder = currentSortDirection.equals(ViewData.SORT_ASCENDING);

			
			//TODO: the reference to SORT_TARGETS should be removed after at some point as project data
			//TODO: was writen using this id instead of SORT_THREATS. Most projects will there for self correct 
			//TODO: once they are opend and resaved. Or a conversion can be wrtten
			if(currentSortBy.equals(ViewData.SORT_THREATS) || currentSortBy.equals(ViewData.SORT_TARGETS))
			{
				threatColumnSortListener.setToggle(sortOrder);
				threatColumnSortListener.sort(0);
			}
			else
			{
				int columnToSort = threatTable.getSummaryColumn();
				if (!currentSortBy.equals(ViewData.SORT_SUMMARY)) 
				{
					ModelNodeId nodeId = new ModelNodeId(new Integer(currentSortBy).intValue());
					columnToSort= ((NonEditableThreatMatrixTableModel)threatTable.getModel()).findTargetIndexById(nodeId);
				}
				targetColumnSortListener.setToggle(sortOrder);
				targetColumnSortListener.sort(columnToSort);
			}
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
		
		setColumnWidths(table);
		table.setRowHeight(ROW_HEIGHT);

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


	private void setColumnWidths(JTable table)
	{
		Graphics2D g2 = (Graphics2D)staticGraphics;
		Enumeration columns = table.getColumnModel().getColumns();
		while(columns.hasMoreElements())
		{
			TableColumn columnToAdjust = (TableColumn)columns.nextElement();
			String headerText = (String)columnToAdjust.getHeaderValue();
			int firstSpace = headerText.indexOf(' ');
			if(firstSpace >= 0)
				headerText = headerText.substring(0, firstSpace);
			TextLayout textLayout = new TextLayout(headerText, g2.getFont(), g2.getFontRenderContext());
			int textWidth = textLayout.getBounds().getBounds().width;
			int width = Math.max(DEFAULT_COLUMN_WIDTH, textWidth + 20);
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
		
		rowHeaderTableToUse.getColumnModel().getColumn(0).setPreferredWidth(LEFTMOST_COLUMN_WIDTH);
		rowHeaderTableToUse.setIntercellSpacing(new Dimension(0, 0));
		rowHeaderTableToUse.setRowHeight(ROW_HEIGHT);

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
	
	
	private final static Graphics staticGraphics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics();
	private ThreatMatrixView view;
	private ThreatRatingBundle highlightedBundle;
	private ThreatMatrixTable threatTable;
	private ThreatNameColumnHandler threatColumnSortListener;
	private BundleColumnSortHandler targetColumnSortListener;
	private JTable rowHeaderTable;
	
	private final static int ABOUT_ONE_LINE = 20;
	private final static int ROW_HEIGHT = 2 * ABOUT_ONE_LINE;

	private final static int ABOUT_ONE_INCH = 72;
	private final static int LEFTMOST_COLUMN_WIDTH = 2 * ABOUT_ONE_INCH;
	private final static int DEFAULT_COLUMN_WIDTH = ABOUT_ONE_INCH;
	
}



