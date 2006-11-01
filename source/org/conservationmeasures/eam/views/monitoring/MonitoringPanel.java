/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.martus.swing.UiScrollPane;

public class MonitoringPanel extends JPanel
{
	public MonitoringPanel(Project projectToUse)
	{
		super(new BorderLayout());
		model = new MonitoringModel(projectToUse);
		tree = new MonitoringTreeTable(model);
		add(new UiScrollPane(tree), BorderLayout.CENTER);
	}

	MonitoringTreeTable tree;
	GenericTreeTableModel model;
}
