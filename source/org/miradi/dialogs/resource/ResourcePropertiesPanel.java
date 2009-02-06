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
package org.miradi.dialogs.resource;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.ids.BaseId;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.questions.BudgetCostUnitQuestion;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.questions.ResourceTypeQuestion;
import org.miradi.utils.CodeList;

public class ResourcePropertiesPanel extends ObjectDataInputPanel
{
	public ResourcePropertiesPanel(Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.PROJECT_RESOURCE, idToEdit);

		ResourceTypeQuestion resourceTypeQuestion = new ResourceTypeQuestion();
		addField(createRadioChoiceField(ProjectResource.getObjectType(), idToEdit, ProjectResource.TAG_RESOURCE_TYPE, resourceTypeQuestion));

		ObjectDataInputField givenNameField = createMediumStringField(ProjectResource.TAG_GIVEN_NAME);
		ObjectDataInputField surNameField = createMediumStringField(ProjectResource.TAG_SUR_NAME);
		ObjectDataInputField initialField = createStringField(ProjectResource.TAG_INITIALS,STD_SHORT);
		addFieldsOnOneLine(EAM.text("Label|Resource"), new ObjectDataInputField[]{givenNameField, surNameField, initialField});

		roleCodeField = createMultiCodeField(ProjectResource.TAG_ROLE_CODES, new ResourceRoleQuestion(), 3);
		addFieldWithCustomLabel(roleCodeField, new PanelTitleLabel(EAM.text("Label|Roles (people only)")));
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

		addField(createRatingChoiceField(ProjectResource.TAG_COST_UNIT, new BudgetCostUnitQuestion()));
		addField(createCurrencyField(ProjectResource.TAG_COST_PER_UNIT));

		addField(createMultilineField(ProjectResource.TAG_COMMENTS));
		
		addField(createStringField(ProjectResource.TAG_CUSTOM_FIELD_1));
		addField(createStringField(ProjectResource.TAG_CUSTOM_FIELD_2));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Resource Properties");
	}
	
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		updateVisibilityOfRoleCodeField();
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		
		if(event.isSetDataCommandWithThisTypeAndTag(ProjectResource.getObjectType(), ProjectResource.TAG_RESOURCE_TYPE))
		{
			updateVisibilityOfRoleCodeField();
		}
		
		if(!event.isSetDataCommandWithThisTypeAndTag(ObjectType.PROJECT_RESOURCE, ProjectResource.TAG_ROLE_CODES))
			return;
			
		try
		{
			CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
			CodeList oldCodes = new CodeList(cmd.getPreviousDataValue());
			CodeList newCodes = new CodeList(cmd.getDataValue());
			oldCodes.subtract(newCodes);
			if(!oldCodes.contains(ResourceRoleQuestion.TeamMemberRoleCode))
				return;
			
			EAM.okDialog(EAM.text("Remove Team Member"), new String[] {
					EAM.text("You are removing this resource from the project team, " +
							"so he/she will no longer appear in " +
							"the list of Team Members in the Summary View. ")});
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		
	}
	
	private void updateVisibilityOfRoleCodeField()
	{
		BaseId idBeingEdited = getObjectIdForType(ProjectResource.getObjectType());
		if(idBeingEdited == null || idBeingEdited.isInvalid())
			return;
		
		ORef ref = new ORef(ProjectResource.getObjectType(), idBeingEdited);
		ProjectResource beingEdited = ProjectResource.find(getProject(), ref);
		boolean isPerson = beingEdited.isPerson();
		roleCodeField.setEditable(isPerson);
	}

	private ObjectDataInputField roleCodeField;
}
