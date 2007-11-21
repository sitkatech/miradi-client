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
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public class ThreatStressRatingMultiTablePanel extends MultiTablePanel
{
	public ThreatStressRatingMultiTablePanel(Project projectToUse) throws Exception
	{
		super(projectToUse);
		
		createTables();
		addTables();
	}
	
	private void createTables() throws Exception
	{
		threatTableModel = new ThreatTableModel(getProject());
		threatTable = new ThreatTable(threatTableModel);

		threatStressRatintListModel = new ThreatStressRatingListTableModel(getProject());
		threatStressRatingListTable = new ThreatStressRatingListTable(threatStressRatintListModel);
		
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
		return threatStressRatingListTable;
	}
	
	private MainThreatTableModel threatTableModel;
	private ThreatTable threatTable;
	private ThreatStressRatingListTableModel threatStressRatintListModel;
	private ThreatStressRatingListTable threatStressRatingListTable;
	private TargetThreatLinkTableModel targetThreatLinkTableModel;
	private TargetThreatLinkTable targetThreatLinkTable;
}
