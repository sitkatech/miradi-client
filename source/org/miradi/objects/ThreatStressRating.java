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
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.project.threatrating.StressBasedThreatFormula;
import org.miradi.questions.ChoiceItem;
import org.miradi.schemas.ThreatStressRatingSchema;
import org.miradi.utils.EnhancedJsonObject;

public class ThreatStressRating extends BaseObject
{
	public ThreatStressRating(ObjectManager objectManager, BaseId idToUse) throws Exception
	{
		super(objectManager, idToUse, new ThreatStressRatingSchema());
		
		clear();
	}
	
	public ThreatStressRating(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json, new ThreatStressRatingSchema());
	}

	public boolean isActive()
	{
		return getBooleanData(TAG_IS_ACTIVE);
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	public static int getObjectType()
	{
		return ObjectType.THREAT_STRESS_RATING;
	}
	
	@Override
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_TAG_THREAT_RATING))
			return getCalculatedThreatRating();
				
		return super.getPseudoData(fieldTag);
	}
	
	private String getCalculatedThreatRating()
	{
		return Integer.toString(calculateThreatRating());
	}
	
	public int calculateThreatRating()
	{
		Stress stress = (Stress) getObjectManager().findObject(getStressRef());
		String stressRatingAsString = stress.getPseudoData(Stress.PSEUDO_STRESS_RATING);
		int stressRating = parseInt(stressRatingAsString);
		int contributionRating = parseInt(getContribution().getCode());
		int irreversibilityRating = parseInt(getIrreversibility().getCode());
		
		StressBasedThreatFormula stressBasedThreatFormula = getProject().getStressBasedThreatFormula();
		int contributionIrreversibilityResult = stressBasedThreatFormula.computeContributionByIrreversibility(contributionRating, irreversibilityRating);
		return stressBasedThreatFormula.computeThreatStressRating(contributionIrreversibilityResult, stressRating);
	}
	
	private int parseInt(String intAsString)
	{
		if (intAsString.length() == 0)
			return 0;
		
		return Integer.parseInt(intAsString);
	}
	
	public ORef getStressRef()
	{
		return getRefData(TAG_STRESS_REF);
	}
	
	public ORef getThreatRef()
	{
		return getRefData(TAG_THREAT_REF);
	}
	
	public ChoiceItem getContribution()
	{
		return getChoiceItemData(TAG_CONTRIBUTION);
	}
	 
	public ChoiceItem getIrreversibility()
	{
		return getChoiceItemData(TAG_IRREVERSIBILITY);
	}
	
	@Override
	public String getTypeName()
	{
		return OBJECT_NAME;
	}
	
	@Override
	public String toString()
	{
		return getLabel();
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
		
	public static ThreatStressRating find(ObjectManager objectManager, ORef threatStressRatingRef)
	{
		return (ThreatStressRating) objectManager.findObject(threatStressRatingRef);
	}
	
	public static ThreatStressRating find(Project project, ORef threatStressRatingRef)
	{
		return find(project.getObjectManager(), threatStressRatingRef);
	}
		
	public static final String OBJECT_NAME = "ThreatStressRating";
	
	public static final String TAG_CONTRIBUTION = "Contribution";
	public static final String TAG_IRREVERSIBILITY = "Irreversibility";
	public static final String TAG_STRESS_REF = "StressRef";
	public static final String TAG_THREAT_REF = "ThreatRef";
	public static final String TAG_IS_ACTIVE = "IsActive";
	public static final String PSEUDO_TAG_THREAT_RATING = "PseudoThreatRating";
}
