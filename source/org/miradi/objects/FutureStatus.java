/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.FutureStatusSchema;
import org.miradi.schemas.IndicatorSchema;

public class FutureStatus extends BaseObject
{
	public FutureStatus(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id, createSchema());
	}

	public static FutureStatusSchema createSchema()
	{
		return new FutureStatusSchema();
	}
	
	@Override
	public String getFullName()
	{
		return getData(FutureStatusSchema.TAG_FUTURE_STATUS_DATE);
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return new int[] {
				IndicatorSchema.getObjectType(),
				};
	}
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	public static boolean is(BaseObject baseObject)
	{
		return is(baseObject.getType());
	}
	
	public static boolean is(ORef parentRef)
	{
		return is(parentRef.getObjectType());
	}
	
	public static boolean is(final int onbjectType)
	{
		return FutureStatusSchema.getObjectType() == onbjectType;
	}
	
	public static FutureStatus find(ObjectManager objectManager, ORef futureStatusRef)
	{
		return (FutureStatus) objectManager.findObject(futureStatusRef);
	}
	
	public static FutureStatus find(Project project, ORef futureStatusRef)
	{
		return find(project.getObjectManager(), futureStatusRef);
	}
	
	@Override
	public String toString()
	{
		return toString(EAM.text("Label|(Future Status)"));
	}
}
