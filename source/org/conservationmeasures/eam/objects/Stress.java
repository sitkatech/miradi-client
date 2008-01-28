/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.StressBasedThreatFormula;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.StressScopeChoiceQuestion;
import org.conservationmeasures.eam.questions.StressSeverityChoiceQuestion;
import org.conservationmeasures.eam.questions.StressRatingChoiceQuestion;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class Stress extends BaseObject
{
	public Stress(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
	}
	
	public Stress(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}

	public int getType()
	{
		return getObjectType();
	}
	
	public static int getObjectType()
	{
		return ObjectType.STRESS;
	}
	
	public String getTypeName()
	{
		return OBJECT_NAME;
	}
	
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_STRESS_RATING))
			return getCalculatedStressRating();
		
		return super.getPseudoData(fieldTag);
	}

	public String getCalculatedStressRating()
	{
		int calculatedStressRating = calculateStressRating();
		if (calculatedStressRating == 0)
			return "";
		
		return Integer.toString(calculatedStressRating);
	}
	
	public int calculateStressRating()
	{
		ChoiceItem scopeChoice = new StressScopeChoiceQuestion(Stress.TAG_SCOPE).findChoiceByCode(scope.get());
		if (scopeChoice.getCode().length() == 0)
			return 0;

		ChoiceItem severityChoice = new StressSeverityChoiceQuestion(TAG_SEVERITY).findChoiceByCode(severity.get());
		if (severityChoice.getCode().length() == 0)
			return 0;
		
		int scopeRating = Integer.parseInt(scopeChoice.getCode());
		int severityRating = Integer.parseInt(severityChoice.getCode());
		StressBasedThreatFormula formula = new StressBasedThreatFormula();
		return formula.computeSeverityByScope(scopeRating, severityRating);
	}
	
	public String getShortLabel()
	{
		return shortLabel.get();
	}
	
	public static Stress find(ObjectManager objectManager, ORef stressRef)
	{
		return (Stress) objectManager.findObject(stressRef);
	}
	
	public static Stress find(Project project, ORef stressRef)
	{
		return find(project.getObjectManager(), stressRef);
	}
		
	public String toString()
	{
		return getLabel();
	}
	
	public void clear()
	{
		super.clear();
		
		shortLabel = new StringData();
		detail = new StringData();
		comments = new StringData();
		scope = new ChoiceData();
		severity = new ChoiceData();
		pseudoStressRating = new PseudoQuestionData(PSEUDO_STRESS_RATING, new StressRatingChoiceQuestion(PSEUDO_STRESS_RATING));
		
		addField(TAG_SHORT_LABEL, shortLabel);
		addField(TAG_DETAIL, detail);
		addField(TAG_COMMENTS, comments);
		addField(TAG_SCOPE, scope);
		addField(TAG_SEVERITY, severity);
		addField(PSEUDO_STRESS_RATING, pseudoStressRating);
	}
	
	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_DETAIL = "Detail";
	public static final String TAG_COMMENTS = "Comments";
	public static final String TAG_SCOPE = "Scope";
	public static final String TAG_SEVERITY = "Severity";
	public static final String PSEUDO_STRESS_RATING = "PseudoStressRating";
	
	private StringData shortLabel;
	private StringData detail;
	private StringData comments;
	private ChoiceData scope;
	private ChoiceData severity;
	
	private PseudoQuestionData pseudoStressRating;
	public static final String OBJECT_NAME = "Stress";
}
