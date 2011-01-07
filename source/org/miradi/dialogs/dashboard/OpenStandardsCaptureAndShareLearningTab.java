/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.dashboard;

import org.miradi.dialogs.base.AbstractOpenStandardsQuestionPanel;
import org.miradi.main.MainWindow;
import org.miradi.questions.OpenStandardsCaptureAndShareLearningQuestion;

public class OpenStandardsCaptureAndShareLearningTab extends SplitterPanelWithRightSideTextPanel
{
	private OpenStandardsCaptureAndShareLearningTab(MainWindow mainWindowToUse, AbstractOpenStandardsQuestionPanel leftPanelToUse) throws Exception
	{
		super(mainWindowToUse, leftPanelToUse);
	}
	
	public static OpenStandardsCaptureAndShareLearningTab createLeftPanel(MainWindow mainWindowToUse) throws Exception
	{
		OpenStandardsCaptureAndShareLearningQuestionPanel leftPanelToUse = new OpenStandardsCaptureAndShareLearningQuestionPanel(mainWindowToUse.getProject());
		return new OpenStandardsCaptureAndShareLearningTab(mainWindowToUse, leftPanelToUse);
	}
	
	@Override
	protected AbstractLongDescriptionProvider getDefaultDescriptionProvider() throws Exception
	{
		return new HtmlResourceLongDescriptionProvider(OpenStandardsCaptureAndShareLearningQuestion.MAIN_DESCRIPTION_FILENAME);
	}

	@Override
	public String getPanelDescription()
	{
		return new OpenStandardsCaptureAndShareLearningQuestion().getHeaderLabel();
	}
}
