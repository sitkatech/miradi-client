/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import javax.swing.JComboBox;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionViewDiagram;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.utils.ToolBarButton;

public class InterviewToolBar extends JToolBar
{
	public InterviewToolBar(Actions actions)
	{
		setFloatable(false);

		String[] views = new String[] {"Interview", "Diagram", "GIS Mapping", "Budgets", "Calendar",};
		JComboBox viewCombo = new JComboBox(views);
		viewCombo.setSelectedIndex(1);

		add(new ToolBarButton(actions, ActionViewDiagram.class));
		add(viewCombo);
	}
}
