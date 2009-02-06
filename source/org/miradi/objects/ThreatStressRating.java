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
import org.miradi.objectdata.BooleanData;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.ORefData;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.CreateThreatStressRatingParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.project.threatrating.StressBasedThreatFormula;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.StressContributionQuestion;
import org.miradi.questions.StressIrreversibilityQuestion;
import org.miradi.questions.ThreatStressRatingChoiceQuestion;
import org.miradi.utils.EnhancedJsonObject;

public class ThreatStressRating extends BaseObject
{
	public ThreatStressRating(ObjectManager objectManager, BaseId idToUse, CreateThreatStressRatingParameter extraInfo) throws Exception
	{
		super(objectManager, idToUse);
		
		clear();
		setData(TAG_STRESS_REF, extraInfo.getStressRef().toString());
	}
	
	public ThreatStressRating(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}

	public CreateObjectParameter getCreationExtraInfo()
	{
		return new CreateThreatStressRatingParameter(getStressRef());
	}
	
	public boolean isActive()
	{
		return isActive.asBoolean();
	}
	
	public int getType()
	{
		return getObjectType();
	}
	
	public static int getObjectType()
	{
		return ObjectType.THREAT_STRESS_RATING;
	}
	
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
		return stressRef.getRawRef();
	}
	
	public ChoiceItem getContribution()
	{
		return new StressContributionQuestion().findChoiceByCode(contribution.toString());
	}
	 
	public String getIrreversibilityCode()
	{
		return irreversibility.get();
	}
	
	public ChoiceItem getIrreversibility()
	{
		return new StressIrreversibilityQuestion().findChoiceByCode(irreversibility.toString());
	}
	
	public String getTypeName()
	{
		return OBJECT_NAME;
	}
	
	public String toString()
	{
		return getLabel();
	}
	
	public static ThreatStressRating find(ObjectManager objectManager, ORef threatStressRatingRef)
	{
		return (ThreatStressRating) objectManager.findObject(threatStressRatingRef);
	}
	
	public static ThreatStressRating find(Project project, ORef threatStressRatingRef)
	{
		return find(project.getObjectManager(), threatStressRatingRef);
	}
		
	public void clear()
	{
		super.clear();
		contribution = new ChoiceData(TAG_CONTRIBUTION, getQuestion(StressContributionQuestion.class));
		irreversibility = new ChoiceData(TAG_IRREVERSIBILITY, getQuestion(StressIrreversibilityQuestion.class));
		stressRef = new ORefData(TAG_STRESS_REF);
		isActive = new BooleanData(TAG_IS_ACTIVE);
		pseudoThreatRating = new PseudoQuestionData(PSEUDO_TAG_THREAT_RATING, new ThreatStressRatingChoiceQuestion());
		
		addField(TAG_CONTRIBUTION, contribution);
		addField(TAG_IRREVERSIBILITY, irreversibility);
		addNoClearField(TAG_STRESS_REF, stressRef);
		addField(TAG_IS_ACTIVE, isActive);
		addField(PSEUDO_TAG_THREAT_RATING, pseudoThreatRating);
	}
	
	public static final String OBJECT_NAME = "ThreatStressRating";
	
	public static final String TAG_CONTRIBUTION = "Contribution";
	public static final String TAG_IRREVERSIBILITY = "Irreversibility";
	public static final String TAG_STRESS_REF = "StressRef";
	public static final String TAG_IS_ACTIVE = "IsActive";
	public static final String PSEUDO_TAG_THREAT_RATING = "PseudoThreatRating";
		
	private ChoiceData contribution;
	private ChoiceData irreversibility;
	private ORefData stressRef;
	private BooleanData isActive;
	private PseudoQuestionData pseudoThreatRating;
	
}
