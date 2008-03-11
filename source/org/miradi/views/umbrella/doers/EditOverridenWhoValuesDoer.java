/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella.doers;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.diagram.OverridingResourcePanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Task;
import org.miradi.views.umbrella.AbstractEditListDoer;

public class EditOverridenWhoValuesDoer extends AbstractEditListDoer
{
	protected ObjectDataInputPanel getRelevancyPanel(ORef objectiveRef)
	{
		return new OverridingResourcePanel(getProject(), objectiveRef);
	}

	protected String getDialogTitle()
	{
		return EAM.text("Choose Resource(s)");
	}
	
	protected int getTypeToUse()
	{
		return Task.getObjectType();
	}
}
