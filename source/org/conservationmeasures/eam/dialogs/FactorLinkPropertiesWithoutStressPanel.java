package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.Project;

public class FactorLinkPropertiesWithoutStressPanel extends ObjectDataInputPanel
{
	public FactorLinkPropertiesWithoutStressPanel(Project projectToUse, FactorLinkId objectIdToUse)
	{
		super(projectToUse, ObjectType.FACTOR_LINK, objectIdToUse);

		addField(createCheckBoxField(FactorLink.TAG_BI_DRECTIONAL_LINK,"Y","N"));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Link Properties");
	}
}

