/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.planning.doers;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Strategy;
import org.miradi.views.diagram.doers.ShareActivityDoer;

public class TreeNodeShareActivityDoer extends ShareActivityDoer
{
	@Override
	public boolean isAvailable()
	{
		return getSingleSelected(Strategy.getObjectType()) != null;
	}
	
	protected ORef getParentRefOfShareableObjects()
	{
		BaseObject foundObject = getSingleSelected(Strategy.getObjectType());
		if (foundObject == null)
			return ORef.INVALID;
		
		return foundObject.getRef();
	}
}
