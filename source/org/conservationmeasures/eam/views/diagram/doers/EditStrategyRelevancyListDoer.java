/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.diagram.RelevancyStrategyPanel;
import org.conservationmeasures.eam.objecthelpers.ORef;

public class EditStrategyRelevancyListDoer extends AbstractEditRelevancyListDoer
{

	protected ObjectDataInputPanel getRelevancyPanel(ORef objectiveRef)
	{
		return new RelevancyStrategyPanel(getProject(), objectiveRef);
	}
}
