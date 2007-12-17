/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.dialogs.base.MultiTablePanel;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

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
		threatTableModel = new ThreatTableModel(getProject());
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
		
		JScrollPane targetThreatLinkTableScroller = new ScrollPaneWithInvisibleVerticalScrollBar(targetThreatLinkTable);
		targetThreatLinkTableScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		addToVerticalController(targetThreatLinkTableScroller);
		addToHorizontalController(targetThreatLinkTableScroller);
		
		JScrollPane threatSummaryColumnTableScroller = new FixedWidthScrollPane(threatSummaryColumnTable);
		addToVerticalController(threatSummaryColumnTableScroller);
		threatSummaryColumnTableScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		threatSummaryColumnTableScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
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
		
		JScrollPane overallProjectSummaryCellTableScroller = new FixedHeightScrollPane(overallProjectSummaryCellTable);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.NORTH;
		addToPanelFixedWidth(mainPanel, targetSummaryRowTableScroller, constraints, 1, 1, 1, 1, 100, 0);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		addToPanelFixedWidth(mainPanel, overallProjectSummaryCellTableScroller, constraints, 2, 1, 1, 1, 0, 100);
		
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
	
	private ThreatTableModel threatTableModel;
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