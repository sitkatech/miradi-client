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

import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.views.diagram.DiagramPaster;

public class ObjectDeepCopier
{
	public ObjectDeepCopier(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public Vector<String> createDeepCopy(BaseObject objectToDeepCopy)
	{
		return createDeepCopy(new EAMGraphCell[0], objectToDeepCopy);
	}
	
	public Vector<String> createDeepCopy(EAMGraphCell[] selectedCellsToCopy, BaseObject objectToDeepCopy)
	{
		clear();
		recursivelyCreateDeepCopy(selectedCellsToCopy, objectToDeepCopy);
		return allOwnedObjects;
	}

	private void clear()
	{
		allOwnedObjects = new Vector();
	}
	
	private void recursivelyCreateDeepCopy(EAMGraphCell[] selectedCellsToCopy, BaseObject objectToDeepCopy)
	{
		if (objectToDeepCopy == null)
			return;
		
		ORefList objectsToDeepCopy = objectToDeepCopy.getAllObjectsToDeepCopy(selectedCellsToCopy);		
		EnhancedJsonObject customJson = getCustomJson(objectToDeepCopy);
		allOwnedObjects.add(customJson.toString());
		for (int i = 0; i < objectsToDeepCopy.size(); ++i)
		{
			ORef objectRef = objectsToDeepCopy.get(i);
			BaseObject thisObjectToDeepCopy = getProject().findObject(objectRef);
			recursivelyCreateDeepCopy(selectedCellsToCopy, thisObjectToDeepCopy);
		}
	}

	private EnhancedJsonObject getCustomJson(BaseObject objectToDeepCopy)
	{
		EnhancedJsonObject customJson = objectToDeepCopy.toJson();
		customJson.put(DiagramPaster.FAKE_TAG_TYPE, objectToDeepCopy.getType());
		customJson.put(DiagramPaster.FAKE_TAG_TAG_NAMES, getTagNames(objectToDeepCopy).toString());
		
		return customJson;
	}
	
	private CodeList getTagNames(BaseObject objectToDeepCopy)
	{
		CodeList tagNames = new CodeList();
		ORefList referringTaggedObjectSetRefs = objectToDeepCopy.findObjectsThatReferToUs(TaggedObjectSet.getObjectType());
		for (int index = 0; index < referringTaggedObjectSetRefs.size(); ++index)
		{
			ORef taggedObjectSetRef = referringTaggedObjectSetRefs.get(index);
			TaggedObjectSet taggedObjectSet = TaggedObjectSet.find(getProject(), taggedObjectSetRef);
			tagNames.add(taggedObjectSet.getLabel());
		}
		
		return tagNames;
	}
	
	private Project getProject()
	{
		return project;
	}

	private Vector<String> allOwnedObjects;
	private Project project;
}
