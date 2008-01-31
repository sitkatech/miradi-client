/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning.doers;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.doers.DeletePoolObjectDoer;

public class DeleteFundingSourceDoer extends DeletePoolObjectDoer
{
	@Override
	protected String getCustomText()
	{
		return EAM.text("Funding Source");
	}
}

