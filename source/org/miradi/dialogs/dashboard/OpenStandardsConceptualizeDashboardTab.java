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

import org.miradi.dialogs.base.OpenStandardsConceptualizeQuestionPanel;
import org.miradi.main.MainWindow;
import org.miradi.objects.Dashboard;
import org.miradi.questions.OpenStandardsConceptualizeQuestion;

public class OpenStandardsConceptualizeDashboardTab extends	OpenStandardsDashboardTab
{
	private OpenStandardsConceptualizeDashboardTab(MainWindow mainWindowToUse, OpenStandardsConceptualizeQuestionPanel leftPanelToUse) throws Exception
	{
		super(mainWindowToUse, leftPanelToUse);
	}
	
	public static OpenStandardsConceptualizeDashboardTab createLeftPanel(MainWindow mainWindowToUse) throws Exception
	{
		OpenStandardsConceptualizeQuestionPanel leftPanelToUse = new OpenStandardsConceptualizeQuestionPanel(mainWindowToUse.getProject());
		return new OpenStandardsConceptualizeDashboardTab(mainWindowToUse, leftPanelToUse);
	}
	
	@Override
	protected AbstractLongDescriptionProvider getDefaultDescriptionProvider() throws Exception
	{
		return new HtmlResourceLongDescriptionProvider(OpenStandardsConceptualizeQuestion.MAIN_DESCRIPTION_RIGHT_PANEL_FILE_NAME);
	}

	@Override
	public String getPanelDescription()
	{
		return new OpenStandardsConceptualizeQuestion().getConceptualizeHeaderLabel();
	}
	
	@Override
	public String getTabCode()
	{
		return Dashboard.DASHBOARD_CONCEPTUALIZE_TAB_CODE;
	}
}
