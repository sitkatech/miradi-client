/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.resource;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.BudgetCostUnitQuestion;
import org.conservationmeasures.eam.questions.ResourceRoleQuestion;
import org.conservationmeasures.eam.questions.ResourceTypeQuestion;
import org.conservationmeasures.eam.utils.CodeList;

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
		addFieldsOnOneLine("IM Address", new ObjectDataInputField[]{iMAddressField, iMServiceField});

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
