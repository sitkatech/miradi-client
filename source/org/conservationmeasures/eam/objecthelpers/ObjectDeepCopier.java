/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.util.Vector;

import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;

public class ObjectDeepCopier
{
	public ObjectDeepCopier(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public Vector createDeepCopy(BaseObject objectToDeepCopy)
	{
		Vector allCopiedObjects = new Vector();
		allCopiedObjects = recursivelyCreateDeepCopy(allCopiedObjects, objectToDeepCopy);
		return allCopiedObjects;
	}
	
	public Vector recursivelyCreateDeepCopy(Vector vector, BaseObject objectToDeepCopy)
	{
		Vector allCopiedObjects = new Vector();
		ORefList ownedObjects = objectToDeepCopy.getOwnedObjects(objectToDeepCopy.getType());		
		
		for (int i = 0; i < ownedObjects.size(); ++i)
		{
			//FIXME nima finish 
		}
		
		return allCopiedObjects;
	}
	
	Project project;
}
