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

package org.miradi.dialogs.base;

import javax.swing.JComponent;

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.questions.OpenStandardsConceptualizeQuestion;

public class OpenStandardsConceptualizeQuestionPanel extends AbstractOpenStandardsQuestionPanel
{
	public OpenStandardsConceptualizeQuestionPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, new OpenStandardsConceptualizeQuestion());
	}

	@Override
	public String getPanelDescription()
	{
		return "OpenStandardsConceptualizeQuestionPanel";
	}
	
	@Override
	protected void addFourthLevelRow(String code)
	{
		if (code.equals(OpenStandardsConceptualizeQuestion.SELECT_INTIAL_TEAM_MEMBERS_CODE))
			addTeamMembersRow();
	}

	public void addTeamMembersRow()
	{
		String leftColumnTranslatedText = EAM.text("Team Members:");
		String rightColumnTranslatedText = getDashboardData(Dashboard.PSEUDO_TEAM_MEMBER_COUNT);
		
		JComponent leftComponent = new PanelTitleLabel(leftColumnTranslatedText);
		JComponent rightComponent = new PanelTitleLabel(rightColumnTranslatedText);
		rightComponent.setFont(getRawFont());
		
		addFourthLevelRow(leftComponent, rightComponent);
	}
}
