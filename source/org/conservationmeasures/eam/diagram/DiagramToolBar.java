/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import javax.swing.JComboBox;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionInsertConnection;
import org.conservationmeasures.eam.actions.ActionInsertGoal;
import org.conservationmeasures.eam.actions.ActionInsertIntervention;
import org.conservationmeasures.eam.actions.ActionInsertThreat;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.ActionViewInterview;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.utils.ToolBarButton;

public class DiagramToolBar extends JToolBar
{
	public DiagramToolBar(Actions actions)
	{
		setFloatable(false);

		String[] views = new String[] {"Interview", "Diagram", "GIS Mapping", "Budgets", "Calendar",};
		JComboBox viewCombo = new JComboBox(views);
		viewCombo.setSelectedIndex(1);

		//add(viewCombo);
		add(new ToolBarButton(actions, ActionViewInterview.class));
		addSeparator();
		add(new ToolBarButton(actions, ActionInsertGoal.class));
		add(new ToolBarButton(actions, ActionInsertThreat.class));
		add(new ToolBarButton(actions, ActionInsertIntervention.class));
		add(new ToolBarButton(actions, ActionInsertConnection.class));
		addSeparator();
		add(new ToolBarButton(actions, ActionUndo.class));
		add(new ToolBarButton(actions, ActionRedo.class));
		addSeparator();
		add(new ToolBarButton(actions, ActionCut.class));
		add(new ToolBarButton(actions, ActionCopy.class));
		add(new ToolBarButton(actions, ActionPaste.class));
		add(new ToolBarButton(actions, ActionDelete.class));
	}
	
}

