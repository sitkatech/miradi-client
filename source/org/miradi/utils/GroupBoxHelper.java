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

package org.miradi.utils;

import java.util.Vector;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.project.Project;

public class GroupBoxHelper
{
	public GroupBoxHelper(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void setGroupBoxTagsToMatchChildren() throws Exception
	{
		Vector<TaggedObjectSet> allTaggedObjectSets = getProject().getTaggedObjectSetPool().getAllTaggedObjectSets();
		for(TaggedObjectSet taggedObjectSet : allTaggedObjectSets)
		{
			ORefSet taggedObjectRefs = taggedObjectSet.getTaggedObjectRefsSet();
			ORefSet groupBoxesToTag = findAllContainingGroupBoxes(taggedObjectRefs);
			if (groupBoxesToTag.hasData())
			{
				tagGroupBoxes(taggedObjectSet, groupBoxesToTag);
			}
		}
	}

	private void tagGroupBoxes(TaggedObjectSet taggedObjectSet, ORefSet groupBoxesToTag) throws Exception, CommandFailedException
	{
		ORefSet taggedObjectRefs = taggedObjectSet.getTaggedObjectRefsSet();
		taggedObjectRefs.addAll(groupBoxesToTag);
		CommandSetObjectData tagGroupBoxes = new CommandSetObjectData(taggedObjectSet, TaggedObjectSet.TAG_TAGGED_OBJECT_REFS, taggedObjectRefs);
		getProject().executeCommand(tagGroupBoxes);
	}
	
	private ORefSet findAllContainingGroupBoxes(ORefSet taggedFactorRefs)
	{	
		ORefList diagramFactors = getProject().getDiagramFactorPool().getRefList();
		ORefSet groupBoxesToTag = new ORefSet();
		for(int index = 0; index < diagramFactors.size(); ++index)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(getProject(), diagramFactors.get(index));
			if (diagramFactor.isGroupBoxFactor())
			{
				ORefSet diagramFactorChildrenRefs = diagramFactor.getGroupBoxChildrenSet();
				ORefSet wrappedRefs = getWrappedFactorRefs(diagramFactorChildrenRefs);
				if (taggedFactorRefs.containsAny(wrappedRefs))
				{
					groupBoxesToTag.add(diagramFactor.getWrappedORef());
				}
			}
		}
		
		return groupBoxesToTag;
	}

	private ORefSet getWrappedFactorRefs(ORefSet diagramFactorChildren)
	{
		ORefSet wrappedRefs = new ORefSet();
		for(ORef diagramFactorRef : diagramFactorChildren)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(getProject(), diagramFactorRef);
			wrappedRefs.add(diagramFactor.getWrappedORef());
		}

		return wrappedRefs;
	}

	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
