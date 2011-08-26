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
package org.miradi.views.diagram.doers;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Stress;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.views.diagram.CreateAnnotationDoer;

public class CreateStressDoer extends CreateAnnotationDoer
{
	@Override
	public int getAnnotationType()
	{
		return Stress.getObjectType();
	}
	@Override
	public String getAnnotationListTag()
	{
		return Target.TAG_STRESS_REFS;
	}
	
	@Override
	protected void doExtraWork(ORef newlyCreatedObjectRef) throws Exception
	{
		super.doExtraWork(newlyCreatedObjectRef);
		
		includeInAllItsOwnersTag(getProject(), newlyCreatedObjectRef);
	}
	
	public static void includeInAllItsOwnersTag(Project projectToUse, ORef newlyCreatedObjectRef) throws Exception
	{
		BaseObject baseObject = BaseObject.find(projectToUse, newlyCreatedObjectRef);
		ORef ownerRef = baseObject.getOwnerRef();
		
		ORefSet allTaggedObjectSetRefs = projectToUse.getTaggedObjectSetPool().getRefSet();
		for(ORef taggedObjectSetRef : allTaggedObjectSetRefs)
		{
			TaggedObjectSet taggedObjectSet = TaggedObjectSet.find(projectToUse, taggedObjectSetRef);
			ORefList taggedObjectRefs = new ORefList(taggedObjectSet.getTaggedObjectRefs());
			if (taggedObjectRefs.contains(ownerRef))
			{	
				taggedObjectRefs.add(baseObject);
				CommandSetObjectData tagCommand = new CommandSetObjectData(taggedObjectSet, TaggedObjectSet.TAG_TAGGED_OBJECT_REFS, taggedObjectRefs.toString());
				projectToUse.executeCommand(tagCommand);
			}
		}
	}
}
