/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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
package org.miradi.objects;

import org.miradi.ids.FactorId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.ScopeBoxSchema;

public class ScopeBox extends Factor
{
	public ScopeBox(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, createSchema(objectManager));
	}

	public static ScopeBoxSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static ScopeBoxSchema createSchema(ObjectManager objectManager)
	{
		return (ScopeBoxSchema) objectManager.getSchemas().get(ObjectType.SCOPE_BOX);
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	@Override
	public boolean isScopeBox()
	{
		return true;
	}

	@Override
	public boolean canDirectlyOwnIndicators()
	{
		return false;
	}

	public String getScopeBoxTypeCode()
	{
		return getData(TAG_SCOPE_BOX_TYPE_CODE);
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == ScopeBoxSchema.getObjectType();
	}
	
	public static ScopeBox find(ObjectManager objectManager, ORef objectRef)
	{
		return (ScopeBox) objectManager.findObject(objectRef);
	}
	
	public static ScopeBox find(Project project, ORef objectRef)
	{
		return find(project.getObjectManager(), objectRef);
	}
	
	public static final String TAG_SCOPE_BOX_TYPE_CODE = "ScopeBoxColorCode";
}
