/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.properties;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.objectdata.BooleanData;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ThreatRatingQuestion;

public class ThreatStressRatingFieldPanel extends ObjectDataInputPanel
{
	public ThreatStressRatingFieldPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		
		addField(createReadonlyTextField(Target.getObjectType(), Target.TAG_LABEL));
		addField(createReadonlyTextField(Cause.getObjectType(), Cause.TAG_LABEL));
		addField(createCheckBoxField(FactorLink.getObjectType(), FactorLink.TAG_BIDIRECTIONAL_LINK, BooleanData.BOOLEAN_TRUE, BooleanData.BOOLEAN_FALSE));
		addField(createMultilineField(FactorLink.getObjectType(), FactorLink.TAG_COMMENT));
		addField(createReadOnlyChoiceField(FactorLink.getObjectType(), new ThreatRatingQuestion(FactorLink.PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE)));

		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return "ThreatStressRatingFieldPanel";
	}
}
