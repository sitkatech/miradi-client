/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

