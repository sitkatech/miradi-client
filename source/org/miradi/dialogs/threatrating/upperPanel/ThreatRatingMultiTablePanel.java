/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.threatrating.upperPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.base.ColumnMarginResizeListenerValidator;
import org.miradi.dialogs.base.MultiTablePanel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefList;
import org.miradi.utils.AbstractTableExporter;
import org.miradi.utils.MainThreatTableModelExporter;
import org.miradi.views.umbrella.ObjectPicker;

public class ThreatRatingMultiTablePanel extends MultiTablePanel implements ListSelectionListener 
{
	public ThreatRatingMultiTablePanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse);
		
		createTables();
		multiTableExporter = createExporter();
		
		addTableToGridBag();
		addTablesToSelectionController();
		synchTableColumns();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		threatNameTable.dispose();
		targetThreatLinkTable.dispose();
		threatSummaryColumnTable.dispose();
		targetSummaryRowTable.dispose();
		overallProjectSummaryCellTable.dispose();	
	}
	
	private void addTablesToSelectionController()
	{
		selectionController.addTable(threatNameTable);
		selectionController.addTable(targetThreatLinkTable);
		selectionController.addTable(threatSummaryColumnTable);
	}
	
	private void createTables() throws Exception
	{
		threatNameTableModel = new ThreatNameColumnTableModel(getProject());
		threatNameTable = new ThreatNameColumnTable(getMainWindow(), threatNameTableModel);
		
		listenForColumnWidthChanges(threatNameTable);

		targetThreatLinkTableModel = new TargetThreatLinkTableModel(getProject());
		targetThreatLinkTable = new TargetThreatLinkTable(getMainWindow(), targetThreatLinkTableModel);

		threatSummaryColumnTableModel = new ThreatSummaryColumnTableModel(getProject());
		threatSummaryColumnTable = new ThreatSummaryColumnTable(getMainWindow(), threatSummaryColumnTableModel);
		
		targetSummaryRowTableModel = new TargetSummaryRowTableModel(getProject());
		targetSummaryRowTable = new TargetSummaryRowTable(getMainWindow(), targetSummaryRowTableModel);
		targetSummaryRowTable.resizeTable(1);
		
		overallProjectSummaryCellTableModel = new OverallProjectSummaryCellTableModel(getProject());
		overallProjectSummaryCellTable = new OverallProjectSummaryCellTable(getMainWindow(), overallProjectSummaryCellTableModel);
		overallProjectSummaryCellTable.resizeTable(1);

	}

	private ThreatRatingMultiTableAsOneExporter createExporter()
	{
		ThreatRatingMultiTableAsOneExporter exporter = new ThreatRatingMultiTableAsOneExporter();
		exporter.addAsTopRowTable(new MainThreatTableModelExporter(threatNameTableModel));
		exporter.addAsTopRowTable(new MainThreatTableModelExporter(targetThreatLinkTableModel));
		exporter.addAsTopRowTable(new MainThreatTableModelExporter(threatSummaryColumnTableModel));
		exporter.setTargetSummaryRowTable(new MainThreatTableModelExporter(targetSummaryRowTableModel));
		exporter.setOverallSummaryRowTable(new MainThreatTableModelExporter(overallProjectSummaryCellTableModel));
		return exporter;
	}
	
	public AbstractTableExporter getTableForExporting()
	{
		return multiTableExporter;
	}	

	private void listenForColumnWidthChanges(JTable table)
	{
		table.getColumnModel().addColumnModelListener(new ColumnMarginResizeListenerValidator(this));
	}
	
	private void addTableToGridBag()
	{		
		JPanel mainPanel = new JPanel(new GridBagLayout());
		
		JScrollPane threatTableScroller = new AdjustableScrollPaneWithInvisibleVerticalScrollBar(threatNameTable);
		threatTableScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		addToVerticalController(threatTableScroller);
		addRowHeightControlledTable(threatNameTable);
		addRowSortControlledTable(threatNameTable);
		
		JScrollPane targetThreatLinkTableScroller = new ScrollPaneWithInvisibleVerticalScrollBar(targetThreatLinkTable);
		targetThreatLinkTableScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		addToVerticalController(targetThreatLinkTableScroller);
		addToHorizontalController(targetThreatLinkTableScroller);
		addRowHeightControlledTable(targetThreatLinkTable);
		addRowSortControlledTable(targetThreatLinkTable);
		
		JScrollPane threatSummaryColumnTableScroller = new FixedWidthScrollPane(threatSummaryColumnTable);
		addToVerticalController(threatSummaryColumnTableScroller);
		threatSummaryColumnTableScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		threatSummaryColumnTableScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		addRowHeightControlledTable(threatSummaryColumnTable);
		addRowSortControlledTable(threatSummaryColumnTable);
		
		JScrollPane targetSummaryRowTableScroller = new FixedHeightScrollPane(targetSummaryRowTable);
		addToHorizontalController(targetSummaryRowTableScroller);
		
		JScrollPane overallProjectSummaryCellTableScroller = new FixedHeightScrollPane(overallProjectSummaryCellTable);

		final int LEFT = 0;
		final int MIDDLE = 1;
		final int RIGHT = 2;
		final int TOP = 0;
		final int BOTTOM = 1;
		
		mainPanel.add(threatTableScroller,
				createGridBagConstraints(TOP, LEFT, GridBagConstraints.VERTICAL));
		mainPanel.add(targetThreatLinkTableScroller,
				createGridBagConstraints(TOP, MIDDLE, GridBagConstraints.BOTH));
		mainPanel.add(threatSummaryColumnTableScroller,
				createGridBagConstraints(TOP, RIGHT, GridBagConstraints.VERTICAL));
		mainPanel.add(targetSummaryRowTableScroller,
				createGridBagConstraints(BOTTOM, MIDDLE, GridBagConstraints.HORIZONTAL));
		mainPanel.add(overallProjectSummaryCellTableScroller,
				createGridBagConstraints(BOTTOM, RIGHT, GridBagConstraints.NONE));
		
		add(mainPanel);
	}

	private static GridBagConstraints createGridBagConstraints(int row, int column, int fill)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = column;
		gbc.gridy = row;
		gbc.fill = fill;
		
		gbc.anchor = gbc.CENTER;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		
		gbc.weightx = 0;
		if(fill == gbc.HORIZONTAL || fill == gbc.BOTH)
			gbc.weightx = 100;
		
		gbc.weighty = 0;
		if(fill == gbc.VERTICAL || fill == gbc.BOTH)
			gbc.weighty = 100;
		return gbc;
	}
	
	private void synchTableColumns()
	{
		ColumnChangeSyncer columnWidthSyncer = new ColumnChangeSyncer(targetSummaryRowTable);
		targetThreatLinkTable.getColumnModel().addColumnModelListener(columnWidthSyncer);
		
		columnWidthSyncer.syncPreferredColumnWidths(targetThreatLinkTable.getColumnModel());
	}
	
	public ObjectPicker getObjectPicker()
	{
		return this;
	}
	
	public ORefList[] getSelectedHierarchies()
	{
		return targetThreatLinkTable.getSelectedHierarchies();
	}
	
	public void addSelectionChangeListener(ListSelectionListener listener)
	{
		targetThreatLinkTable.getSelectionModel().addListSelectionListener(listener);
		targetThreatLinkTable.getColumnModel().getSelectionModel().addListSelectionListener(listener);
	}

	public void removeSelectionChangeListener(ListSelectionListener listener)
	{
		targetThreatLinkTable.getSelectionModel().removeListSelectionListener(listener);
	}
	
	private ThreatNameColumnTableModel threatNameTableModel;
	private ThreatNameColumnTable threatNameTable;
	private TargetThreatLinkTableModel targetThreatLinkTableModel;
	private TargetThreatLinkTable targetThreatLinkTable;
	
	private ThreatSummaryColumnTableModel threatSummaryColumnTableModel;
	private ThreatSummaryColumnTable threatSummaryColumnTable;
	
	private TargetSummaryRowTableModel targetSummaryRowTableModel;
	private TargetSummaryRowTable targetSummaryRowTable;
	
	private OverallProjectSummaryCellTable overallProjectSummaryCellTable;
	private OverallProjectSummaryCellTableModel overallProjectSummaryCellTableModel;
	
	private ThreatRatingMultiTableAsOneExporter multiTableExporter;
}