/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import org.miradi.objects.ProjectResource;
import org.miradi.views.planning.doers.CreatePoolObjectDoer;

public class CreateResource extends CreatePoolObjectDoer
{
	@Override
	protected int getTypeToCreate()
	{
		return ProjectResource.getObjectType();
	}
}
