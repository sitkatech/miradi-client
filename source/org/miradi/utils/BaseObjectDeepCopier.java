/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

abstract public class BaseObjectDeepCopier
{
	public BaseObjectDeepCopier(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public BaseObject createDeepCopier(BaseObject baseObjectToClone) throws Exception
	{
		return createCopy(baseObjectToClone);
	}
	
	private BaseObject createCopy(BaseObject baseObjectToClone) throws Exception
	{
		ORef copiedBaseObjectRef = createBaseObject(baseObjectToClone);
		BaseObject copiedBaseObject = BaseObject.find(getProject(), copiedBaseObjectRef);
		copyBaseObject(baseObjectToClone, copiedBaseObject);

		return copiedBaseObject;
	}

	public void copyBaseObject(BaseObject baseObjectToClone, BaseObject copiedBaseObjectToFill) throws Exception
	{	
		Vector<String> tagsToCopy = getFieldTagsToCopy(baseObjectToClone);
		for (String tag : tagsToCopy)
		{
			final boolean isOwnedField = baseObjectToClone.isOwnedField(tag);
			String dataToBeSaved = "";
			if (baseObjectToClone.isRefList(tag) && isOwnedField)
			{
				dataToBeSaved = copyOwnedObjectRefs(baseObjectToClone.getSafeRefListData(tag)).toString();
			}
			else if (baseObjectToClone.isIdListTag(tag) && isOwnedField)
			{
				dataToBeSaved = copyOwnedObjectIds(baseObjectToClone.getSafeRefListData(tag));
			}
			else
			{
				dataToBeSaved = baseObjectToClone.getData(tag);
			}
			
			setBaseObjectData(copiedBaseObjectToFill, tag, dataToBeSaved);
		}
	}

	private Vector<String> getFieldTagsToCopy(BaseObject baseObjectToClone)
	{
		Vector<String> tagsToCopy = baseObjectToClone.getStoredFieldTags();
		Vector<String> tagsToSkip = baseObjectToClone.getFieldTagsToSkipOnCopy();
		tagsToCopy.removeAll(tagsToSkip);
		return tagsToCopy;
	}

	private String copyOwnedObjectIds(final ORefList baseObjectRefsToCopy) throws Exception
	{
		if (baseObjectRefsToCopy.isEmpty())
			return "";
		
		final ORefList copiedReflist = copyOwnedObjectRefs(baseObjectRefsToCopy);
		final int objectTypeInRefList = copiedReflist.getFirstElement().getObjectType();
		
		return copiedReflist.convertToIdList(objectTypeInRefList).toString();
	}

	private ORefList copyOwnedObjectRefs(ORefList baseObjectRefsToCopy) throws Exception
	{
		ORefList copiedRefs = new ORefList();
		for (ORef ref : baseObjectRefsToCopy)
		{
			BaseObject baseObjectToCopy = BaseObject.find(getProject(), ref);
			BaseObject copiedBaseObject = createCopy(baseObjectToCopy);
			copiedRefs.add(copiedBaseObject);
		}
		
		return copiedRefs;
	}
	
	abstract protected ORef createBaseObject(BaseObject baseObjectToClone) throws Exception;
	
	abstract protected void setBaseObjectData(BaseObject copiedBaseObjectToFill, String tag, String dataToBeSaved) throws Exception;

	protected Project getProject()
	{
		return project;
	}
	
	private Project project;
}
