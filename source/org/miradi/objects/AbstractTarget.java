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

import java.util.Arrays;
import java.util.Vector;
import java.util.function.Function;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.project.KEAViabilityFormula;
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
	public boolean canDirectlyOwnIndicators()
	{
		if (isViabilityModeKEA())
			return false;
		
		return true;
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
	public String getPseudoData(String fieldTag)
	{
		try
		{
			if(fieldTag.equals(PSEUDO_TAG_TARGET_VIABILITY))
				return getTargetViability();
			
			if(fieldTag.equals(PSEUDO_TAG_TARGET_FUTURE_VIABILITY))
				return getTargetFutureViability();

			return super.getPseudoData(fieldTag);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}

	private IdList findAllKeaIndicators()
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

	private String getBasicTargetFutureStatus()
	{
		return getData(TAG_TARGET_FUTURE_STATUS);
	}

	public boolean isViabilityModeKEA()
	{
		return getViabilityMode().equals(ViabilityModeQuestion.TNC_STYLE_CODE);
	}
	
	public boolean isSimpleMode()
	{
		return !isViabilityModeKEA();
	}

	public String getViabilityMode()
	{
		return getData(TAG_VIABILITY_MODE);
	}

	public String getTargetViability()
	{
		if(isViabilityModeKEA())
			return computeViability(KeyEcologicalAttribute::computeViability);
		return getBasicTargetStatus();
	}

	public String getTargetFutureViability()
	{
		if(isViabilityModeKEA())
			return computeViability(KeyEcologicalAttribute::computeFutureViability);
		return getBasicTargetFutureStatus();
	}

	private String computeViability(Function<KeyEcologicalAttribute, String> keaViabilityFn)
	{
		CodeList ratingForEachType = new CodeList();

		CodeList allCodes = getActiveKeyEcologicalAttributeTypes();
		for(int i = 0; i < allCodes.size(); ++i)
		{
			String code = allCodes.get(i);
			if(code.equals(StatusQuestion.UNSPECIFIED))
				continue;
			ratingForEachType.add(computeViabilityOfKEAType(allCodes.get(i), keaViabilityFn));
		}

		return KEAViabilityFormula.getAverageRatingCode(ratingForEachType);
	}

	private String computeViabilityOfKEAType(String typeCode, Function<KeyEcologicalAttribute, String> keaViabilityFn)
	{
		KeyEcologicalAttribute[] keas = getKEAsForType(typeCode);
		CodeList codes = new CodeList();
		for(int i = 0; i < keas.length; ++i)
		{
			codes.add(keaViabilityFn.apply(keas[i]));
		}
		return KEAViabilityFormula.getTotalCategoryRatingCode(codes);
	}

	private KeyEcologicalAttribute[] getKEAsForType(String typeCode)
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

	public static String computeOverallProjectViability(Project project)
	{
		return computeOverallProjectViability(project, AbstractTarget::getTargetViability);
	}

	public static String computeOverallProjectFutureViability(Project project)
	{
		return computeOverallProjectViability(project, AbstractTarget::getTargetFutureViability);
	}

	private static String computeOverallProjectViability(Project project, Function<AbstractTarget, String> targetViabilityFn)
	{
		Vector<AbstractTarget> targets = new Vector<AbstractTarget>();
		targets.addAll(Arrays.asList(project.getHumanWelfareTargetPool().getSortedHumanWelfareTargets()));
		targets.addAll(Arrays.asList(project.getTargetPool().getSortedTargets()));
		CodeList codes = new CodeList();
		for(AbstractTarget target : targets)
		{
			codes.add(targetViabilityFn.apply(target));
		}

		return KEAViabilityFormula.getAverageRatingCode(codes);
	}

	private CodeList getActiveKeyEcologicalAttributeTypes()
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
		if(!isViabilityModeKEA())
			return super.getOnlyDirectIndicatorIds();
		
		return findAllKeaIndicators();
	}
	
	@Override
	public ORefList getGoalRefs()
	{
		return getSafeRefListData(TAG_GOAL_IDS);
	}

	public static final String TAG_VIABILITY_MODE = "ViabilityMode";
	public static final String TAG_TARGET_STATUS = "TargetStatus";
	public static final String TAG_CURRENT_STATUS_JUSTIFICATION = "CurrentStatusJustification";
	public static final String TAG_TARGET_FUTURE_STATUS = "TargetFutureStatus";
	public static final String TAG_FUTURE_STATUS_JUSTIFICATION = "FutureStatusJustification";
	public static final String TAG_SUB_TARGET_REFS = "SubTargetRefs";
	public static final String TAG_GOAL_IDS = "GoalIds"; 
	public static final String TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS = "KeyEcologicalAttributeIds";

	public static final String PSEUDO_TAG_TARGET_VIABILITY = "TargetViability";
	public static final String PSEUDO_TAG_TARGET_FUTURE_VIABILITY = "TargetFutureViability";
}
