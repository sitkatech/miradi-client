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

import org.miradi.ids.FactorId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.ThreatReductionResultSchema;

public class ThreatReductionResult extends Factor
{
	public ThreatReductionResult(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, createSchema(objectManager));
	}

	public static ThreatReductionResultSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static ThreatReductionResultSchema createSchema(ObjectManager objectManager)
	{
		return (ThreatReductionResultSchema) objectManager.getSchemas().get(ObjectType.THREAT_REDUCTION_RESULT);
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	@Override
	public boolean isThreatReductionResult()
	{
		return true;
	}
	
	@Override
	public boolean canHaveObjectives()
	{
		return true;
	}
	
	public String getRelatedDirectThreatRefAsString()
	{
		return getData(TAG_RELATED_DIRECT_THREAT_REF);
	}
	
	public ORef getRelatedThreatRef()
	{
		if (getRelatedDirectThreatRefAsString().length() == 0)
			return ORef.INVALID;
		
		return ORef.createFromString(getRelatedDirectThreatRefAsString());
	}
	
	public static boolean is(BaseObject baseObject)
	{
		return is(baseObject.getType());
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == ThreatReductionResultSchema.getObjectType();
	}
	
	public static ThreatReductionResult find(ObjectManager objectManager, ORef threatReductionResultRef)
	{
		return (ThreatReductionResult) objectManager.findObject(threatReductionResultRef);
	}
	
	public static ThreatReductionResult find(Project project, ORef threatReductionResultRef)
	{
		return find(project.getObjectManager(), threatReductionResultRef);
	}
	
	public static final String TAG_RELATED_DIRECT_THREAT_REF = "RelatedDirectThreatRef";
}
