/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.dialogs.base.MultiTablePanel;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

import com.jhlabs.awt.BasicGridLayout;

public class ThreatStressRatingMultiTablePanel extends MultiTablePanel implements ListSelectionListener
{
	public ThreatStressRatingMultiTablePanel(Project projectToUse) throws Exception
	{
		super(projectToUse);
	
		createTables();
		addTables();
		addTablesToSelectionController();
		targetThreatLinkTable.getSelectionModel().addListSelectionListener(this);
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
		threatTable = new ThreatTable(threatTableModel);

		targetThreatLinkTableModel = new TargetThreatLinkTableModel(getProject());
		targetThreatLinkTable = new TargetThreatLinkTable(targetThreatLinkTableModel);
		
		threatSummaryColumnTableModel = new ThreatSummaryColumnTableModel(getProject());
		threatSummaryColumnTable = new ThreatSummaryColumnTable(threatSummaryColumnTableModel);
		
		targetSummaryRowTableModel = new TargetSummaryRowTableModel(getProject());
		targetSummaryRowTable = new TargetSummaryRowTable(targetSummaryRowTableModel);
		targetSummaryRowTable.resizeTable(1);
	}

	private void addTables()
	{
		JPanel mainPanel = new JPanel(new BasicGridLayout(2, 3));
		JScrollPane threatTableScroller = new ScrollPaneWithInvisibleVerticalScrollBar(threatTable);
		threatTableScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		addToVerticalController(threatTableScroller);
		
		JScrollPane targetThreatLinkTableScroller = new ScrollPaneWithInvisibleVerticalScrollBar(targetThreatLinkTable);
		targetThreatLinkTableScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		addToVerticalController(targetThreatLinkTableScroller);
		addToHorizontalController(targetThreatLinkTableScroller);
		
		JScrollPane threatSummaryColumnTableScroller = new FastScrollPane(threatSummaryColumnTable);
		addToVerticalController(threatSummaryColumnTableScroller);
		threatSummaryColumnTableScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		threatSummaryColumnTableScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		mainPanel.add(threatTableScroller);
		mainPanel.add(targetThreatLinkTableScroller);
		mainPanel.add(threatSummaryColumnTableScroller);
		
		JScrollPane targetSummaryRowTableScroller = new ScrollPaneWithInvisibleVerticalScrollBar(targetSummaryRowTable);
		addToHorizontalController(targetSummaryRowTableScroller);
		mainPanel.add(new JLabel());
		mainPanel.add(targetSummaryRowTableScroller);
		FastScrollPane mainPanelScroller = new FastScrollPane(mainPanel);
		add(mainPanelScroller);		
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
	}

	public void removeSelectionChangeListener(ListSelectionListener listener)
	{
		targetThreatLinkTable.getSelectionModel().removeListSelectionListener(listener);
	}
	
	private ThreatTableModel threatTableModel;
	private ThreatTable threatTable;
	private TargetThreatLinkTableModel targetThreatLinkTableModel;
	private TargetThreatLinkTable targetThreatLinkTable;
	
	private ThreatSummaryColumnTableModel threatSummaryColumnTableModel;
	private ThreatSummaryColumnTable threatSummaryColumnTable;
	
	private TargetSummaryRowTableModel targetSummaryRowTableModel;
	private TargetSummaryRowTable targetSummaryRowTable;
}