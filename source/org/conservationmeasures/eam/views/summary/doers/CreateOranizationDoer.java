/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary.doers;

import org.conservationmeasures.eam.objects.Organization;
import org.conservationmeasures.eam.views.planning.doers.CreatePoolObjectDoer;

public class CreateOranizationDoer extends CreatePoolObjectDoer
{
	@Override
	protected int getTypeToCreate()
	{
		return Organization.getObjectType();
	}
}
