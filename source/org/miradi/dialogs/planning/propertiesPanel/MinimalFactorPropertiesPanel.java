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
package org.miradi.dialogs.planning.propertiesPanel;

import javax.swing.Icon;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.project.Project;
import org.miradi.questions.EvidenceConfidenceTypeQuestion;
import org.miradi.schemas.BaseObjectSchema;

public class MinimalFactorPropertiesPanel extends ObjectDataInputPanelWithSections
{
	public MinimalFactorPropertiesPanel(Project projectToUse, BaseObjectSchema factorSchemaToUse)
	{
		super(projectToUse, factorSchemaToUse.getType());
		factorSchema = factorSchemaToUse;
		createSingleSection(EAM.text("Summary"));
	}
	
	protected void createAndAddFields(String translatedNameLabel, Icon icon) throws Exception
	{
		ObjectDataInputField shortLabelField = createShortStringField(Factor.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createExpandableField(Factor.TAG_LABEL);
		addFieldsOnOneLine(translatedNameLabel, icon, new ObjectDataInputField[]{shortLabelField, labelField,});
		
		addCustomFieldsStart();
		
		addField(createMultilineField(Factor.TAG_TEXT));

		addCustomFieldsMiddle();

		addField(createMultilineField(Factor.TAG_COMMENTS));

		if (canHaveEvidenceConfidence(factorSchema))
			addField(createRadioButtonEditorField(factorSchema.getType(), BaseObject.TAG_EVIDENCE_CONFIDENCE, EvidenceConfidenceTypeQuestion.getQuestion(factorSchema)));

		addField(createMultilineField(Factor.TAG_EVIDENCE_NOTES));

		addCustomFieldsEnd();
	}

	protected void addCustomFieldsStart()
	{
	}

	protected void addCustomFieldsMiddle()
	{
	}

	protected void addCustomFieldsEnd() throws Exception
	{
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Factor Properties");
	}

	private boolean canHaveEvidenceConfidence(BaseObjectSchema factorSchema)
	{
		if (factorSchema.containsField(BaseObject.TAG_EVIDENCE_CONFIDENCE))
			return true;

		return false;
	}

	private BaseObjectSchema factorSchema;
}
