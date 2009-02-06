/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.diagram;

import javax.swing.JComponent;

import org.miradi.actions.ActionCopy;
import org.miradi.actions.ActionCreateDiagramMargin;
import org.miradi.actions.ActionCreateOrShowResultsChain;
import org.miradi.actions.ActionCut;
import org.miradi.actions.ActionDelete;
import org.miradi.actions.ActionPaste;
import org.miradi.actions.ActionPrint;
import org.miradi.actions.ActionShowConceptualModel;
import org.miradi.actions.ActionShowFullModelMode;
import org.miradi.actions.ActionShowSelectedChainMode;
import org.miradi.actions.ActionZoomIn;
import org.miradi.actions.ActionZoomOut;
import org.miradi.actions.ActionZoomToFit;
import org.miradi.actions.Actions;
import org.miradi.actions.views.ActionViewDiagram;
import org.miradi.main.EAMToolBar;
import org.miradi.utils.ToolBarButton;

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
				new ToolBarButton(actions, ActionCreateDiagramMargin.class),
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

