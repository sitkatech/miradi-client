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
import org.miradi.schemas.HumanWelfareTargetSchema;

public class HumanWelfareTarget extends AbstractTarget
{
	public HumanWelfareTarget(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, createSchema(objectManager));
	}

	public static HumanWelfareTargetSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static HumanWelfareTargetSchema createSchema(ObjectManager objectManager)
	{
		return (HumanWelfareTargetSchema) objectManager.getSchemas().get(ObjectType.HUMAN_WELFARE_TARGET);
	}

	@Override
	public boolean isHumanWelfareTarget()
	{
		return true;
	}
	
	public static HumanWelfareTarget find(ObjectManager objectManager, ORef humanWelfareTargetRef)
	{
		return (HumanWelfareTarget) objectManager.findObject(humanWelfareTargetRef);
	}
	
	public static HumanWelfareTarget find(Project project, ORef humanWelfareTargetRef)
	{
		return find(project.getObjectManager(), humanWelfareTargetRef);
	}

	public static boolean is(BaseObject baseObject)
	{
		return is(baseObject.getType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == HumanWelfareTargetSchema.getObjectType();
	}
		
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}	
}
