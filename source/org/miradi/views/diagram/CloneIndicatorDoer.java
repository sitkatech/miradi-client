/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.dialogs.indicator.IndicatorPoolTablePanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;

public class CloneIndicatorDoer extends CreateIndicator
{
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;

		if (validUserChoiceForObjectToClone(new IndicatorPoolTablePanel(getProject()), EAM.text("Choose Indicator to Clone")))
			super.doIt();
	}
}