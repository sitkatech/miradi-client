/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import javax.swing.JToolBar;

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
import org.conservationmeasures.eam.main.ViewSwitcher;
import org.conservationmeasures.eam.utils.ToolBarButton;

public class DiagramToolBar extends JToolBar
{
	public DiagramToolBar(Actions actions)
	{
		setFloatable(false);

		add(ViewSwitcher.create(actions, ActionViewDiagram.class));
		addSeparator();
		add(new ToolBarButton(actions, ActionInsertIntervention.class));
		add(new ToolBarButton(actions, ActionInsertIndirectFactor.class));
		add(new ToolBarButton(actions, ActionInsertDirectThreat.class));
		
		add(new ToolBarButton(actions, ActionInsertTarget.class));
		add(new ToolBarButton(actions, ActionInsertConnection.class));
		addSeparator();
		add(new ToolBarButton(actions, ActionUndo.class));
		add(new ToolBarButton(actions, ActionRedo.class));
		addSeparator();
		add(new ToolBarButton(actions, ActionCut.class));
		add(new ToolBarButton(actions, ActionCopy.class));
		add(new ToolBarButton(actions, ActionPaste.class));
		add(new ToolBarButton(actions, ActionDelete.class));
		addSeparator();
		add(new ToolBarButton(actions, ActionPrint.class));
		addSeparator();
		add(new ToolBarButton(actions, ActionZoomIn.class));
		add(new ToolBarButton(actions, ActionZoomOut.class));
	}
	
}

