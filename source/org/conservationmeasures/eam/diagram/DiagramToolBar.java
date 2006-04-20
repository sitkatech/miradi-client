/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import javax.swing.JComponent;

import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionInsertConnection;
import org.conservationmeasures.eam.actions.ActionInsertDirectThreat;
import org.conservationmeasures.eam.actions.ActionInsertIndirectFactor;
import org.conservationmeasures.eam.actions.ActionInsertIntervention;
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionPrint;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.ActionViewDiagram;
import org.conservationmeasures.eam.actions.ActionZoomIn;
import org.conservationmeasures.eam.actions.ActionZoomOut;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAMToolBar;
import org.conservationmeasures.eam.utils.ToolBarButton;

public class DiagramToolBar extends EAMToolBar
{
	public DiagramToolBar(Actions actions)
	{
		super(actions, ActionViewDiagram.class, createButtons(actions));
	}
	
	static JComponent[] createButtons(Actions actions)
	{
		JComponent[] buttons = new JComponent[] {
				new Separator(),
				new ToolBarButton(actions, ActionInsertIntervention.class),
				new ToolBarButton(actions, ActionInsertIndirectFactor.class),
				new ToolBarButton(actions, ActionInsertDirectThreat.class),
				new ToolBarButton(actions, ActionInsertTarget.class),
				new ToolBarButton(actions, ActionInsertConnection.class),
				new Separator(),
				new ToolBarButton(actions, ActionUndo.class),
				new ToolBarButton(actions, ActionRedo.class),
				new Separator(),
				new ToolBarButton(actions, ActionCut.class),
				new ToolBarButton(actions, ActionCopy.class),
				new ToolBarButton(actions, ActionPaste.class),
				new ToolBarButton(actions, ActionDelete.class),
				new Separator(),
				new ToolBarButton(actions, ActionPrint.class),
				new Separator(),
				new ToolBarButton(actions, ActionZoomIn.class),
				new ToolBarButton(actions, ActionZoomOut.class),
				
		};
		return buttons;
	}
}

