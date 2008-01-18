/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.diagram.RelevancyIndicatorPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;

public class EditIndicatorRelevancyListDoer extends AbstractEditRelevancyListDoer
{
	protected ObjectDataInputPanel getRelevancyPanel(ORef objectiveRef)
	{
		return new RelevancyIndicatorPanel(getProject(), objectiveRef);
	}

	protected String getDialogTitle()
	{
		return EAM.text("Choose Relevant Indicator(s)");
	}
}
