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
import org.miradi.ids.ObjectiveId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.*;


public class Objective extends Desire
{
	public Objective(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, new ObjectiveId(id.asInt()), createSchema(objectManager));
	}

	public static ObjectiveSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static ObjectiveSchema createSchema(ObjectManager objectManager)
	{
		return (ObjectiveSchema) objectManager.getSchemas().get(ObjectType.OBJECTIVE);
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return new int[] {
            StrategySchema.getObjectType(),
            CauseSchema.getObjectType(),
            IntermediateResultSchema.getObjectType(),
            ThreatReductionResultSchema.getObjectType(),
            BiophysicalFactorSchema.getObjectType(),
            BiophysicalResultSchema.getObjectType(),
        };
	}
	
	public static Objective find(ObjectManager objectManager, ORef objectiveRef)
	{
		return (Objective) objectManager.findObject(objectiveRef);
	}
	
	public static Objective find(Project project, ORef objectiveRef)
	{
		return find(project.getObjectManager(), objectiveRef);
	}
	
	public static boolean is(BaseObject baseObject)
	{
		return is(baseObject.getType());
	}
	
	public static boolean is(int nodeType)
	{
		return nodeType == ObjectiveSchema.getObjectType();
	}

	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
}
