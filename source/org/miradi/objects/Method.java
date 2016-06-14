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

import org.miradi.ids.MethodId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.MethodSchema;

public class Method extends BaseObject
{
	public Method(final ObjectManager objectManager, final MethodId idToUse)
	{
		this(objectManager, idToUse, createSchema(objectManager));
	}

	protected Method(ObjectManager objectManager, MethodId idToUse, MethodSchema methodSchema)
	{
		super(objectManager, idToUse, methodSchema);
	}

	public static MethodSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static MethodSchema createSchema(ObjectManager objectManager)
	{
		return (MethodSchema) objectManager.getSchemas().get(ObjectType.METHOD);
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return new int[] {
			IndicatorSchema.getObjectType(),
		};
	}
	
	@Override
	public String getShortLabel()
	{
		return getData(TAG_SHORT_LABEL);
	}
	
	@Override
	public String toString()
	{
		if(getId().isInvalid())
			return "(None)";
		return combineShortLabelAndLabel(getShortLabel(), getLabel());
	}
	
	public static boolean is(BaseObject object)
	{
		if(object == null)
			return false;
		return is(object.getRef());
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == MethodSchema.getObjectType();
	}
	
	public static Method find(ObjectManager objectManager, ORef methodRef)
	{
		return (Method) objectManager.findObject(methodRef);
	}
	
	public static Method find(Project project, ORef methodRef)
	{
		return find(project.getObjectManager(), methodRef);
	}
	
	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_DETAILS = "Details";
	public static final String TAG_COMMENTS = "Comments";
}
