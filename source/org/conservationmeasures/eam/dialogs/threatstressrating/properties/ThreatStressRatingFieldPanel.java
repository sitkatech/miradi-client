/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.properties;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;

public class ThreatStressRatingFieldPanel extends ObjectDataInputPanel
{
	public ThreatStressRatingFieldPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		
		addField(createReadonlyTextField(Cause.getObjectType(), Cause.TAG_LABEL));
		addField(createReadonlyTextField(Target.getObjectType(), Target.TAG_LABEL));
		addField(createMultilineField(FactorLink.getObjectType(), FactorLink.TAG_COMMENT));
		addField(createReadonlyTextField(FactorLink.getObjectType(), FactorLink.PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE, 20));

		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return "ThreatStressRatingFieldPanel";
	}
}
