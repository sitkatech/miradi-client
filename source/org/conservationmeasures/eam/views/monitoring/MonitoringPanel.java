/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiScrollPane;

public class MonitoringPanel extends JPanel
{
	public MonitoringPanel(Project projectToUse)
	{
		super(new BorderLayout());
		model = new MonitoringModel(projectToUse);
		add(new UiScrollPane(new MonitoringTreeTable(model)), BorderLayout.CENTER);
	}

	MonitoringModel model;
}
