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
package org.miradi.dialogs.threatstressrating.upperPanel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.base.MultiTablePanel;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.Project;
import org.miradi.views.umbrella.ObjectPicker;

public class ThreatStressRatingMultiTablePanel extends MultiTablePanel implements ListSelectionListener
{
	public ThreatStressRatingMultiTablePanel(Project projectToUse) throws Exception
	{
		super(projectToUse);
		
		createTables();
		addTableToGridBag();
		addTablesToSelectionController();
		synchTableColumns();
	}
	
	private void addTablesToSelectionController()
	{
		selectionController.addTable(threatTable);
		selectionController.addTable(targetThreatLinkTable);
		selectionController.addTable(threatSummaryColumnTable);
	}
	
	private void createTables() throws Exception
	{
		threatTableModel = new ThreatNameColumnTableModel(getProject());
		threatTable = new ThreatNameColumnTable(threatTableModel);

		targetThreatLinkTableModel = new TargetThreatLinkTableModel(getProject());
		targetThreatLinkTable = new TargetThreatLinkTable(targetThreatLinkTableModel);

		threatSummaryColumnTableModel = new ThreatSummaryColumnTableModel(getProject());
		threatSummaryColumnTable = new ThreatSummaryColumnTable(threatSummaryColumnTableModel);
		
		targetSummaryRowTableModel = new TargetSummaryRowTableModel(getProject());
		targetSummaryRowTable = new TargetSummaryRowTable(targetSummaryRowTableModel);
		targetSummaryRowTable.resizeTable(1);
		
		overallProjectSummaryCellTableModel = new OverallProjectSummaryCellTableModel(getProject());
		overallProjectSummaryCellTable = new OverallProjectSummaryCellTable(overallProjectSummaryCellTableModel);
		overallProjectSummaryCellTable.resizeTable(1);
	}

	
	private void addTableToGridBag()
	{		
		JPanel mainPanel = new JPanel(new GridBagLayout());
		
		JScrollPane threatTableScroller = new FixedWidthScrollPaneWithInvisibleVerticalScrollBar(threatTable);
		threatTableScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		addToVerticalController(threatTableScroller);
		addRowHeightControlledTable(threatTable);
		
		JScrollPane targetThreatLinkTableScroller = new ScrollPaneWithInvisibleVerticalScrollBar(targetThreatLinkTable);
		targetThreatLinkTableScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		addToVerticalController(targetThreatLinkTableScroller);
		addToHorizontalController(targetThreatLinkTableScroller);
		addRowHeightControlledTable(targetThreatLinkTable);
		
		JScrollPane threatSummaryColumnTableScroller = new FixedWidthScrollPane(threatSummaryColumnTable);
		addToVerticalController(threatSummaryColumnTableScroller);
		threatSummaryColumnTableScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		threatSummaryColumnTableScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		addRowHeightControlledTable(threatSummaryColumnTable);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.anchor = GridBagConstraints.WEST;
		addToPanelFixedWidth(mainPanel, threatTableScroller, constraints, 0, 0, 1, 1, 0, 100);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		addToPanelFixedWidth(mainPanel, targetThreatLinkTableScroller, constraints, 1, 0, 1, 1, 100, 100);
		
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.anchor = GridBagConstraints.WEST;
		addToPanelFixedWidth(mainPanel, threatSummaryColumnTableScroller, constraints, 2, 0, 1, 1, 0, 100);

		JScrollPane targetSummaryRowTableScroller = new FixedHeightScrollPane(targetSummaryRowTable);
		addToHorizontalController(targetSummaryRowTableScroller);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.NORTH;
		addToPanelFixedWidth(mainPanel, targetSummaryRowTableScroller, constraints, 1, 1, 1, 1, 100, 0);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		JScrollPane overallProjectSummaryCellTableScroller = new FixedHeightScrollPane(overallProjectSummaryCellTable);
		addToPanelFixedWidth(mainPanel, overallProjectSummaryCellTableScroller, constraints, 2, 1, 1, 1, 0, 0);
		
		add(mainPanel);
	}

	public static void addToPanelFixedWidth(JPanel p, Component c, GridBagConstraints gbc, int x, int y, int w, int h, int weightX, int weightY) 
	{
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.weightx = weightX;
		gbc.weighty = weightY;
		p.add(c,gbc);
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
	
	private ThreatNameColumnTableModel threatTableModel;
	private ThreatNameColumnTable threatTable;
	private TargetThreatLinkTableModel targetThreatLinkTableModel;
	private TargetThreatLinkTable targetThreatLinkTable;
	
	private ThreatSummaryColumnTableModel threatSummaryColumnTableModel;
	private ThreatSummaryColumnTable threatSummaryColumnTable;
	
	private TargetSummaryRowTableModel targetSummaryRowTableModel;
	private TargetSummaryRowTable targetSummaryRowTable;
	
	private OverallProjectSummaryCellTable overallProjectSummaryCellTable;
	private OverallProjectSummaryCellTableModel overallProjectSummaryCellTableModel;
}