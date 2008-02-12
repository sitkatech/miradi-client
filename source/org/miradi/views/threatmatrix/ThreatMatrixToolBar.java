/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.threatmatrix;

import javax.swing.JComponent;

import org.miradi.actions.ActionHideCellRatings;
import org.miradi.actions.ActionShowCellRatings;
import org.miradi.actions.Actions;
import org.miradi.actions.views.ActionViewThreatMatrix;
import org.miradi.main.EAMToolBar;
import org.miradi.main.MainWindow;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;
import org.miradi.utils.ToolBarButton;

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

