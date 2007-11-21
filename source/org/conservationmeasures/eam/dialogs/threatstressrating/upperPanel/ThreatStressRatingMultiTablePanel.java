/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.dialogs.base.MultiTablePanel;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public class ThreatStressRatingMultiTablePanel extends MultiTablePanel
{
	public ThreatStressRatingMultiTablePanel(Project projectToUse) throws Exception
	{
		super(projectToUse);
		
		createTables();
		addTables();
		addTablesToSelectionController();
	}
	
	private void addTablesToSelectionController()
	{
		selectionController.addTable(threatTable);
		selectionController.addTable(targetThreatLinkTable);
	}
	
	private void createTables() throws Exception
	{
		threatTableModel = new ThreatTableModel(getProject());
		threatTable = new ThreatTable(threatTableModel);

		targetThreatLinkTableModel = new TargetThreatLinkTableModel(getProject());
		targetThreatLinkTable = new TargetThreatLinkTable(targetThreatLinkTableModel);
	}

	private void addTables()
	{
		Box horizontalBox = Box.createHorizontalBox();
		JScrollPane threatTableScroller = new ScrollPaneWithInvisibleVerticalScrollBar(threatTable);
		addVerticalScrollableControlledTable(horizontalBox, threatTableScroller);

		JScrollPane targetThreatLinkTableScroller = new ScrollPaneWithInvisibleVerticalScrollBar(targetThreatLinkTable);
		addVerticalAndHorizontalScrollableControlledTable(horizontalBox, targetThreatLinkTableScroller);
		
		add(horizontalBox, BorderLayout.CENTER);
	}
	
	public ObjectPicker getObjectPicker()
	{
		return this;
	}
	
	public ORefList[] getSelectedHierarchies()
	{
		return targetThreatLinkTable.getSelectedHierarchies();
	}
	
	private ThreatTableModel threatTableModel;
	private ThreatTable threatTable;
	private TargetThreatLinkTableModel targetThreatLinkTableModel;
	private TargetThreatLinkTable targetThreatLinkTable;
}