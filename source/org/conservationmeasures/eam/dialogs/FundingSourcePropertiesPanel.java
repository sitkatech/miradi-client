/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.AccountingCode;
import org.conservationmeasures.eam.project.Project;

public class FundingSourcePropertiesPanel extends ObjectDataInputPanel
{
	public FundingSourcePropertiesPanel(Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.FUNDING_SOURCE, idToEdit);
		
		addField(createStringField(AccountingCode.TAG_LABEL));
		addField(createStringField(AccountingCode.TAG_CODE));
		addField(createMultilineField(AccountingCode.TAG_COMMENTS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Funding Source Properties");
	}
}
