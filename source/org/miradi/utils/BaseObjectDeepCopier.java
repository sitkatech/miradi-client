/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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
	
	public BaseObject createDeepCopier(BaseObject baseObejctToClone) throws Exception
	{
		return createCopy(baseObejctToClone);
	}
	
	private BaseObject createCopy(BaseObject baseObejctToClone) throws Exception
	{
		ORef copiedBaseObjectRef = createBaseObject(baseObejctToClone);
		BaseObject copiedBaseObject = BaseObject.find(getProject(), copiedBaseObjectRef);
		copyBaseObject(baseObejctToClone, copiedBaseObject);

		return copiedBaseObject;
	}

	public void copyBaseObject(BaseObject baseObejctToClone, BaseObject copiedBaseObjectToFill) throws Exception
	{	
		Vector<String> storedTags = baseObejctToClone.getStoredFieldTags();
		for (String tag : storedTags)
		{
			final boolean isOwnedField = baseObejctToClone.isOwnedField(tag);
			String dataToBeSaved = "";
			if (baseObejctToClone.isRefList(tag) && isOwnedField)
			{
				dataToBeSaved = copyOwnedObjectRefs(baseObejctToClone.getSafeRefListData(tag)).toString();
			}
			else if (baseObejctToClone.isIdListTag(tag) && isOwnedField)
			{
				dataToBeSaved = copyOwnedObjectIds(baseObejctToClone.getSafeRefListData(tag));
			}
			else
			{
				dataToBeSaved = baseObejctToClone.getData(tag); 
			}
			
			setBaseObjectData(copiedBaseObjectToFill, tag, dataToBeSaved);
		}
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
	
	abstract protected ORef createBaseObject(BaseObject baseObejctToClone) throws Exception;
	
	abstract protected void setBaseObjectData(BaseObject copiedBaseObjectToFill, String tag, String dataToBeSaved) throws Exception;

	protected Project getProject()
	{
		return project;
	}
	
	private Project project;
}
