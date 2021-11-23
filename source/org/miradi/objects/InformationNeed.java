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
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.*;

public class InformationNeed extends BaseObject
{
    public InformationNeed(ObjectManager objectManager, BaseId idToUse, final BaseObjectSchema schema)
    {
        super(objectManager, idToUse, schema);
    }

    public InformationNeed(ObjectManager objectManager, BaseId idToUse)
    {
        this(objectManager, idToUse, createSchema(objectManager));
    }

    public static InformationNeedSchema createSchema(Project projectToUse)
    {
        return createSchema(projectToUse.getObjectManager());
    }

    public static InformationNeedSchema createSchema(ObjectManager objectManager)
    {
        return (InformationNeedSchema) objectManager.getSchemas().get(ObjectType.INFORMATION_NEED);
    }

    @Override
    public int[] getTypesThatCanOwnUs()
    {
        return new int[] {
                AssumptionSchema.getObjectType(),
        };
    }

    @Override
    public String toString()
    {
        return getLabel();
    }

    public static boolean is(ORef ref)
    {
        return is(ref.getObjectType());
    }

    public static boolean is(int objectType)
    {
        return objectType == InformationNeedSchema.getObjectType();
    }

    public static boolean is(BaseObject baseObject)
    {
        return is(baseObject.getType());
    }

    public static InformationNeed find(ObjectManager objectManager, ORef informationNeedRef)
    {
        return (InformationNeed) objectManager.findObject(informationNeedRef);
    }

    public static InformationNeed find(Project project, ORef informationNeedRef)
    {
        return find(project.getObjectManager(), informationNeedRef);
    }

    public String getDetails()
    {
        return getStringData(Factor.TAG_TEXT);
    }

    public static final String TAG_INDICATOR_IDS = "IndicatorIds";
}
