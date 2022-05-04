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

import org.miradi.ids.IdList;
import org.miradi.ids.KeyEcologicalAttributeId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.project.KEAViabilityFormula;
import org.miradi.schemas.*;
import org.miradi.utils.CodeList;

import java.util.function.Function;

public class KeyEcologicalAttribute extends BaseObject
{
	public KeyEcologicalAttribute(ObjectManager objectManager, KeyEcologicalAttributeId idToUse)
	{
		super(objectManager, idToUse, createSchema(objectManager));
	}

	public static KeyEcologicalAttributeSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static KeyEcologicalAttributeSchema createSchema(ObjectManager objectManager)
	{
		return (KeyEcologicalAttributeSchema) objectManager.getSchemas().get(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE);
	}

	@Override
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_INDICATOR_IDS))
			return IndicatorSchema.getObjectType();
		
		return super.getAnnotationType(tag);
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return new int[] {
			TargetSchema.getObjectType(),
			HumanWelfareTargetSchema.getObjectType(),
			};
	}
	
	public ORefList getIndicatorRefs()
	{
		return getSafeRefListData(TAG_INDICATOR_IDS);
	}
	
	public IdList getIndicatorIds()
	{
		return getSafeIdListData(TAG_INDICATOR_IDS);
	}
	
	public String getKeyEcologicalAttributeType()
	{
		return getData(TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE);
	}

	@Override
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_VIABILITY_STATUS))
			return computeViability();
		if(fieldTag.equals(PSEUDO_TAG_VIABILITY_FUTURE_STATUS))
			return computeFutureViability();
		return super.getPseudoData(fieldTag);
	}
	
	public String computeViability()
	{
		return computeIndicatorViability(Indicator::getCurrentStatus);
	}

	public String computeFutureViability()
	{
		return computeIndicatorViability(Indicator::getFutureStatus);
	}

	private String computeIndicatorViability(Function<Indicator, String> indicatorStatusFn)
	{
		CodeList statuses = new CodeList();
		IdList indicatorIds = getIndicatorIds();
		for(int i = 0; i < indicatorIds.size(); ++i)
		{
			Indicator indicator = (Indicator) objectManager.findObject(new ORef(IndicatorSchema.getObjectType(), indicatorIds.get(i)));
			String status = indicatorStatusFn.apply(indicator);
			statuses.add(status);
		}
		return KEAViabilityFormula.getAverageRatingCode(statuses);
	}

	public boolean isActive()
	{
		ORef targetRef = getOwnerRef();
		AbstractTarget target = AbstractTarget.findTarget(getProject(), targetRef);
		return target.isViabilityModeKEA();
	}

	@Override
	public String getShortLabel()
	{
		return getStringData(TAG_SHORT_LABEL);
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
		return objectType == KeyEcologicalAttributeSchema.getObjectType();
	}
	
	public static KeyEcologicalAttribute find(ObjectManager objectManager, ORef keaRef)
	{
		return (KeyEcologicalAttribute) objectManager.findObject(keaRef);
	}
	
	public static KeyEcologicalAttribute find(Project project, ORef keaRef)
	{
		return find(project.getObjectManager(), keaRef);
	}
	
	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_INDICATOR_IDS = "IndicatorIds";
	public static final String TAG_DESCRIPTION = "Description";
	public static final String TAG_DETAILS = "Details";
	public static final String TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE = "KeyEcologicalAttributeType";
	public static final String PSEUDO_TAG_VIABILITY_STATUS = "ViabilityStatus";
	public static final String PSEUDO_TAG_VIABILITY_FUTURE_STATUS = "ViabilityFutureStatus";
}
