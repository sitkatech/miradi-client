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
package org.miradi.dialogs.summary;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.ProjectResourceIcon;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.utils.CodeList;

public class TeamMemberPropertiesPanel extends ObjectDataInputPanel
{
	public TeamMemberPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ProjectResource.getObjectType(), BaseId.INVALID);

		ObjectDataInputField givenNameField = createMediumStringField(ProjectResource.TAG_GIVEN_NAME);
		ObjectDataInputField surNameField = createMediumStringField(ProjectResource.TAG_SUR_NAME);
		ObjectDataInputField initialField = createStringField(ProjectResource.TAG_INITIALS,STD_SHORT);
		ObjectDataInputField[] nameFields = new ObjectDataInputField[]{
				givenNameField, 
				surNameField, 
				initialField
				};
		String[] nameLabelTexts = new String[] {
				EAM.fieldLabel(ProjectResource.getObjectType(), ProjectResource.TAG_GIVEN_NAME),
				EAM.fieldLabel(ProjectResource.getObjectType(), ProjectResource.TAG_SUR_NAME),
				EAM.text("Label|Initials"),
				};
		addFieldsOnOneLine(EAM.text("Label|Team Member"), new ProjectResourceIcon(), nameLabelTexts, nameFields);
		
		CodeList disabledRoleCodes = new CodeList(new String[] {ResourceRoleQuestion.TeamMemberRoleCode});
		addField(createMultiCodeEditorField(ProjectResource.TAG_ROLE_CODES, new ResourceRoleQuestion(), disabledRoleCodes, 3));
		addField(createStringField(ProjectResource.TAG_ORGANIZATION));
		addField(createStringField(ProjectResource.TAG_POSITION));
		addField(createStringField(ProjectResource.TAG_LOCATION));
		
		ObjectDataInputField mainPhoneNumberField = createMediumStringField(ProjectResource.TAG_PHONE_NUMBER);
		ObjectDataInputField mobilePhoneNumberField = createMediumStringField(ProjectResource.TAG_PHONE_NUMBER_MOBILE);
		addFieldsOnOneLine(EAM.text("Label|Phone Numbers"), new ObjectDataInputField[]{mainPhoneNumberField, mobilePhoneNumberField});
		
		ObjectDataInputField homePhoneNumberField = createMediumStringField(ProjectResource.TAG_PHONE_NUMBER_HOME);
		ObjectDataInputField otherPhoneNumberField = createMediumStringField(ProjectResource.TAG_PHONE_NUMBER_OTHER);
		addFieldsOnOneLine(" ", new ObjectDataInputField[]{homePhoneNumberField, otherPhoneNumberField});
		
		addField(createStringField(ProjectResource.TAG_EMAIL));
		addField(createStringField(ProjectResource.TAG_ALTERNATIVE_EMAIL));
		
		ObjectDataInputField iMAddressField = createMediumStringField(ProjectResource.TAG_IM_ADDRESS);
		ObjectDataInputField iMServiceField = createMediumStringField(ProjectResource.TAG_IM_SERVICE);
		addFieldsOnOneLine(EAM.text("Label|IM Address"), new ObjectDataInputField[]{iMAddressField, iMServiceField});

		addField(createDateChooserField(ProjectResource.TAG_DATE_UPDATED));

		addField(createMultilineField(ProjectResource.TAG_COMMENTS));
		addField(createStringField(ProjectResource.TAG_CUSTOM_FIELD_1));
		addField(createStringField(ProjectResource.TAG_CUSTOM_FIELD_2));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Team Properties");
	}
}
