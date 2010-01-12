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
package org.miradi.dialogs.base;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

abstract public class ObjectListTableModel extends ObjectTableModel
{
	public ObjectListTableModel(Project projectToUse, ORef containingRefToUse, String listFieldTag, int listedItemType, String[] columnTags)
	{
		this(projectToUse, new ORefList(containingRefToUse), listFieldTag, listedItemType, columnTags);
	}
	
	public ObjectListTableModel(Project projectToUse, ORefList containingRefsToUse, String listFieldTag, int listedItemType, String[] columnTags)
	{
		super(projectToUse, listedItemType, columnTags);
		
		tagOfList = listFieldTag;
		containingRefs = containingRefsToUse;
		setRowObjectRefs(getLatestRefListFromProject());
	}
	
	public ORefList getSelectedHierarchy()
	{
		return containingRefs;
	}

	public ORef getContainingRef()
	{
		return containingRefs.get(0);
	}
	
	public int getContainingObjectType()
	{
		return getContainingRef().getObjectType();
	}
	
	public BaseId getContainingObjectId()
	{
		return getContainingRef().getObjectId();
	}
	
	public String getFieldTag()
	{
		return tagOfList;
	}

	public BaseObject getContainingObject()
	{
		return getProject().findObject(getContainingRef());
	}

	public ORefList getLatestRefListFromProject()
	{
		try
		{
			if(getContainingRef().isInvalid())
				return new ORefList();
			
			return getContainingObject().getRefList(tagOfList);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new RuntimeException(e);
		}
	}
	
	private String tagOfList;
	private ORefList containingRefs;
}
