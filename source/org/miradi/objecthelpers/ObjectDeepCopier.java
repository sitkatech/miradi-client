/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.objecthelpers;

import java.util.Vector;

import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;

public class ObjectDeepCopier
{
	public ObjectDeepCopier(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public Vector createDeepCopy(BaseObject objectToDeepCopy)
	{
		clear();
		recursivelyCreateDeepCopy(objectToDeepCopy);
		return allOwnedObjects;
	}

	private void clear()
	{
		allOwnedObjects = new Vector();
	}
	
	private void recursivelyCreateDeepCopy(BaseObject objectToDeepCopy)
	{
		if (objectToDeepCopy == null)
			return;
		
		ORefList objectsToDeepCopy = objectToDeepCopy.getAllObjectsToDeepCopy();		
		EnhancedJsonObject json = getJsonWithType(objectToDeepCopy);
		allOwnedObjects.add(json.toString());
		for (int i = 0; i < objectsToDeepCopy.size(); ++i)
		{
			ORef objectRef = objectsToDeepCopy.get(i);
			BaseObject thisObjectToDeepCopy = project.findObject(objectRef);
			recursivelyCreateDeepCopy(thisObjectToDeepCopy);
		}
	}

	private EnhancedJsonObject getJsonWithType(BaseObject objectToDeepCopy)
	{
		EnhancedJsonObject jsonWithType = objectToDeepCopy.toJson();
		jsonWithType.put("Type", objectToDeepCopy.getType());
		
		return jsonWithType;
	}
	
	private Vector allOwnedObjects;
	private Project project;
}
