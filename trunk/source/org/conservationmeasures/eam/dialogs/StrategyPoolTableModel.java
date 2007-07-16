/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.questions.StrategyRatingSummaryQuestion;

public class StrategyPoolTableModel extends ObjectPoolTableModel
{
	public StrategyPoolTableModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.STRATEGY, COLUMN_TAGS);	
	}

	
	public IdList getLatestIdListFromProject()
	{
		IdList filteredStrategies = new IdList();
		
		IdList strategy = super.getLatestIdListFromProject();
		for (int i=0; i<strategy.size(); ++i)
		{
			BaseId baseId = strategy.get(i);
			Factor factor = (Factor) project.findObject(ObjectType.STRATEGY, baseId);
			if (!factor.isStrategy())
				continue;
			if (!((Strategy)factor).isStatusDraft())
				filteredStrategies.add(baseId);
		}
		return filteredStrategies;
	}
	
	public String getValueToDisplay(ORef rowObjectRef, String tag)
	{
		if(tag.equals(Strategy.PSEUDO_TAG_RATING_SUMMARY))
			return getRatingSummaryString(rowObjectRef, new StrategyRatingSummaryQuestion(tag));
		return super.getValueToDisplay(rowObjectRef, tag);
	}
	
	String getRatingSummaryString(ORef objectRef, ChoiceQuestion question)
	{
		String code = project.getObjectData(objectRef.getObjectType(), objectRef.getObjectId(), question.getTag());
		return question.findChoiceByCode(code).getLabel();
	}


	private static final String[] COLUMN_TAGS = new String[] {
		Strategy.TAG_SHORT_LABEL,
		Strategy.TAG_LABEL,
		Strategy.PSEUDO_TAG_RATING_SUMMARY,
	};

}
