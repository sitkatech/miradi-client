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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.RatingCriterionSchema;
import org.miradi.utils.EnhancedJsonObject;

public class RatingCriterion extends BaseObject
{
	public RatingCriterion(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse, createSchema(objectManager));
	}

	public static RatingCriterionSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static RatingCriterionSchema createSchema(ObjectManager objectManager)
	{
		return (RatingCriterionSchema) objectManager.getSchemas().get(ObjectType.RATING_CRITERION);
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	@Override
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = super.toJson();

		return json;
	}
	
	public static boolean is(final int onbjectType)
	{
		return RatingCriterionSchema.getObjectType() == onbjectType;
	}
}
