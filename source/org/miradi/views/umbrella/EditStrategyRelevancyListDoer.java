/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.diagram.RelevancyStrategyPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;

public class EditStrategyRelevancyListDoer extends AbstractEditRelevancyListDoer
{

	protected ObjectDataInputPanel getRelevancyPanel(ORef objectiveRef)
	{
		return new RelevancyStrategyPanel(getProject(), objectiveRef);
	}
	
	protected String getDialogTitle()
	{
		return EAM.text("Choose Relevant Strategy(s)");
	}
}
