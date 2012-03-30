/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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
import org.miradi.ids.IdList;
import org.miradi.ids.KeyEcologicalAttributeId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.project.TNCViabilityFormula;
import org.miradi.schemas.HumanWelfareTargetSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.KeyEcologicalAttributeSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;

public class KeyEcologicalAttribute extends BaseObject
{
	public KeyEcologicalAttribute(ObjectManager objectManager, KeyEcologicalAttributeId idToUse)
	{
		super(objectManager, idToUse, new KeyEcologicalAttributeSchema());
	}

	public KeyEcologicalAttribute(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json, new KeyEcologicalAttributeSchema());
	}
	
	@Override
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_INDICATOR_IDS))
			return IndicatorSchema.getObjectType();
		
		return super.getAnnotationType(tag);
	}

	@Override
	public boolean isIdListTag(String tag)
	{
		if (tag.equals(TAG_INDICATOR_IDS))
			return true;
		
		return super.isIdListTag(tag);
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}

	@Override
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return new int[] {
			Target.getObjectType(),
			HumanWelfareTargetSchema.getObjectType(),
			};
	}
	
	public static int getObjectType()
	{
		return ObjectType.KEY_ECOLOGICAL_ATTRIBUTE;
	}
	
	@Override
	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = super.getOwnedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.INDICATOR: 
				list.addAll(new ORefList(objectType, getIndicatorIds()));
				break;
		}
		return list;
	}
	
	public ORefList getIndicatorRefs()
	{
		return getRefListData(TAG_INDICATOR_IDS);
	}
	
	public IdList getIndicatorIds()
	{
		return getIdListData(TAG_INDICATOR_IDS);
	}
	
	public String getKeyEcologicalAttributeType()
	{
		return getData(TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE);
	}
	
	@Override
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_VIABILITY_STATUS))
			return computeTNCViability();
		return super.getPseudoData(fieldTag);
	}
	
	public String computeTNCViability()
	{
		CodeList statuses = new CodeList();
		IdList indicatorIds = getIndicatorIds();
		for(int i = 0; i < indicatorIds.size(); ++i)
		{
			Indicator indicator = (Indicator) objectManager.findObject(new ORef(IndicatorSchema.getObjectType(), indicatorIds.get(i)));
			ORef latestMeasurementRef = indicator.getLatestMeasurementRef();
			if (latestMeasurementRef.isInvalid())
				continue;
			
			String status = objectManager.getObjectData(latestMeasurementRef, Measurement.TAG_STATUS);
			statuses.add(status);
		}
		return TNCViabilityFormula.getAverageRatingCode(statuses);
	}
	
	public boolean isActive()
	{
		ORef targetRef = getOwnerRef();
		AbstractTarget target = AbstractTarget.findTarget(getProject(), targetRef);
		return target.isViabilityModeTNC();
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
		return objectType == getObjectType();
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

	public static final String OBJECT_NAME = "KeyEcologicalAttribute";
}
