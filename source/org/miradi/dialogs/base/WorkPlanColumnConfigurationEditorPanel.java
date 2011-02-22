/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.WorkPlanColumnConfigurationQuestion;
import org.miradi.questions.WorkPlanVisibleRowsQuestion;

public class WorkPlanColumnConfigurationEditorPanel extends ObjectDataInputPanel
{
	public WorkPlanColumnConfigurationEditorPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);

		addField(createChoiceField(TableSettings.getObjectType(), TableSettings.TAG_WORK_PLAN_VISIBLE_NODES_CODE, getProject().getQuestion(WorkPlanVisibleRowsQuestion.class)));
		
		addLabel("");
		addLabel(EAM.text("<html>Select which column groups to display."));
		
		PanelTitleLabel label = new PanelTitleLabel(EAM.text("Column groups"));
		ChoiceQuestion columnConfigurationQuestion = getProject().getQuestion(WorkPlanColumnConfigurationQuestion.class);
		addFieldWithCustomLabel(createStringMapWorkPlanBudgetColumnCodeListEditor(orefToUse.getObjectType(), TableSettings.TAG_TABLE_SETTINGS_MAP, columnConfigurationQuestion), label);
		
		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Work Plan Column Editor");
	}
}
