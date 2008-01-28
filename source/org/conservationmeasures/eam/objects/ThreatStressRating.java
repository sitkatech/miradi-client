/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.BooleanData;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.ORefData;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.CreateThreatStressRatingParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.StressBasedThreatFormula;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.StressContributionQuestion;
import org.conservationmeasures.eam.questions.StressIrreversibilityQuestion;
import org.conservationmeasures.eam.questions.ThreatStressRatingChoiceQuestion;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

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
		return new StressContributionQuestion(TAG_CONTRIBUTION).findChoiceByCode(contribution.toString());
	}
	 
	public ChoiceItem getIrreversibility()
	{
		return new StressIrreversibilityQuestion(TAG_IRREVERSIBILITY).findChoiceByCode(irreversibility.toString());
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
		contribution = new ChoiceData();
		irreversibility = new ChoiceData();
		stressRef = new ORefData();
		isActive = new BooleanData();
		pseudoThreatRating = new PseudoQuestionData(PSEUDO_TAG_THREAT_RATING, new ThreatStressRatingChoiceQuestion(PSEUDO_TAG_THREAT_RATING));
		
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
