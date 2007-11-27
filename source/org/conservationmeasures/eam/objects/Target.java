/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.ORefListData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.FactorLinkSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.TNCViabilityFormula;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.questions.KeyEcologicalAttributeTypeQuestion;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.questions.ViabilityModeQuestion;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;


public class Target extends Factor
{
	public Target(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, Factor.TYPE_TARGET);
		clear();
	}
	
	public Target(FactorId idToUse)
	{
		super(idToUse, Factor.TYPE_TARGET);
		clear();
	}
	
	public Target(ObjectManager objectManager, FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, Factor.TYPE_TARGET, json);
	}
	
	public Target(FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, Factor.TYPE_TARGET, json);
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

	public static boolean canReferToThisType(int type)
	{
		switch(type)
		{
			case ObjectType.STRESS: 
				return true;

			default:
				return false;
		}
	}
	
	public Set<String> getReferencedObjectTags()
	{
		Set<String> set = super.getReferencedObjectTags();
		set.add(TAG_STRESS_REFS);
		set.add(TAG_GOAL_IDS);
		set.add(TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
		return set;
	}
	
	public ORefList getStressRefs()
	{
		return stressRefs.getORefList();
	}
	
	public boolean isTarget()
	{
		return true;
	}
	
	public boolean canHaveGoal()
	{
		return true;
	}
	
	public boolean canHaveKeyEcologicalAttribures()
	{
		return true;
	}
	
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_TARGET_VIABILITY))
			return getTargetViability();
		return super.getPseudoData(fieldTag);
	}

	public FactorLinkSet getDirectThreatTargetFactorLinks()
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
			return super.getIndicators();
		
		return findAllKeaIndicators();
	}
	

	public IdList findAllKeaIndicators()
	{
		IdList list = new IdList(KeyEcologicalAttribute.getObjectType());
		IdList keas = getKeyEcologicalAttributes();
		for (int j=0; j<keas.size(); ++j)
		{
			BaseId keyEcologicalAttributeId = keas.get(j);
			KeyEcologicalAttribute kea = (KeyEcologicalAttribute) objectManager.findObject(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, keyEcologicalAttributeId);
			list.addAll(kea.getIndicatorIds());
		}
		return list;
	}
	
	public ChoiceItem getPseudoTargetViabilityChoiceItem()
	{
		return targetViabilityLabel.getChoiceItem();
	}
	
	public String getBasicTargetStatus()
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
			ChoiceQuestion question = new KeyEcologicalAttributeTypeQuestion(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE);
			return question.getChoices();
		}
		return new ChoiceItem[0];
	}
	
	private String getTargetViability()
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
		IdList keyEcologicalAttributes = getKeyEcologicalAttributes();
		int childCount = keyEcologicalAttributes.size();
		Vector KeyEcologicalAttributes = new Vector();
		for(int i = 0; i < childCount; ++i)
		{
			BaseId keaId = keyEcologicalAttributes.get(i);
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
	
	void clear()
	{
		super.clear();
		targetStatus = new ChoiceData();
		viabiltyMode = new ChoiceData();
		currentStatusJustification = new StringData();
		stressRefs = new ORefListData();
		
		targetViability = new PseudoStringData(PSEUDO_TAG_TARGET_VIABILITY);
		targetStatusLabel = new PseudoQuestionData(new StatusQuestion(TAG_TARGET_STATUS));
		viabiltyModeLabel = new PseudoQuestionData(new ViabilityModeQuestion(TAG_VIABILITY_MODE));
		targetViabilityLabel = new PseudoQuestionData(new StatusQuestion(PSEUDO_TAG_TARGET_VIABILITY));
		
		addField(TAG_TARGET_STATUS, targetStatus);
		addField(TAG_VIABILITY_MODE, viabiltyMode);
		addField(TAG_CURRENT_STATUS_JUSTIFICATION, currentStatusJustification);
		addField(TAG_STRESS_REFS, stressRefs);
		
		addField(PSEUDO_TAG_TARGET_VIABILITY, targetViability);
		addField(PSEUDO_TAG_TARGET_STATUS_VALUE, targetStatusLabel);
		addField(PSEUDO_TAG_VIABILITY_MODE_VALUE, targetStatusLabel);
		addField(PSEUDO_TAG_TARGET_VIABILITY_VALUE, targetViabilityLabel);
	}
	
	public static final String TAG_TARGET_STATUS = "TargetStatus";
	public static final String TAG_VIABILITY_MODE = "ViabilityMode";
	public static final String TAG_CURRENT_STATUS_JUSTIFICATION = "CurrentStatusJustification";
	public static final String TAG_STRESS_REFS = "StressRefs";
	public static final String OBJECT_NAME = "Target";
	
	public static final String PSEUDO_TAG_TARGET_VIABILITY = "TargetViability";
	public static final String PSEUDO_TAG_TARGET_STATUS_VALUE = "TargetStatusValue";
	public static final String PSEUDO_TAG_VIABILITY_MODE_VALUE = "ViabilityModeValue";
	public static final String PSEUDO_TAG_TARGET_VIABILITY_VALUE = "ViabilityValue";
	
	private ChoiceData targetStatus;
	private ChoiceData viabiltyMode;
	private StringData currentStatusJustification;
	private ORefListData stressRefs;
	private PseudoStringData targetViability;
	private PseudoQuestionData targetStatusLabel;
	PseudoQuestionData viabiltyModeLabel;
	private PseudoQuestionData targetViabilityLabel;
}
