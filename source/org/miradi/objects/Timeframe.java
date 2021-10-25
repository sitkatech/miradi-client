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
import org.miradi.ids.TimeframeId;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.TimeframeSchema;
import org.miradi.utils.OptionalDouble;

public class Timeframe extends AbstractPlanningObject
{
	public Timeframe(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, new TimeframeId(idToUse.asInt()), createSchema(objectManager));
	}

	public static TimeframeSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static TimeframeSchema createSchema(ObjectManager objectManager)
	{
		return (TimeframeSchema) objectManager.getSchemas().get(ObjectType.TIMEFRAME);
	}

	@Override
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_TAG_OWNING_FACTOR_NAME))
			return getOwningFactorName();

		return super.getPseudoData(fieldTag);
	}

	private String getOwningFactorName()
	{
		Factor owningFactor = getDirectOrIndirectOwningFactor();
		if (owningFactor == null)
			return "";

		return owningFactor.toString();
	}

	@Override
	protected TimePeriodCosts createTimePeriodCosts(OptionalDouble quantity)
	{
		return new TimePeriodCosts();
	}

	@Override
	protected boolean shouldIncludeEffort(DateUnit dateUnit) throws Exception
	{
		return true;
	}

	@Override
	public String toString()
	{
		return getLabel();
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
		return objectType == TimeframeSchema.getObjectType();
	}
	
	public static Timeframe find(ObjectManager objectManager, ORef timeframeRef)
	{
		return (Timeframe) objectManager.findObject(timeframeRef);
	}
	
	public static Timeframe find(Project project, ORef timeframeRef)
	{
		return find(project.getObjectManager(), timeframeRef);
	}

	public static final String PSEUDO_TAG_OWNING_FACTOR_NAME = "PseudoTagOwningFactorName";
}
