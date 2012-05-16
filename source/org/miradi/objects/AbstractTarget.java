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

import java.util.Vector;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.project.TNCViabilityFormula;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.KeyEcologicalAttributeTypeQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.ViabilityModeQuestion;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.GoalSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.KeyEcologicalAttributeSchema;
import org.miradi.schemas.SubTargetSchema;
import org.miradi.utils.CodeList;

abstract public class AbstractTarget extends Factor
{
	public AbstractTarget(ObjectManager objectManager, FactorId idToUse, final BaseObjectSchema schemaToUse)
	{
		super(objectManager, idToUse, schemaToUse);
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	@Override
	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = super.getOwnedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.GOAL: 
				list.addAll(getGoalRefs());
				break;
			case ObjectType.KEY_ECOLOGICAL_ATTRIBUTE: 
				list.addAll(new ORefList(objectType, getKeyEcologicalAttributes()));
				break;
		}
		return list;
	}

	@Override
	public ORefList getAllObjectsToDeepCopy(ORefList deepCopiedFactorRefs)
	{
		ORefList deepObjectRefsToCopy = super.getAllObjectsToDeepCopy(deepCopiedFactorRefs);
		deepObjectRefsToCopy.addAll(getSubTargetRefs());
		
		return deepObjectRefsToCopy;
	}

	public ORefList getSubTargetRefs()
	{
		return getSafeRefListData(TAG_SUB_TARGET_REFS);
	}

	public ORefList getKeyEcologicalAttributeRefs()
	{
		return new ORefList(KeyEcologicalAttributeSchema.getObjectType(), getKeyEcologicalAttributes());
	}

	public IdList getKeyEcologicalAttributes()
	{
		return getSafeIdListData(TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
	}

	@Override
	public boolean canHaveGoal()
	{
		return true;
	}

	@Override
	public boolean canHaveIndicators()
	{
		if (isViabilityModeTNC())
			return false;
		
		return true;
	}

	@Override
	public boolean canHaveKeyEcologicalAttribures()
	{
		return true;
	}

	@Override
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_SUB_TARGET_REFS))
			return true;
		
		return super.isRefList(tag);
	}

	@Override
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_SUB_TARGET_REFS))
			return SubTargetSchema.getObjectType();
		
		if (tag.equals(TAG_GOAL_IDS))
			return GoalSchema.getObjectType();
		
		if (tag.equals(TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS))
			return KeyEcologicalAttributeSchema.getObjectType();

		return super.getAnnotationType(tag);
	}

	@Override
	public boolean isIdListTag(String tag)
	{
		if (tag.equals(TAG_GOAL_IDS))
			return true;
		
		if (tag.equals(TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS))
			return true;
		
		return super.isIdListTag(tag);
	}

	@Override
	public String getPseudoData(String fieldTag)
	{
		try
		{
			if(fieldTag.equals(PSEUDO_TAG_TARGET_VIABILITY))
				return getTargetViability();
			
			return super.getPseudoData(fieldTag);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}

	public ORefList findAllKeaIndicatorRefs()
	{
		return new ORefList(IndicatorSchema.getObjectType(), findAllKeaIndicators());
	}

	public IdList findAllKeaIndicators()
	{
		IdList list = new IdList(IndicatorSchema.getObjectType());
		IdList keas = getKeyEcologicalAttributes();
		for (int j=0; j<keas.size(); ++j)
		{
			BaseId keyEcologicalAttributeId = keas.get(j);
			KeyEcologicalAttribute kea = (KeyEcologicalAttribute) objectManager.findObject(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, keyEcologicalAttributeId);
			list.addAll(kea.getIndicatorIds());
		}
		return list;
	}

	private String getBasicTargetStatus()
	{
		return getData(TAG_TARGET_STATUS);
	}

	public boolean isViabilityModeTNC()
	{
		return getViabilityMode().equals(ViabilityModeQuestion.TNC_STYLE_CODE);
	}
	
	public boolean isSimpleMode()
	{
		return !isViabilityModeTNC();
	}

	public String getViabilityMode()
	{
		return getData(TAG_VIABILITY_MODE);
	}

	public ChoiceItem[] getKeyEcologicalAttributesTypes()
	{
		if (isViabilityModeTNC())
		{
			ChoiceQuestion question = new KeyEcologicalAttributeTypeQuestion();
			return question.getChoices();
		}
		return new ChoiceItem[0];
	}

	public String getTargetViability()
	{
		if(isViabilityModeTNC())
			return computeTNCViability();
		return getBasicTargetStatus();
	}

	public String computeTNCViability()
	{
		CodeList ratingForEachType = new CodeList();
		
		CodeList allCodes = getActiveKeyEcologicalAttributeTypes();
		for(int i = 0; i < allCodes.size(); ++i)
		{
			String code = allCodes.get(i);
			if(code.equals(StatusQuestion.UNSPECIFIED))
				continue;
			ratingForEachType.add(computeTNCViabilityOfKEAType(allCodes.get(i)));
		}
	
		return TNCViabilityFormula.getAverageRatingCode(ratingForEachType);
	}

	public String computeTNCViabilityOfKEAType(String typeCode)
	{
		KeyEcologicalAttribute[] keas = getKeasForType(typeCode);
		CodeList codes = new CodeList();
		for(int i = 0; i < keas.length; ++i)
		{
			codes.add(keas[i].computeTNCViability());
		}
		return TNCViabilityFormula.getTotalCategoryRatingCode(codes);
	}

	public KeyEcologicalAttribute[] getKeasForType(String typeCode)
	{
		IdList keyEcologicalAttributeIds = getKeyEcologicalAttributes();
		int childCount = keyEcologicalAttributeIds.size();
		Vector<KeyEcologicalAttribute> KeyEcologicalAttributes = new Vector<KeyEcologicalAttribute>();
		for(int i = 0; i < childCount; ++i)
		{
			BaseId keaId = keyEcologicalAttributeIds.get(i);
			KeyEcologicalAttribute kea = objectManager.getKeyEcologicalAttributePool().find(keaId);
			if (kea.getKeyEcologicalAttributeType().equals(typeCode))
				KeyEcologicalAttributes.add(kea);
		}
		
		return KeyEcologicalAttributes.toArray(new KeyEcologicalAttribute[0]);
	}

	public static String computeTNCViability(Project project)
	{
		Target[] targets = project.getTargetPool().getSortedTargets();
		CodeList codes = new CodeList();
		for(int i = 0; i < targets.length; ++i)
		{
			codes.add(targets[i].getTargetViability());
		}
		return TNCViabilityFormula.getAverageRatingCode(codes);
	}

	public CodeList getActiveKeyEcologicalAttributeTypes()
	{
		CodeList allCodes = new CodeList();
		IdList keas = getKeyEcologicalAttributes();
		for(int i = 0; i < keas.size(); ++i)
		{
			KeyEcologicalAttribute kea = (KeyEcologicalAttribute)objectManager.findObject(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, keas.get(i));
			String category = kea.getData(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE);
			if(!allCodes.contains(category))
				allCodes.add(category);
		}
		return allCodes;
	}
	
	public static AbstractTarget findTarget(Project project, ORef abstractTargetRef)
	{
		return (AbstractTarget) project.findObject(abstractTargetRef);
	}
	
	public static boolean isAbstractTarget(BaseObject baseObject)
	{
		return isAbstractTarget(baseObject.getRef());
	}
	
	public static boolean isAbstractTarget(ORef ref)
	{
		return isAbstractTarget(ref.getObjectType());
	}

	public static boolean isAbstractTarget(int type)
	{
		if (Target.is(type))
			return true;
		
		return HumanWelfareTarget.is(type);
	}
	
	@Override
	public IdList getDirectOrIndirectIndicators()
	{
		if(!isViabilityModeTNC())
			return super.getOnlyDirectIndicatorIds();
		
		return findAllKeaIndicators();
	}
	
	@Override
	public ORefList getGoalRefs()
	{
		return getSafeRefListData(TAG_GOAL_IDS);
	}
	
	public static final String TAG_TARGET_STATUS = "TargetStatus";
	public static final String TAG_VIABILITY_MODE = "ViabilityMode";
	public static final String TAG_CURRENT_STATUS_JUSTIFICATION = "CurrentStatusJustification";
	public static final String TAG_SUB_TARGET_REFS = "SubTargetRefs";
	public static final String TAG_GOAL_IDS = "GoalIds"; 
	public static final String TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS = "KeyEcologicalAttributeIds";

	public static final String PSEUDO_TAG_TARGET_VIABILITY = "TargetViability";
	public static final String PSEUDO_TAG_TARGET_STATUS_VALUE = "TargetStatusValue";
	public static final String PSEUDO_TAG_VIABILITY_MODE_VALUE = "ViabilityModeValue";
}
