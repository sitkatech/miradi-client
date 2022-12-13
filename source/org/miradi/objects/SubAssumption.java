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
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.AssumptionSchema;
import org.miradi.schemas.SubAssumptionSchema;

public class SubAssumption extends AbstractAssumption
{
    public SubAssumption(ObjectManager objectManager, FactorId idToUse)
    {
        super(objectManager, idToUse, createSchema(objectManager));
    }

    public static SubAssumptionSchema createSchema(Project projectToUse)
    {
        return createSchema(projectToUse.getObjectManager());
    }

    public static SubAssumptionSchema createSchema(ObjectManager objectManager)
    {
        return (SubAssumptionSchema) objectManager.getSchemas().get(ObjectType.SUB_ASSUMPTION);
    }

    @Override
    public int[] getTypesThatCanOwnUs()
    {
        return new int[] {
                AssumptionSchema.getObjectType(),
        };
    }

	public boolean hasReferrers()
	{
		boolean isSuperShared = super.hasReferrers();
		if (isSuperShared)
			return true;

		ORefList referrers = findObjectsThatReferToUs(AssumptionSchema.getObjectType());

		return referrers.size() > 0;
	}

    @Override
	public boolean isSubAssumption()
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
        return objectType == SubAssumptionSchema.getObjectType();
    }

    public static SubAssumption find(ObjectManager objectManager, ORef subAssumptionRef)
    {
        return (SubAssumption) objectManager.findObject(subAssumptionRef);
    }

    public static SubAssumption find(Project project, ORef subAssumptionRef)
    {
        return find(project.getObjectManager(), subAssumptionRef);
    }
}
