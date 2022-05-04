/*
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
import org.miradi.schemas.AnalyticalQuestionSchema;
import org.miradi.schemas.AssumptionSchema;

public class Assumption extends AbstractAnalyticalQuestion
{
    public Assumption(ObjectManager objectManager, FactorId idToUse)
    {
        super(objectManager, idToUse, createSchema(objectManager));
    }

    public static AssumptionSchema createSchema(Project projectToUse)
    {
        return createSchema(projectToUse.getObjectManager());
    }

    public static AssumptionSchema createSchema(ObjectManager objectManager)
    {
        return (AssumptionSchema) objectManager.getSchemas().get(ObjectType.ASSUMPTION);
    }

    @Override
    public int[] getTypesThatCanOwnUs()
    {
        return new int[] {
                AnalyticalQuestionSchema.getObjectType(),
        };
    }

    @Override
	public boolean isAssumption()
	{
		return true;
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
        return objectType == AssumptionSchema.getObjectType();
    }

    public static Assumption find(ObjectManager objectManager, ORef assumptionRef)
    {
        return (Assumption) objectManager.findObject(assumptionRef);
    }

    public static Assumption find(Project project, ORef assumptionRef)
    {
        return find(project.getObjectManager(), assumptionRef);
    }
}
