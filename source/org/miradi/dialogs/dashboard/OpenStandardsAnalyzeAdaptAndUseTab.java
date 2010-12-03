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
import org.miradi.questions.OpenStandardsAnalyzeUseAndAdaptQuestion;

public class OpenStandardsAnalyzeAdaptAndUseTab extends PanelWithDescriptionPanel
{
	private OpenStandardsAnalyzeAdaptAndUseTab(MainWindow mainWindowToUse, AbstractOpenStandardsQuestionPanel leftPanelToUse) throws Exception
	{
		super(mainWindowToUse, leftPanelToUse);
	}
	
	public static OpenStandardsAnalyzeAdaptAndUseTab createLeftPanel(MainWindow mainWindowToUse) throws Exception
	{
		OpenStandardsAnalyzeAdaptAndUseQuestionPanel leftPanelToUse = new OpenStandardsAnalyzeAdaptAndUseQuestionPanel(mainWindowToUse.getProject());
		return new OpenStandardsAnalyzeAdaptAndUseTab(mainWindowToUse, leftPanelToUse);
	}
	
	@Override
	protected void setupCommunicationBetweenLeftAndRightPanels(RightSideDescriptionPanel rightPanel)
	{
		((AbstractOpenStandardsQuestionPanel) leftPanel).addRowSelectionListener(rightPanel);
	}

	@Override
	protected AbstractLongDescriptionProvider getDefaultDescriptionProvider() throws Exception
	{
		return new StaticLongDescriptionProvider();
	}

	@Override
	public String getPanelDescription()
	{
		return new OpenStandardsAnalyzeUseAndAdaptQuestion().getHeaderLabel();
	}
}
