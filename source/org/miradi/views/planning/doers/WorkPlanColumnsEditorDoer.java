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

package org.miradi.views.planning.doers;

import org.miradi.dialogfields.AbstractWorkPlanStringMapEditorDoer;
import org.miradi.dialogs.base.WorkPlanColumnConfigurationEditorPanel;
import org.miradi.main.EAM;
import org.miradi.objects.TableSettings;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.WorkPlanColumnConfigurationQuestion;

public class WorkPlanColumnsEditorDoer extends AbstractWorkPlanStringMapEditorDoer
{
	@Override
	protected WorkPlanColumnConfigurationEditorPanel createEditorPanel(TableSettings workPlanTableSettings)
	{
		ChoiceQuestion columnConfigurationQuestion = getProject().getQuestion(WorkPlanColumnConfigurationQuestion.class);
		
		return new WorkPlanColumnConfigurationEditorPanel(getProject(), workPlanTableSettings.getRef(), TableSettings.TAG_TABLE_SETTINGS_MAP, columnConfigurationQuestion);
	}

	@Override
	protected String getDialogTitle()
	{
		return EAM.text("Title|Customize Table");
	}
}
