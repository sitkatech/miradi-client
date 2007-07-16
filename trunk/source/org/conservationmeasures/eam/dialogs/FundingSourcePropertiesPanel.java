/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.FundingSource;
import org.conservationmeasures.eam.project.Project;

public class FundingSourcePropertiesPanel extends ObjectDataInputPanel
{
	public FundingSourcePropertiesPanel(Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.FUNDING_SOURCE, idToEdit);
		
		addField(createStringField(FundingSource.TAG_CODE));
		addField(createStringField(FundingSource.TAG_LABEL));
		addField(createMultilineField(FundingSource.TAG_COMMENTS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Funding Source Properties");
	}
}
