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
package org.miradi.objectpools;

import java.util.Vector;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.project.ObjectManager;

public class TaggedObjectSetPool extends EAMNormalObjectPool
{
	public TaggedObjectSetPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.TAGGED_OBJECT_SET);
	}
	
	public void put(TaggedObjectSet taggedObjectSet) throws Exception
	{
		put(taggedObjectSet.getId(), taggedObjectSet);
	}
	
	public TaggedObjectSet find(BaseId id)
	{
		return (TaggedObjectSet) getRawObject(id);
	}
	
	public Vector<TaggedObjectSet> getAllTaggedObjectSets()
	{
		ORefList allTaggedObjectSetRefs = getORefList();
		Vector<TaggedObjectSet> allTaggedObjectSets = new Vector();
		for (int index = 0; index < allTaggedObjectSetRefs.size(); ++index)
		{
			TaggedObjectSet taggedObjectSet = (TaggedObjectSet) findObject(allTaggedObjectSetRefs.get(index));
			allTaggedObjectSets.add(taggedObjectSet);
		}
			
		return allTaggedObjectSets;
	}
	
	public Vector<TaggedObjectSet> findTaggedObjectSetsWithFactor(ORef factorRef) throws Exception
	{
		Vector<TaggedObjectSet> taggedObjectSetsWithFactor = new Vector();
		ORefList taggedObjectRefs = getORefList();
		for (int index = 0; index < taggedObjectRefs.size(); ++index)
		{
			TaggedObjectSet taggedObjectSet = find(taggedObjectRefs.get(index).getObjectId());
			if (taggedObjectSet.getTaggedObjectRefs().contains(factorRef))
				taggedObjectSetsWithFactor.add(taggedObjectSet);
		}
		
		return taggedObjectSetsWithFactor;
	}


	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo)
	{
		return new TaggedObjectSet(objectManager, actualId);
	}
}
