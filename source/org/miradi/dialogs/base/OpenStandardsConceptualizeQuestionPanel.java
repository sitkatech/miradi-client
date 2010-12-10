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

import java.util.HashMap;
import java.util.Vector;

import org.miradi.actions.AbstractJumpMenuAction;
import org.miradi.dialogs.dashboard.AbstractLongDescriptionProvider;
import org.miradi.dialogs.dashboard.DashboardRowDefinition;
import org.miradi.dialogs.dashboard.DashboardRowDefinitionManager;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
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
	protected void addRow(ChoiceItem choiceItem, int level) throws Exception
	{		
		super.addRow(choiceItem, level);
		
		DashboardRowDefinitionManager manager = new DashboardRowDefinitionManager();
		Vector<DashboardRowDefinition> rowDefinitions = manager.getRowDefinitions(choiceItem.getCode());
		
		for (DashboardRowDefinition rowDefinition: rowDefinitions)
		{
			Vector<String> pseudoTags = rowDefinition.getPseudoTags();
			HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
			if (pseudoTags.size() == 1)
			{
				tokenReplacementMap.put("%X", getDashboardData(pseudoTags.get(0)));
			}
			
			if (pseudoTags.size() == 2)
			{
				tokenReplacementMap.put("%X", getDashboardData(pseudoTags.get(0)));
				tokenReplacementMap.put("%Y", getDashboardData(pseudoTags.get(1)));
			}

			String code = choiceItem.getCode();
			AbstractLongDescriptionProvider longDescriptionProvider = choiceItem.getLongDescriptionProvider();
			AbstractJumpMenuAction action = getMainWindow().getActions().getJumpMenuAction(code);
			String stepName = getMainWindow().getWizardManager().stripJumpPrefix(action.getClass());
			longDescriptionProvider.setWizardStepName(stepName);

			addRow(EMPTY_LEFT_COLUMN_TEXT, rowDefinition.getRightColumnTemplate(), tokenReplacementMap, longDescriptionProvider, choiceItem.getCode(), level);
		}
	}
}
