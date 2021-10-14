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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.OutputSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;

public class Output extends BaseObject
{
    public Output(ObjectManager objectManager, BaseId idToUse, final BaseObjectSchema schema)
    {
        super(objectManager, idToUse, schema);
    }

    public Output(ObjectManager objectManager, BaseId idToUse)
    {
        this(objectManager, idToUse, createSchema(objectManager));
    }

    public static OutputSchema createSchema(Project projectToUse)
    {
        return createSchema(projectToUse.getObjectManager());
    }

    public static OutputSchema createSchema(ObjectManager objectManager)
    {
        return (OutputSchema) objectManager.getSchemas().get(ObjectType.OUTPUT);
    }

    @Override
    public int[] getTypesThatCanOwnUs()
    {
        return new int[] {
                StrategySchema.getObjectType(),
                TaskSchema.getObjectType(),
        };
    }

    public static boolean is(ORef ref)
    {
        return is(ref.getObjectType());
    }

    public static boolean is(int objectType)
    {
        return objectType == OutputSchema.getObjectType();
    }

    public static boolean is(BaseObject baseObject)
    {
        return is(baseObject.getType());
    }

    public static Output find(ObjectManager objectManager, ORef outputRef)
    {
        return (Output) objectManager.findObject(outputRef);
    }

    public static Output find(Project project, ORef outputRef)
    {
        return find(project.getObjectManager(), outputRef);
    }

    public String getDetails()
	{
		return getStringData(Factor.TAG_TEXT);
	}

	public static final String TAG_URL = "Url";
	public static final String TAG_DUE_DATE = "DueDate";
}
