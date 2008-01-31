/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.organization;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Organization;
import org.conservationmeasures.eam.project.Project;

public class OrganizationPropertiesPanel extends ObjectDataInputPanel
{
	public OrganizationPropertiesPanel(Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.ORGANIZATION, idToEdit);

		ObjectDataInputField labelField = createStringField(Organization.TAG_LABEL);
		ObjectDataInputField shortLabelField = createShortStringField(Organization.TAG_SHORT_LABEL);
		addFieldsOnOneLine(EAM.text("Label|Organization"), new ObjectDataInputField[]{labelField, shortLabelField});
		
		addField(createStringField(Organization.TAG_ROLES));

		ObjectDataInputField firstNameField = createStringField(Organization.TAG_CONTACT_FIRST_NAME);
		ObjectDataInputField lastNameField = createStringField(Organization.TAG_CONTACT_LAST_NAME);
		addFieldsOnOneLine(EAM.text("Label|Org Contact"), new ObjectDataInputField[]{firstNameField, lastNameField});
		
		addField(createStringField(Organization.TAG_EMAIL));
		addField(createStringField(Organization.TAG_PHONE_NUMBER));
		addField(createMultilineField(Organization.TAG_COMMENTS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Organization Properties");
	}
}
