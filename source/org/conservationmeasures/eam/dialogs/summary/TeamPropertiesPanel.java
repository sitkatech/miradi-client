/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.summary;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.TeamRoleQuestion;

public class TeamPropertiesPanel extends ObjectDataInputPanel
{
	public TeamPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ProjectResource.getObjectType(), BaseId.INVALID);

		ObjectDataInputField nameField = createStringField(ProjectResource.TAG_NAME);
		ObjectDataInputField initialField = createStringField(ProjectResource.TAG_INITIALS,STD_SHORT);
		addFieldsOnOneLine(EAM.text("Label|"), new ObjectDataInputField[]{nameField, initialField});
		
		addField(createMultiCodeField(new TeamRoleQuestion(ProjectResource.TAG_ROLE_CODES), 3));
		addField(createStringField(ProjectResource.TAG_ORGANIZATION));
		addField(createStringField(ProjectResource.TAG_POSITION));
		addField(createStringField(ProjectResource.TAG_LOCATION));
		
		ObjectDataInputField mainPhoneNumberField = createStringField(ProjectResource.TAG_PHONE_NUMBER);
		ObjectDataInputField mobilePhoneNumberField = createStringField(ProjectResource.TAG_PHONE_NUMBER_MOBILE);
		addFieldsOnOneLine(EAM.text("Label|Phone Numbers"), new ObjectDataInputField[]{mainPhoneNumberField, mobilePhoneNumberField});
		
		ObjectDataInputField homePhoneNumberField = createStringField(ProjectResource.TAG_PHONE_NUMBER_HOME);
		ObjectDataInputField otherPhoneNumberField = createStringField(ProjectResource.TAG_PHONE_NUMBER_OTHER);
		addFieldsOnOneLine("Home", new ObjectDataInputField[]{homePhoneNumberField, otherPhoneNumberField});
		
		addField(createStringField(ProjectResource.TAG_EMAIL));
		addField(createStringField(ProjectResource.TAG_ALTERNATIVE_EMAIL));
		
		ObjectDataInputField iMAddressField = createStringField(ProjectResource.TAG_IM_ADDRESS);
		ObjectDataInputField iMServiceField = createStringField(ProjectResource.TAG_IM_SERVICE);
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
