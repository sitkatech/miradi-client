/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.organization;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.OrganizationIcon;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Organization;
import org.miradi.project.Project;

public class OrganizationPropertiesPanel extends ObjectDataInputPanel
{
	public OrganizationPropertiesPanel(Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.ORGANIZATION, idToEdit);

		ObjectDataInputField labelField = createStringField(Organization.TAG_LABEL, 30);
		ObjectDataInputField shortLabelField = createShortStringField(Organization.TAG_SHORT_LABEL);
		addFieldsOnOneLine(EAM.text("Label|Organization"), new OrganizationIcon(), new ObjectDataInputField[]{labelField, shortLabelField});
		
		addField(createStringField(Organization.TAG_ROLES_DESCRIPTION));

		ObjectDataInputField firstNameField = createMediumStringField(Organization.TAG_CONTACT_FIRST_NAME);
		ObjectDataInputField lastNameField = createMediumStringField(Organization.TAG_CONTACT_LAST_NAME);
		addFieldsOnOneLine(EAM.text("Label|Org Contact"), new ObjectDataInputField[]{firstNameField, lastNameField});
		
		addField(createMediumStringField(Organization.TAG_EMAIL));
		addField(createMediumStringField(Organization.TAG_PHONE_NUMBER));
		addField(createMultilineField(Organization.TAG_COMMENTS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Organization Properties");
	}
}
