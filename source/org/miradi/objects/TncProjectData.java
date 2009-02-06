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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.utils.EnhancedJsonObject;

public class TncProjectData extends BaseObject
{
	public TncProjectData(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id);
		clear();
	}
	
	public TncProjectData(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new BaseId(idAsInt), jsonObject);
	}
	
	public int getType()
	{
		return getObjectType();
	}

	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.TNC_PROJECT_DATA;
	}
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	void clear()
	{
		super.clear();
		
		organizationalPriority = new StringData(TAG_ORGANIZATIONAL_PRIORITY);
		projectSharingCode = new StringData(TAG_PROJECT_SHARING_CODE);
		
		addField(organizationalPriority);
		addField(projectSharingCode);
	}
	
	public static final String OBJECT_NAME = "TncProjectData";

	public final static String TAG_ORGANIZATIONAL_PRIORITY = "OrganizationalPriority";
	public final static String TAG_PROJECT_SHARING_CODE = "ProjectSharingCode";
	
	public StringData organizationalPriority;
	public StringData projectSharingCode;

}
