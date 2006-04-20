/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.JComponent;

import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.ActionViewThreatMatrix;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAMToolBar;
import org.conservationmeasures.eam.utils.ToolBarButton;

public class ThreatMatrixToolBar extends EAMToolBar
{
	public ThreatMatrixToolBar(Actions actions)
	{
		super(actions, ActionViewThreatMatrix.class, createButtons(actions));
	}
	
	static JComponent[] createButtons(Actions actions)
	{
		JComponent[] buttons = new JComponent[] {
				new Separator(),
				new ToolBarButton(actions, ActionUndo.class),
				new ToolBarButton(actions, ActionRedo.class),
		};
		return buttons;
	}

}
