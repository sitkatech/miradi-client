/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.planning.doers;

import org.miradi.main.EAM;
import org.miradi.views.umbrella.doers.DeletePoolObjectDoer;

public class DeleteFundingSourceDoer extends DeletePoolObjectDoer
{
	@Override
	protected String getCustomText()
	{
		return EAM.text("Funding Source");
	}
}

