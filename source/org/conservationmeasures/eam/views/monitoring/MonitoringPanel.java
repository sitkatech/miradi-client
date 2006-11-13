/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.martus.swing.UiScrollPane;

public class MonitoringPanel extends JPanel
{
	public MonitoringPanel(Project projectToUse) throws Exception
	{
		super(new BorderLayout());
		model = new MonitoringModel(projectToUse);
		tree = new MonitoringTreeTable(projectToUse, model);
		restoreTreeExpansionState();
		add(new UiScrollPane(tree), BorderLayout.CENTER);
	}
	
	private void restoreTreeExpansionState() 
	{
		try
		{
			tree.getModelAdapter().restoreTreeState();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error restoring tree state"));
		}
	}

	MonitoringTreeTable tree;
	GenericTreeTableModel model;
}
