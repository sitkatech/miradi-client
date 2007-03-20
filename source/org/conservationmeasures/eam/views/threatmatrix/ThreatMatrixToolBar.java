/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.JComponent;

import org.conservationmeasures.eam.actions.ActionHideCellRatings;
import org.conservationmeasures.eam.actions.ActionShowCellRatings;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.views.ActionViewThreatMatrix;
import org.conservationmeasures.eam.main.EAMToolBar;
import org.conservationmeasures.eam.utils.ToolBarButton;

public class ThreatMatrixToolBar extends EAMToolBar
{
	public ThreatMatrixToolBar(Actions actions)
	{
		super(actions, ActionViewThreatMatrix.class, createButtons(actions));
	}
	
	static JComponent[][] createButtons(Actions actions)
	{
		JComponent[][] buttons = new JComponent[][] {
				{new ToolBarButton(actions, ActionShowCellRatings.class),
				 new ToolBarButton(actions, ActionHideCellRatings.class),	
				},
			};
		
		return buttons;
	}
}

