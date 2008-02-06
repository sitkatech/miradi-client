/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import javax.swing.JComponent;

import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCreateOrShowResultsChain;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionPrint;
import org.conservationmeasures.eam.actions.ActionShowConceptualModel;
import org.conservationmeasures.eam.actions.ActionShowFullModelMode;
import org.conservationmeasures.eam.actions.ActionShowSelectedChainMode;
import org.conservationmeasures.eam.actions.ActionZoomIn;
import org.conservationmeasures.eam.actions.ActionZoomOut;
import org.conservationmeasures.eam.actions.ActionZoomToFit;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.views.ActionViewDiagram;
import org.conservationmeasures.eam.main.EAMToolBar;
import org.conservationmeasures.eam.utils.ToolBarButton;

public class DiagramToolBar extends EAMToolBar
{
	public DiagramToolBar(Actions actions, DiagramView diagramView)
	{
		super(actions, ActionViewDiagram.class, createButtons(actions, diagramView));
	}
	
	static JComponent[][] createButtons(Actions actions, DiagramView diagramView)
	{
		JComponent[][] buttons = new JComponent[][] {
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
				new ToolBarButton(actions, ActionZoomToFit.class),
			},
			{
				getModeSwitchButton(actions, diagramView),
				getDiagramObjectSwitchButton(actions, diagramView),
			},
		};
		
		return buttons;
	}
	
	private static ToolBarButton getDiagramObjectSwitchButton(Actions actions, DiagramView diagramView)
	{
		if (diagramView.isResultsChainTab())
			return new ToolBarButton(actions, ActionShowConceptualModel.class);
			
		return new ToolBarButton(actions, ActionCreateOrShowResultsChain.class);
	}
	
	private static ToolBarButton getModeSwitchButton(Actions actions, DiagramView diagramView)
	{
		if(diagramView.isStategyBrainstormMode())
			return new ToolBarButton(actions, ActionShowFullModelMode.class);
		return new ToolBarButton(actions, ActionShowSelectedChainMode.class);
	}
	
}

