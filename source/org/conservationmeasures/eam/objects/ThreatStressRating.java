/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.ORefData;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.CreateThreatStressRatingParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.PriorityRatingQuestion;
import org.conservationmeasures.eam.questions.StatusQuestion;
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
			return calculateThreatRating();
				
		return super.getPseudoData(fieldTag);
	}
	
	private String calculateThreatRating()
	{
		Stress stress = (Stress) getObjectManager().findObject(getStressRef());
		String stressRatingAsString = stress.getPseudoData(Stress.PSEUDO_STRESS_RATING);
		int stressRating = Integer.parseInt(stressRatingAsString);
		int contributionRating = Integer.parseInt(getContribution().getCode());
		int irreverisbility = Integer.parseInt(getIrreversibility().getCode());
		int max = Math.max(stressRating, contributionRating);
		
		return Integer.toString(Math.max(max, irreverisbility));
	}
	
	public ORef getStressRef()
	{
		return stressRef.getRawRef();
	}
	
	public ChoiceItem getContribution()
	{
		return new StatusQuestion(TAG_CONTRIBUTION).findChoiceByCode(contribution.toString());
	}
	 
	public ChoiceItem getIrreversibility()
	{
		return new StatusQuestion(TAG_IRREVERSIBILITY).findChoiceByCode(irreversibility.toString());
	}
	
	public String getTypeName()
	{
		return OBJECT_NAME;
	}
	
	public String toString()
	{
		return getLabel();
	}
	
	public void clear()
	{
		super.clear();
		contribution = new ChoiceData();
		irreversibility = new ChoiceData();
		stressRef = new ORefData();
		pseudoThreatRating = new PseudoQuestionData(new PriorityRatingQuestion(ThreatStressRating.PSEUDO_TAG_THREAT_RATING));
		
		addField(TAG_CONTRIBUTION, contribution);
		addField(TAG_IRREVERSIBILITY, irreversibility);
		addNoClearField(TAG_STRESS_REF, stressRef);
		addField(PSEUDO_TAG_THREAT_RATING, pseudoThreatRating);
	}
	
	public static final String OBJECT_NAME = "ThreatStressRating";
	
	public static final String TAG_CONTRIBUTION = "Contribution";
	public static final String TAG_IRREVERSIBILITY = "Irreversibility";
	public static final String TAG_STRESS_REF = "StressRef";
	public static final String PSEUDO_TAG_THREAT_RATING = "PseudoThreatRating";
		
	private ChoiceData contribution;
	private ChoiceData irreversibility;
	private ORefData stressRef;
	private PseudoQuestionData pseudoThreatRating;
	
}
