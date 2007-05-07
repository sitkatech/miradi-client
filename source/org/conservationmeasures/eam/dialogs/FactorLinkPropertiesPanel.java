/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.Project;

public class FactorLinkPropertiesPanel extends ObjectDataInputPanel
{
	public FactorLinkPropertiesPanel(Project projectToUse, DiagramFactorLink link)
	{
		super(projectToUse, ObjectType.FACTOR_LINK, link.getWrappedId());

		if (link.isTargetLink())
			addField(createStringField(FactorLink.TAG_STRESS_LABEL));
		addField(createCheckBoxField(FactorLink.TAG_BI_DRECTIONAL_LINK, Boolean.toString(true), Boolean.toString(false)));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Link Properties");
	}
}
