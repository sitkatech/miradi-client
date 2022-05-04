/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.dialogs.threatrating.properties;

import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataField;
import org.miradi.objects.AbstractThreatRatingData;
import org.miradi.project.Project;
import org.miradi.questions.EvidenceConfidenceTypeQuestion;

public class ThreatRatingDataEvidenceSubPanel extends AbstractThreatRatingDataSubPanel
{
	public ThreatRatingDataEvidenceSubPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, actions);
	}

	@Override
	protected void addFields(int threatRatingDataObjectType) throws Exception
	{
		commentsField = addField(createMultilineField(threatRatingDataObjectType, AbstractThreatRatingData.TAG_COMMENTS));
		evidenceConfidenceField = addField(createRadioButtonEditorField(threatRatingDataObjectType, AbstractThreatRatingData.TAG_EVIDENCE_CONFIDENCE, EvidenceConfidenceTypeQuestion.getQuestion(threatRatingDataObjectType)));
		evidenceNotesField = addField(createMultilineField(threatRatingDataObjectType, AbstractThreatRatingData.TAG_EVIDENCE_NOTES));
	}

	@Override
	public String getPanelDescription()
	{
		return "ThreatRatingDataEvidenceSubPanel";
	}

	private ObjectDataField commentsField;
	private ObjectDataField evidenceConfidenceField;
	private ObjectDataField evidenceNotesField;
}
