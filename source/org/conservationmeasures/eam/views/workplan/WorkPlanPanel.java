/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableWithIcons;
import org.martus.swing.UiScrollPane;

public class WorkPlanPanel extends JPanel
{
	public WorkPlanPanel(Project projectToUse)
	{
		super(new BorderLayout());
		WorkPlanTreeTableModel model = new WorkPlanTreeTableModel(projectToUse);
		TreeTableWithIcons tree = new WorkPlanTreeTable(model);
		UiScrollPane uiScrollPane = new UiScrollPane(tree);
		add(uiScrollPane, BorderLayout.CENTER);
	}
}
