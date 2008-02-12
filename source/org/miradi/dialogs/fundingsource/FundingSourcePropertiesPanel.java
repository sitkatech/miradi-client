/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.fundingsource;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.FundingSource;
import org.miradi.project.Project;

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
