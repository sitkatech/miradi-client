/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.CodeListData;
import org.miradi.objectdata.ORefListData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.FactorLinkSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.project.TNCViabilityFormula;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.HabitatAssociationQuestion;
import org.miradi.questions.KeyEcologicalAttributeTypeQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.ViabilityModeQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;


public class Target extends Factor
{
	public Target(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, Factor.TYPE_TARGET);
		clear();
	}
	
	public Target(ObjectManager objectManager, FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, Factor.TYPE_TARGET, json);
	}
	
	public static boolean canOwnThisType(int type)
	{
		if (Factor.canOwnThisType(type))
			return true;
		
		switch(type)
		{
			case ObjectType.GOAL: 
				return true;
			case ObjectType.KEY_ECOLOGICAL_ATTRIBUTE: 
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
			case ObjectType.GOAL: 
				list.addAll(new ORefList(objectType, getGoals()));
				break;
			case ObjectType.KEY_ECOLOGICAL_ATTRIBUTE: 
				list.addAll(new ORefList(objectType, getKeyEcologicalAttributes()));
				break;
		}
		return list;
	}

	public ORefList getAllObjectsToDeepCopy()
	{
		ORefList deepObjectRefsToCopy = super.getAllObjectsToDeepCopy();
		deepObjectRefsToCopy.addAll(getStressRefs());
		deepObjectRefsToCopy.addAll(getSubTargetRefs());
		
		return deepObjectRefsToCopy;
	}
		
	public ORefList getStressRefs()
	{
		return stressRefs.getORefList();
	}
	
	public ORefList getSubTargetRefs()
	{
		return subTargetRefs.getORefList();
	}
	
	public ORefList getKeyEcologicalAttributeRefs()
	{
		return new ORefList(KeyEcologicalAttribute.getObjectType(), getKeyEcologicalAttributes());
	}
	
	public IdList getKeyEcologicalAttributes()
	{
		return keyEcologicalAttributes.getIdList();
	}
	
	public boolean isTarget()
	{
		return true;
	}
	
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
	
	public boolean canHaveKeyEcologicalAttribures()
	{
		return true;
	}
	
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_STRESS_REFS))
			return true;
		
		if (tag.equals(TAG_SUB_TARGET_REFS))
			return true;
		
		return super.isRefList(tag);
	}
	
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_STRESS_REFS))
			return Stress.getObjectType();
	
		if (tag.equals(TAG_SUB_TARGET_REFS))
			return SubTarget.getObjectType();
		
		return super.getAnnotationType(tag);
	}
	
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_TARGET_VIABILITY))
			return getTargetViability();
		
		if(fieldTag.equals(PSEUDO_TAG_HABITAT_ASSOCIATION_VALUE))
			return getHabitatAssociationValue();
		
		return super.getPseudoData(fieldTag);
	}

	private String getHabitatAssociationValue()
	{
		StringBuffer appendedChoiceValues = new StringBuffer();
		ChoiceQuestion question = getHabitatAssociationQuestion();
		CodeList habitatCodes = habitatAssociation.getCodeList();
		for(int index = 0; index < habitatCodes.size(); ++index)
		{
			ChoiceItem choiceItem = question.findChoiceByCode(habitatCodes.get(index));
			appendedChoiceValues.append(choiceItem);
			appendedChoiceValues.append(";");
		}
		
		return appendedChoiceValues.toString();
	}

	public FactorLinkSet getThreatTargetFactorLinks()
	{
		ORefList linkRefsThatReferToUs = findObjectsThatReferToUs(FactorLink.getObjectType());
		FactorLinkSet directThreatTargetLinks = new FactorLinkSet();
		for (int i = 0; i < linkRefsThatReferToUs.size(); ++i)
		{
			ORef linkRef = linkRefsThatReferToUs.get(i);
			FactorLink factorLink = (FactorLink) objectManager.findObject(linkRef);
			if (factorLink.isThreatTargetLink())
				directThreatTargetLinks.add(factorLink);
		}
		return directThreatTargetLinks;
	}
	
	public IdList getDirectOrIndirectIndicators()
	{
		if(!isViabilityModeTNC())
			return super.getIndicatorIds();
		
		return findAllKeaIndicators();
	}

	public ORefList findAllKeaIndicatorRefs()
	{
		return new ORefList(Indicator.getObjectType(), findAllKeaIndicators());
	}
	
	public IdList findAllKeaIndicators()
	{
		IdList list = new IdList(Indicator.getObjectType());
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
		return targetStatus.get();
	}
	
	public boolean isViabilityModeTNC()
	{
		return getViabilityMode().equals(ViabilityModeQuestion.TNC_STYLE_CODE);
	}

	public String getViabilityMode()
	{
		return viabiltyMode.get();
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
		Vector KeyEcologicalAttributes = new Vector();
		for(int i = 0; i < childCount; ++i)
		{
			BaseId keaId = keyEcologicalAttributeIds.get(i);
			KeyEcologicalAttribute kea = objectManager.getKeyEcologicalAttributePool().find(keaId);
			if (kea.getKeyEcologicalAttributeType().equals(typeCode))
				KeyEcologicalAttributes.add(kea);
		}
		
		return (KeyEcologicalAttribute[])KeyEcologicalAttributes.toArray(new KeyEcologicalAttribute[0]);
	}
	
	static public String computeTNCViability(Project project)
	{
		Target[] targets = project.getTargetPool().getTargets();
		CodeList codes = new CodeList();
		for(int i = 0; i < targets.length; ++i)
		{
			codes.add(targets[i].computeTNCViability());
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
		return ObjectType.TARGET;
	}
	
	public static Target find(ObjectManager objectManager, ORef targetRef)
	{
		return (Target) objectManager.findObject(targetRef);
	}
	
	public static Target find(Project project, ORef targetRef)
	{
		return find(project.getObjectManager(), targetRef);
	}

	public static boolean is(BaseObject baseObject)
	{
		return is(baseObject.getType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
		
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	void clear()
	{
		super.clear();
		targetStatus = new ChoiceData(TAG_TARGET_STATUS, getQuestion(StatusQuestion.class));
		viabiltyMode = new ChoiceData(TAG_VIABILITY_MODE, getQuestion(ViabilityModeQuestion.class));
		currentStatusJustification = new StringData(TAG_CURRENT_STATUS_JUSTIFICATION);
		stressRefs = new ORefListData(TAG_STRESS_REFS);
		speciesLatinName = new StringData(TAG_SPECIES_LATIN_NAME);
		subTargetRefs = new ORefListData(TAG_SUB_TARGET_REFS);
		habitatAssociation = new CodeListData(TAG_HABITAT_ASSOCIATION, getHabitatAssociationQuestion());
		
		
		targetViability = new PseudoStringData(PSEUDO_TAG_TARGET_VIABILITY);
		targetStatusLabel = new PseudoQuestionData(PSEUDO_TAG_TARGET_STATUS_VALUE, new StatusQuestion());
		viabiltyModeLabel = new PseudoQuestionData(PSEUDO_TAG_VIABILITY_MODE_VALUE, new ViabilityModeQuestion());
		habitatAssociationLabel = new PseudoQuestionData(PSEUDO_TAG_HABITAT_ASSOCIATION_VALUE, getHabitatAssociationQuestion());
		
		addField(TAG_TARGET_STATUS, targetStatus);
		addField(TAG_VIABILITY_MODE, viabiltyMode);
		addField(TAG_CURRENT_STATUS_JUSTIFICATION, currentStatusJustification);
		addField(TAG_STRESS_REFS, stressRefs);
		addField(TAG_SPECIES_LATIN_NAME, speciesLatinName);
		addField(TAG_SUB_TARGET_REFS, subTargetRefs);
		addField(TAG_HABITAT_ASSOCIATION, habitatAssociation);
		
		addField(PSEUDO_TAG_TARGET_VIABILITY, targetViability);
		addField(PSEUDO_TAG_TARGET_STATUS_VALUE, targetStatusLabel);
		addField(PSEUDO_TAG_VIABILITY_MODE_VALUE, viabiltyModeLabel);
		addField(PSEUDO_TAG_HABITAT_ASSOCIATION_VALUE, habitatAssociationLabel);
	}

	private ChoiceQuestion getHabitatAssociationQuestion()
	{
		return getQuestion(HabitatAssociationQuestion.class);
	}
	
	public static final String TAG_TARGET_STATUS = "TargetStatus";
	public static final String TAG_VIABILITY_MODE = "ViabilityMode";
	public static final String TAG_CURRENT_STATUS_JUSTIFICATION = "CurrentStatusJustification";
	public static final String TAG_STRESS_REFS = "StressRefs";
	public static final String TAG_SPECIES_LATIN_NAME = "SpeciesLatinName";
	public static final String TAG_SUB_TARGET_REFS = "SubTargetRefs";
	public static final String TAG_HABITAT_ASSOCIATION = "HabitatAssociation";
	
	public static final String OBJECT_NAME = "Target";
	
	public static final String PSEUDO_TAG_TARGET_VIABILITY = "TargetViability";
	public static final String PSEUDO_TAG_TARGET_STATUS_VALUE = "TargetStatusValue";
	public static final String PSEUDO_TAG_VIABILITY_MODE_VALUE = "ViabilityModeValue";
	public static final String PSEUDO_TAG_HABITAT_ASSOCIATION_VALUE = "HabitatAssociationValue";
	
	private ChoiceData targetStatus;
	private ChoiceData viabiltyMode;
	private StringData currentStatusJustification;
	private ORefListData stressRefs;
	private StringData speciesLatinName;
	private ORefListData subTargetRefs;
	private CodeListData habitatAssociation;
	
	private PseudoStringData targetViability;
	private PseudoQuestionData targetStatusLabel;
	private PseudoQuestionData viabiltyModeLabel;
	private PseudoQuestionData habitatAssociationLabel;
}
