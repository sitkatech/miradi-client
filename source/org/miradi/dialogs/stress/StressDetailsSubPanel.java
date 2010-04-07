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
package org.miradi.dialogs.stress;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.StressIcon;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Stress;
import org.miradi.project.Project;
import org.miradi.questions.ScopeThreatRatingQuestion;
import org.miradi.questions.SeverityThreatRatingQuestion;
import org.miradi.questions.StressRatingChoiceQuestion;

public class StressDetailsSubPanel extends ObjectDataInputPanel
{
	public StressDetailsSubPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ObjectType.STRESS, BaseId.INVALID);
	
		ObjectDataInputField shortLabelField = createShortStringField(Stress.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createExpandableField(Stress.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Stress"), new StressIcon(), new ObjectDataInputField[]{shortLabelField, labelField});
		
		addField(createMultilineField(Stress.TAG_DETAIL));
		if (projectToUse.isStressBaseMode())
			addRatingsFields();
		
		updateFieldsFromProject();
	}

	private void addRatingsFields() throws Exception
	{
		ObjectDataInputField scopeField = createPopupQuestionEditor(Stress.getObjectType(), Stress.TAG_SCOPE, ScopeThreatRatingQuestion.class);
		ObjectDataInputField severityField = createPopupQuestionEditor(Stress.getObjectType(), Stress.TAG_SEVERITY, SeverityThreatRatingQuestion.class);		
		addFieldsOnOneLine(EAM.text("Ratings"), new ObjectDataInputField[]{scopeField, severityField});
		
		addField(createReadOnlyChoiceField(Stress.getObjectType(), Stress.PSEUDO_STRESS_RATING, new StressRatingChoiceQuestion()));
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Stress Details");
	}
}
