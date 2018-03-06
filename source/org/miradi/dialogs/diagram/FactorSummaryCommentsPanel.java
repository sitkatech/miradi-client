/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.diagram;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.project.Project;
import org.miradi.questions.EvidenceConfidenceTypeQuestion;
import org.miradi.schemas.BaseObjectSchema;

public class FactorSummaryCommentsPanel extends ObjectDataInputPanel
{
	public FactorSummaryCommentsPanel(Project project, BaseObjectSchema factorSchema) throws Exception
	{
		super(project, factorSchema.getType());

		addFactorCoreFields(factorSchema);

		addField(createMultilineField(factorSchema.getType(), Factor.TAG_COMMENTS));

		if (canHaveEvidenceConfidence(factorSchema))
			addField(createRadioButtonEditorField(factorSchema.getType(), BaseObject.TAG_EVIDENCE_CONFIDENCE, EvidenceConfidenceTypeQuestion.getQuestion(factorSchema)));

		if (canHaveEvidenceNotes(factorSchema))
			addField(createMultilineField(factorSchema.getType(), Factor.TAG_EVIDENCE_NOTES));
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Comments");
	}

	protected void addFactorCoreFields(BaseObjectSchema factorSchema) throws Exception
	{
	}

	private boolean canHaveEvidenceConfidence(BaseObjectSchema factorSchema)
	{
		if (factorSchema.containsField(BaseObject.TAG_EVIDENCE_CONFIDENCE))
			return true;

		return false;
	}

	private boolean canHaveEvidenceNotes(BaseObjectSchema factorSchema)
	{
		if (factorSchema.containsField(BaseObject.TAG_EVIDENCE_NOTES))
			return true;

		return false;
	}
}
