/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import javax.swing.JComponent;

import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCreateBendPoint;
import org.conservationmeasures.eam.actions.ActionCreateOrShowResultsChain;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionInsertContributingFactor;
import org.conservationmeasures.eam.actions.ActionInsertDirectThreat;
import org.conservationmeasures.eam.actions.ActionInsertDraftStrategy;
import org.conservationmeasures.eam.actions.ActionInsertFactorLink;
import org.conservationmeasures.eam.actions.ActionInsertIntermediateResult;
import org.conservationmeasures.eam.actions.ActionInsertStrategy;
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.ActionInsertTextBox;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionPrint;
import org.conservationmeasures.eam.actions.ActionShowConceptualModel;
import org.conservationmeasures.eam.actions.ActionShowFullModelMode;
import org.conservationmeasures.eam.actions.ActionShowSelectedChainMode;
import org.conservationmeasures.eam.actions.ActionToggleSlideShowPanel;
import org.conservationmeasures.eam.actions.ActionZoomIn;
import org.conservationmeasures.eam.actions.ActionZoomOut;
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
				new ToolBarButton(actions, ActionInsertTextBox.class),
				getInsertInterventionButton(actions, diagramView),
				getContributionFactorButton(actions, diagramView),
				new ToolBarButton(actions, ActionInsertDirectThreat.class),
				new ToolBarButton(actions, ActionInsertTarget.class),
				new ToolBarButton(actions, ActionInsertFactorLink.class),
				new ToolBarButton(actions, ActionCreateBendPoint.class),
				new ToolBarButton(actions, ActionToggleSlideShowPanel.class),
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
				getModeSwitchButton(actions, diagramView),
				getDiagramObjectSwitchButton(actions, diagramView),
			},
		};
		
		return buttons;
	}
	
	static ToolBarButton getDiagramObjectSwitchButton(Actions actions, DiagramView diagramView)
	{
		if (diagramView.isResultsChainTab())
			return new ToolBarButton(actions, ActionShowConceptualModel.class);
			
		return new ToolBarButton(actions, ActionCreateOrShowResultsChain.class);
	}
	
	static ToolBarButton getInsertInterventionButton(Actions actions, DiagramView diagramView)
	{
		if(diagramView.isStategyBrainstormMode())
			return new ToolBarButton(actions, ActionInsertDraftStrategy.class);
		return new ToolBarButton(actions, ActionInsertStrategy.class);
	}

	static ToolBarButton getModeSwitchButton(Actions actions, DiagramView diagramView)
	{
		if(diagramView.isStategyBrainstormMode())
			return new ToolBarButton(actions, ActionShowFullModelMode.class);
		return new ToolBarButton(actions, ActionShowSelectedChainMode.class);
	}
	
	static ToolBarButton getContributionFactorButton(Actions actions, DiagramView diagramView)
	{
		if(diagramView.isResultsChainTab())
			return new ToolBarButton(actions, ActionInsertIntermediateResult.class);
		return new ToolBarButton(actions, ActionInsertContributingFactor.class);
	}
}

