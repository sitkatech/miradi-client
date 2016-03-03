/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.resource;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogfields.*;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.ids.BaseId;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.questions.ResourceTypeQuestion;
import org.miradi.schemas.ProjectResourceSchema;
import org.miradi.utils.CodeList;

public class ResourcePropertiesPanel extends ObjectDataInputPanel
{
	public ResourcePropertiesPanel(Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.PROJECT_RESOURCE);

		resourceTypeRadioButtonHandler = new ResourceTypeHandler();
		teamMemberCheckBoxHandler = new TeamMemberHandler();
		
		ResourceTypeQuestion resourceTypeQuestion = new ResourceTypeQuestion();
		resourceTypeField = createRadioChoiceField(new ORef(ProjectResourceSchema.getObjectType(), idToEdit), ProjectResource.TAG_RESOURCE_TYPE, resourceTypeQuestion);
		addField(resourceTypeField);

		firstNameLabel = new PanelTitleLabel(EAM.text(FIRST_NAME_LABEL_PERSON));
		ObjectDataInputField givenNameField = createMediumStringField(ProjectResource.TAG_GIVEN_NAME);
		addFieldsOnOneLine(new PanelTitleLabel(EAM.text("Label|Resource")), new Object[]{firstNameLabel, givenNameField});

		lastNameLabel = new PanelTitleLabel(EAM.text(LAST_NAME_LABEL_PERSON));
		ObjectDataInputField surNameField = createMediumStringField(ProjectResource.TAG_SUR_NAME);
		addFieldsOnOneLine(new PanelTitleLabel(EAM.text(" ")), new Object[]{lastNameLabel, surNameField});

		initialLabel = new PanelTitleLabel(EAM.text(INITIAL_LABEL_PERSON));
		ObjectDataInputField initialField = createStringField(ProjectResource.TAG_INITIALS,STD_SHORT);
		addFieldsOnOneLine(new PanelTitleLabel(EAM.text(" ")), new Object[]{initialLabel, initialField});

		addTaxonomyFields(ProjectResourceSchema.getObjectType());

		roleCodeField = createMultiCodeField(ProjectResource.TAG_ROLE_CODES, new ResourceRoleQuestion(), 3);
		addFieldWithCustomLabel(roleCodeField, EAM.text("Label|Roles (people only)"));
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

		costPerUnitFieldHint = addFieldWithCustomLabelAndDynamicHint(createCurrencyField(ProjectResource.TAG_COST_PER_UNIT), getWorkUnitRateDescriptionHint());

		addField(createMultilineField(ProjectResource.TAG_COMMENTS));
		
		addField(createStringField(ProjectResource.TAG_CUSTOM_FIELD_1));
		addField(createStringField(ProjectResource.TAG_CUSTOM_FIELD_2));
		
		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Resource Properties");
	}
	
	@Override
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		updateVisibilityOfRoleCodeField();
		updateNameLabels();
	}

	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		
		if(event.isSetDataCommandWithThisTypeAndTag(ProjectResourceSchema.getObjectType(), ProjectResource.TAG_RESOURCE_TYPE))
		{
			updateVisibilityOfRoleCodeField();
		}
	}
	
	private void updateVisibilityOfRoleCodeField()
	{
		ORef ref = getProjectResourceRefBeingEdited();

		if (ref.isValid())
		{
			ProjectResource projectResource = ProjectResource.find(getProject(), ref);
			boolean isPerson = projectResource.isPerson();
			roleCodeField.setEditable(isPerson);
		}
	}

	private ORef getProjectResourceRefBeingEdited()
	{
		BaseId idBeingEdited = getObjectIdForType(ProjectResourceSchema.getObjectType());
		if(idBeingEdited == null || idBeingEdited.isInvalid())
			return ORef.INVALID;

		return new ORef(ProjectResourceSchema.getObjectType(), idBeingEdited);
	}

	private String getWorkUnitRateDescriptionHint()
	{
		return getProject().getMetadata().getWorkUnitRateDescription();
	}

	@Override
	public void becomeActive()
	{
		super.becomeActive();

		resourceTypeField.addListSelectionListener(resourceTypeRadioButtonHandler);
		costPerUnitFieldHint.setText(getWorkUnitRateDescriptionHint());
		roleCodeField.getCodeListEditor().addListSelectionListener(teamMemberCheckBoxHandler);
	}
	
	@Override
	public void becomeInactive()
	{
		resourceTypeField.removeListSelectionListener(resourceTypeRadioButtonHandler);
		roleCodeField.getCodeListEditor().removeListSelectionListener(teamMemberCheckBoxHandler);
		
		super.becomeInactive();
	}
	
	public static void notifyUserThatResourceIsNoLongerPartOfTeam()
	{
		EAM.okDialog(EAM.text("Remove Team Member"), new String[] {
			EAM.text("You are removing this resource from the project team, " +
					 "so he/she will no longer appear in " +
					 "the list of Team Members in the Summary View. ")});
	}

	private void updateNameLabels()
	{
		ORef ref = getProjectResourceRefBeingEdited();

		if (ref.isValid())
		{
			ProjectResource projectResource = ProjectResource.find(getProject(), ref);
			updateNameLabels(projectResource.isPerson());
		}
		else
		{
			updateNameLabels(true);
		}
	}

	private void updateNameLabels(boolean isPerson)
	{
		if (isPerson)
		{
			firstNameLabel.setText(FIRST_NAME_LABEL_PERSON);
			lastNameLabel.setText(LAST_NAME_LABEL_PERSON);
			initialLabel.setText(INITIAL_LABEL_PERSON);
		}
		else
		{
			firstNameLabel.setText(FIRST_NAME_LABEL_GROUP);
			lastNameLabel.setText(LAST_NAME_LABEL_GROUP);
			initialLabel.setText(INITIAL_LABEL_GROUP);
		}
	}

	class TeamMemberHandler implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent event)
		{
			try
			{
				String code = ((ChoiceItemListSelectionEvent)event).getCode();
				if (!code.equals(ResourceRoleQuestion.TEAM_MEMBER_ROLE_CODE))
					return;

				CodeList newRoleCodes = new CodeList(roleCodeField.getCodeListEditor().getText());
				if(newRoleCodes.contains(ResourceRoleQuestion.TEAM_MEMBER_ROLE_CODE))
					return;

				notifyUserThatResourceIsNoLongerPartOfTeam();
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}
	}

	class ResourceTypeHandler implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent event)
		{
			try
			{
				String code = ((ChoiceItemListSelectionEvent)event).getCode();
				updateNameLabels(code.equals(ResourceTypeQuestion.PERSON_CODE));
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}
	}

	private PanelTitleLabel firstNameLabel;
	private PanelTitleLabel lastNameLabel;
	private PanelTitleLabel initialLabel;
	private ObjectRadioButtonGroupField resourceTypeField;
	private ObjectCodeEditorField roleCodeField;
	private PanelTitleLabel costPerUnitFieldHint;
	private TeamMemberHandler teamMemberCheckBoxHandler;
	private ResourceTypeHandler resourceTypeRadioButtonHandler;

	// note: whitespace is a hacktastic attempt to align columns
	private static final String FIRST_NAME_LABEL_PERSON = "First Name   ";
	private static final String FIRST_NAME_LABEL_GROUP = "Group Name";
	private static final String LAST_NAME_LABEL_PERSON = "Last Name    ";
	private static final String LAST_NAME_LABEL_GROUP = "                      ";
	private static final String INITIAL_LABEL_PERSON = "Resource ID ";
	private static final String INITIAL_LABEL_GROUP = "Resource ID  ";
}
