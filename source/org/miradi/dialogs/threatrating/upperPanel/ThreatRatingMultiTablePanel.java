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

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.base.ColumnMarginResizeListenerValidator;
import org.miradi.dialogs.base.MultiTablePanel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefList;
import org.miradi.utils.AbstractTableExporter;
import org.miradi.utils.FastScrollPane;
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
		addRowHeightControlledTable(threatNameTable);
		addRowSortControlledTable(threatNameTable);
		listenForColumnWidthChanges(threatNameTable);

		targetThreatLinkTableModel = new TargetThreatLinkTableModel(getProject());
		targetThreatLinkTable = new TargetThreatLinkTable(getMainWindow(), targetThreatLinkTableModel);
		addRowHeightControlledTable(targetThreatLinkTable);
		addRowSortControlledTable(targetThreatLinkTable);

		threatSummaryColumnTableModel = new ThreatSummaryColumnTableModel(getProject());
		threatSummaryColumnTable = new ThreatSummaryColumnTable(getMainWindow(), threatSummaryColumnTableModel);
		addRowHeightControlledTable(threatSummaryColumnTable);
		addRowSortControlledTable(threatSummaryColumnTable);

		targetSummaryRowTableModel = new TargetSummaryRowTableModel(getProject());
		targetSummaryRowTable = new TargetSummaryRowTable(getMainWindow(), targetSummaryRowTableModel, targetThreatLinkTable);
		targetSummaryRowTable.resizeTable(1);
		
		listenForColumnWidthChanges(targetSummaryRowTable);
		
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
	
	class ScrollPaneWithSizeConstraints extends FastScrollPane
	{
		public ScrollPaneWithSizeConstraints(Component view)
		{
			super(view);
		}
		
		public void capMaxWidth()
		{
			capMaxWidth = true;
		}
		
		public void capMaxHeight()
		{
			capMaxHeight = true;
		}
		
		public void capMinWidth()
		{
			capMinWidth = true;
		}
		
		public void capMinHeight()
		{
			capMinHeight = true;
		}
		
		@Override
		public Dimension getMaximumSize()
		{
			final Dimension max = super.getMaximumSize();
			final Dimension preferred = getPreferredSize();
			int width = max.width;
			if(capMaxWidth)
				width = Math.min(width, preferred.width);
			int height = max.height;
			if(capMaxHeight)
				height = Math.min(height, preferred.height);
			return new Dimension(width, height);
		}

		@Override
		public Dimension getMinimumSize()
		{
			final Dimension min = super.getMinimumSize();
			final Dimension preferred = getPreferredSize();
			int width = min.width;
			if(capMinWidth)
				width = Math.max(width, preferred.width);
			int height = min.height;
			if(capMinHeight)
				height = Math.max(height, preferred.height);
			return new Dimension(width, height);
		}

		private boolean capMaxWidth;
		private boolean capMaxHeight;
		private boolean capMinWidth;
		private boolean capMinHeight;
	}
	
	static class ComponentSizeMatcher
	{
		public ComponentSizeMatcher(JComponent matchWidthOfComponent, JComponent matchHeightOfComponent)
		{
			matchWidthOf = matchWidthOfComponent;
			matchHeightOf = matchHeightOfComponent;
		}
		
		public Dimension getSize()
		{
			return new Dimension(matchWidthOf.getWidth(), matchHeightOf.getHeight());
		}
		
		private JComponent matchWidthOf;
		private JComponent matchHeightOf;
	}
	
	static class CornerFillerComponent extends JComponent
	{
		public CornerFillerComponent(JComponent matchWidthOfComponent, JComponent matchHeightOfComponent)
		{
			matcher = new ComponentSizeMatcher(matchWidthOfComponent, matchHeightOfComponent);
		}
		
		@Override
		public int getWidth()
		{
			return getSize().width;
		}
		
		@Override
		public int getHeight()
		{
			return getSize().height;
		}
		
		@Override
		public Dimension getSize()
		{
			return matcher.getSize();
		}
		
		@Override
		public Dimension getPreferredSize()
		{
			return getSize();
		}
		
		@Override
		public Dimension getMaximumSize()
		{
			return getSize();
		}
		
		@Override
		public Dimension getMinimumSize()
		{
			return getSize();
		}
		
		private ComponentSizeMatcher matcher;
	}
	
	static class ScrollPaneWithWidthMatchingForSingleRowTable extends FastScrollPane
	{
		public ScrollPaneWithWidthMatchingForSingleRowTable(JTable view, JComponent matchWidthOf)
		{
			super(view);
			table = view;
			matcher = new ComponentSizeMatcher(matchWidthOf, this);
		}
		
		@Override
		public int getWidth()
		{
			return getSize().width;
		}
		
		@Override
		public Dimension getSize()
		{
			return new Dimension(matcher.getSize().width, super.getSize().height);
		}
		
		@Override
		public Dimension getPreferredSize()
		{
			return new Dimension(matcher.getSize().width, super.getPreferredSize().height);
		}
		
		@Override
		public Dimension getMaximumSize()
		{
			return new Dimension(matcher.getSize().width, table.getRowHeight(0) + getHorizontalScrollBar().getPreferredSize().height);
		}
		
		@Override
		public Dimension getMinimumSize()
		{
			return new Dimension(matcher.getSize().width, super.getMinimumSize().height);
		}
		
		private JTable table;
		private ComponentSizeMatcher matcher;
	}
	
	private void addTableToGridBag()
	{		
		ScrollPaneWithSizeConstraints threatTableScroller = new ScrollPaneWithSizeConstraints(threatNameTable);
		threatTableScroller.capMinWidth();
		threatTableScroller.capMaxWidth();
		threatTableScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		threatTableScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		addToVerticalController(threatTableScroller);
		
		ScrollPaneWithSizeConstraints targetThreatLinkTableScroller = new ScrollPaneWithSizeConstraints(targetThreatLinkTable);
		targetThreatLinkTableScroller.capMaxWidth();
		targetThreatLinkTableScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		targetThreatLinkTableScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		addToVerticalController(targetThreatLinkTableScroller);
		addToHorizontalController(targetThreatLinkTableScroller);
		
		ScrollPaneWithSizeConstraints threatSummaryColumnTableScroller = new ScrollPaneWithSizeConstraints(threatSummaryColumnTable);
		threatSummaryColumnTableScroller.capMinWidth();
		threatSummaryColumnTableScroller.capMaxWidth();
		addToVerticalController(threatSummaryColumnTableScroller);
		threatSummaryColumnTableScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		threatSummaryColumnTableScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JScrollPane targetSummaryRowTableScroller = new ScrollPaneWithWidthMatchingForSingleRowTable(targetSummaryRowTable, targetThreatLinkTableScroller);
		addToHorizontalController(targetSummaryRowTableScroller);
		targetSummaryRowTableScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		targetSummaryRowTableScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
		JScrollPane overallProjectSummaryCellTableScroller = new ScrollPaneWithWidthMatchingForSingleRowTable(overallProjectSummaryCellTable, threatSummaryColumnTableScroller);
		overallProjectSummaryCellTableScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		overallProjectSummaryCellTableScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		CornerFillerComponent lowerLeftCell = new CornerFillerComponent(threatTableScroller, targetSummaryRowTableScroller);
		
		Box hBoxTop = Box.createHorizontalBox();
		hBoxTop.add(threatTableScroller);
		hBoxTop.add(targetThreatLinkTableScroller);
		hBoxTop.add(threatSummaryColumnTableScroller);
		hBoxTop.add(Box.createHorizontalGlue());
		
		Box hBoxBottom = Box.createHorizontalBox();
		hBoxBottom.add(lowerLeftCell);
		hBoxBottom.add(targetSummaryRowTableScroller);
		hBoxBottom.add(overallProjectSummaryCellTableScroller);
		hBoxBottom.add(Box.createHorizontalGlue());
		
		Box vbox = Box.createVerticalBox();
		vbox.add(hBoxTop);
		vbox.add(hBoxBottom);
		
		add(vbox);
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