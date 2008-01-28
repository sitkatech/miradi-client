/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.summary;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ResourceRoleQuestion;
import org.conservationmeasures.eam.utils.CodeList;

public class TeamMemberPropertiesPanel extends ObjectDataInputPanel
{
	public TeamMemberPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ProjectResource.getObjectType(), BaseId.INVALID);

		ObjectDataInputField givenNameField = createMediumStringField(ProjectResource.TAG_GIVEN_NAME);
		ObjectDataInputField surNameField = createMediumStringField(ProjectResource.TAG_SUR_NAME);
		ObjectDataInputField initialField = createStringField(ProjectResource.TAG_INITIALS,STD_SHORT);
		addFieldsOnOneLine(EAM.text("Label|Team Member"), new ObjectDataInputField[]{givenNameField, surNameField, initialField});
		
		CodeList disabledRoleCodes = new CodeList(new String[] {ResourceRoleQuestion.TeamMemberRoleCode});
		addField(createMultiCodeField(ProjectResource.TAG_ROLE_CODES, new ResourceRoleQuestion(ProjectResource.TAG_ROLE_CODES), disabledRoleCodes, 3));
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
		addFieldsOnOneLine("IM Address", new ObjectDataInputField[]{iMAddressField, iMServiceField});

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
