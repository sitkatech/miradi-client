/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.JComponent;

import org.conservationmeasures.eam.actions.ActionHideCellRatings;
import org.conservationmeasures.eam.actions.ActionShowCellRatings;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.views.ActionViewThreatMatrix;
import org.conservationmeasures.eam.main.EAMToolBar;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.questions.ThreatRatingModeChoiceQuestion;
import org.conservationmeasures.eam.utils.ToolBarButton;

public class ThreatMatrixToolBar extends EAMToolBar
{
	public ThreatMatrixToolBar(MainWindow mainWindowToUse, boolean isCellRatingVisible)
	{
		super(mainWindowToUse.getActions(), 
			  ActionViewThreatMatrix.class, 
			  createButtons(mainWindowToUse, isCellRatingVisible));
	}
	
	static JComponent[][] createButtons(MainWindow mainWindow, boolean isCellRatingVisible)
	{
		if (mainWindow.getProject().getMetadata().getThreatRatingMode().equals(ThreatRatingModeChoiceQuestion.STRESS_BASED_CODE))
				return new JComponent[0][0];
		
		JComponent[][] buttons = new JComponent[][] 
		    {
				{getCellRatingsButton(mainWindow.getActions(), isCellRatingVisible)},
			};
		
		return buttons;
	}
	
	private static ToolBarButton getCellRatingsButton(Actions actions, boolean isCellRatingVisible)
	{
		if (isCellRatingVisible)
			return new ToolBarButton(actions, ActionHideCellRatings.class);
	
		return new ToolBarButton(actions, ActionShowCellRatings.class);
	}
	
}

