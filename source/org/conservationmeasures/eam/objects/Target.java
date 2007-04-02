/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.questions.ViabilityModeQuestion;
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
	
	public IdList getDirectOrIndirectGoals()
	{
		IdList goalIds = new IdList();

		if(!isViabilityModeTNC())
			return getGoals();
		
		IdList indicatorIds = getDirectOrIndirectIndicators();
		for(int i = 0; i < indicatorIds.size(); ++i)
		{
			Indicator indicator = (Indicator)objectManager.findObject(ObjectType.INDICATOR, indicatorIds.get(i));
			goalIds.addAll(indicator.getGoalIds());
		}
		
		return goalIds;
	}
	
	
	public IdList getDirectOrIndirectIndicators()
	{
		if(!isViabilityModeTNC())
			return super.getIndicators();
		
		return findAllKeaIndicators();
	}
	

	public IdList findAllKeaIndicators()
	{
		IdList list = new IdList();
		IdList keas = getKeyEcologicalAttributes();
		for (int j=0; j<keas.size(); ++j)
		{
			BaseId keyEcologicalAttributeId = keas.get(j);
			KeyEcologicalAttribute kea = (KeyEcologicalAttribute) objectManager.findObject(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, keyEcologicalAttributeId);
			list.addAll(kea.getIndicatorIds());
		}
		return list;
	}
	
	
	public String getBasicTargetStatus()
	{
		return targetStatus.get();
	}
	
	public boolean isViabilityModeTNC()
	{
		return viabiltyMode.get().equals(ViabilityModeQuestion.TNC_STYLE_CODE);
	}
	
	void clear()
	{
		super.clear();
		targetStatus = new ChoiceData();
		viabiltyMode = new ChoiceData();
		
		addField(TAG_TARGET_STATUS, targetStatus);
		addField(TAG_VIABILITY_MODE, viabiltyMode);
	}
	
	public static final String TAG_TARGET_STATUS = "TargetStatus";
	public static final String TAG_VIABILITY_MODE = "ViabilityMode";
	public static final String OBJECT_NAME = "Target";
	
	public static final String PSEUDO_TAG_TARGET_VIABILITY = "PseudoTagTargetViability";
	
	ChoiceData targetStatus;
	ChoiceData viabiltyMode;
}
