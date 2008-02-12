/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary.doers;

import org.miradi.objects.Organization;
import org.miradi.views.planning.doers.CreatePoolObjectDoer;

public class CreateOranizationDoer extends CreatePoolObjectDoer
{
	@Override
	protected int getTypeToCreate()
	{
		return Organization.getObjectType();
	}
}
