/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import javax.swing.JComponent;

import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionInsertConnection;
import org.conservationmeasures.eam.actions.ActionInsertDirectThreat;
import org.conservationmeasures.eam.actions.ActionInsertDraftIntervention;
import org.conservationmeasures.eam.actions.ActionInsertIndirectFactor;
import org.conservationmeasures.eam.actions.ActionInsertIntervention;
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.ActionNormalDiagramMode;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionPrint;
import org.conservationmeasures.eam.actions.ActionStrategyBrainstormMode;
import org.conservationmeasures.eam.actions.ActionZoomIn;
import org.conservationmeasures.eam.actions.ActionZoomOut;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.views.ActionViewDiagram;
import org.conservationmeasures.eam.main.EAMToolBar;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.utils.ToolBarButton;

public class DiagramToolBar extends EAMToolBar
{
	public DiagramToolBar(Actions actions, String mode)
	{
		super(actions, ActionViewDiagram.class, createButtons(actions, mode));
	}
	
	static JComponent[][] createButtons(Actions actions, String mode)
	{
		JComponent[][] buttons = new JComponent[][] {
			{
				getInsertInterventionButton(actions, mode),
				new ToolBarButton(actions, ActionInsertIndirectFactor.class),
				new ToolBarButton(actions, ActionInsertDirectThreat.class),
				new ToolBarButton(actions, ActionInsertTarget.class),
				new ToolBarButton(actions, ActionInsertConnection.class),
			},
			{
				new ToolBarButton(actions, ActionCut.class),
				new ToolBarButton(actions, ActionCopy.class),
				new ToolBarButton(actions, ActionPaste.class),
				new ToolBarButton(actions, ActionDelete.class),
			},
			{
				new ToolBarButton(actions, ActionPrint.class),
			},
			{
				new ToolBarButton(actions, ActionZoomIn.class),
				new ToolBarButton(actions, ActionZoomOut.class),
			},
			{
				getModeSwitchButton(actions, mode),
			},
		};
		
		return buttons;
	}
	
	static ToolBarButton getInsertInterventionButton(Actions actions, String mode)
	{
		if(mode.equals(ViewData.MODE_STRATEGY_BRAINSTORM))
			return new ToolBarButton(actions, ActionInsertDraftIntervention.class);
		return new ToolBarButton(actions, ActionInsertIntervention.class);
	}

	static ToolBarButton getModeSwitchButton(Actions actions, String mode)
	{
		if(mode.equals(ViewData.MODE_STRATEGY_BRAINSTORM))
			return new ToolBarButton(actions, ActionNormalDiagramMode.class);
		return new ToolBarButton(actions, ActionStrategyBrainstormMode.class);
	}
}

