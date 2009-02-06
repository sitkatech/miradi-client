/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.IdListData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.project.TNCViabilityFormula;
import org.miradi.questions.KeyEcologicalAttributeTypeQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;

public class KeyEcologicalAttribute extends BaseObject
{
	public KeyEcologicalAttribute(ObjectManager objectManager, KeyEcologicalAttributeId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}

	public KeyEcologicalAttribute(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}
	
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_INDICATOR_IDS))
			return Indicator.getObjectType();
		
		return super.getAnnotationType(tag);
	}

	public boolean isIdListTag(String tag)
	{
		if (tag.equals(TAG_INDICATOR_IDS))
			return true;
		
		return super.isIdListTag(tag);
	}
	
	public int getType()
	{
		return getObjectType();
	}

	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.KEY_ECOLOGICAL_ATTRIBUTE;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		switch(type)
		{
			case ObjectType.INDICATOR: 
				return true;
			default:
				return false;
		}
	}
	
	
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
		return new ORefList(Indicator.getObjectType(), getIndicatorIds());
	}
	
	public IdList getIndicatorIds()
	{
		return indicatorIds.getIdList();
	}
	
	public String getKeyEcologicalAttributeType()
	{
		return keyEcologicalAttributeType.toString();
	}
	
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_VIABILITY_STATUS))
			return computeTNCViability();
		return super.getPseudoData(fieldTag);
	}
	
	public String computeTNCViability()
	{
		CodeList statuses = new CodeList();
		for(int i = 0; i < indicatorIds.size(); ++i)
		{
			Indicator indicator = (Indicator) objectManager.findObject(new ORef(Indicator.getObjectType(), indicatorIds.get(i)));
			ORef latestMeasurementRef = indicator.getLatestMeasurementRef();
			if (latestMeasurementRef.isInvalid())
				continue;
			
			String status = objectManager.getObjectData(latestMeasurementRef, Measurement.TAG_STATUS);
			statuses.add(status);
		}
		return TNCViabilityFormula.getAverageRatingCode(statuses);
	}
	
	public String getShortLabel()
	{
		return shortLabel.get();
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
	
	void clear()
	{
		super.clear();
		indicatorIds = new IdListData(TAG_INDICATOR_IDS, Indicator.getObjectType());
		description = new StringData(TAG_DESCRIPTION);
		details = new StringData(TAG_DETAILS);
		keyEcologicalAttributeType = new ChoiceData(TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE, getQuestion(KeyEcologicalAttributeTypeQuestion.class));
		shortLabel = new StringData(TAG_SHORT_LABEL);
		viabilityStatus = new PseudoStringData(PSEUDO_TAG_VIABILITY_STATUS);
		
		addField(TAG_INDICATOR_IDS, indicatorIds);
		addField(TAG_DESCRIPTION, description);
		addField(TAG_DETAILS, details);
		addField(TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE, keyEcologicalAttributeType);
		addField(TAG_SHORT_LABEL, shortLabel);
		addField(PSEUDO_TAG_VIABILITY_STATUS, viabilityStatus);
	}
		
	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_INDICATOR_IDS = "IndicatorIds";
	public static final String TAG_DESCRIPTION = "Description";
	public static final String TAG_DETAILS = "Details";
	public static final String TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE = "KeyEcologicalAttributeType";
	public static final String PSEUDO_TAG_VIABILITY_STATUS = "ViabilityStatus";

	public static final String OBJECT_NAME = "KeyEcologicalAttribute";
	
	private IdListData indicatorIds;
	private StringData description;
	private StringData details;
	private ChoiceData keyEcologicalAttributeType;
	private StringData shortLabel;
	private PseudoStringData viabilityStatus;
}
