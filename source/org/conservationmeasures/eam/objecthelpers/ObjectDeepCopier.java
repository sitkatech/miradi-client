/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.util.Vector;

import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

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
