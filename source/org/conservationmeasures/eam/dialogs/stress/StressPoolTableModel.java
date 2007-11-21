/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.stress;

import org.conservationmeasures.eam.dialogs.base.ObjectPoolTableModel;
import org.conservationmeasures.eam.objects.Stress;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.questions.PriorityRatingQuestion;

public class StressPoolTableModel extends ObjectPoolTableModel
{
	public StressPoolTableModel(Project projectToUse)
	{
		super(projectToUse, Stress.getObjectType(), COLUMN_TAGS);
	}
	
	public ChoiceQuestion getColumnQuestion(int column)
	{
		return getPriorityQuestion(getColumnTag(column));
	}

	public static ChoiceQuestion getPriorityQuestion(String columnTag)
	{
		boolean isPriorityRatingQuestionColumn = columnTag.equals(Stress.TAG_SCOPE) ||
		 columnTag.equals(Stress.TAG_SEVERITY) ||
		 columnTag.equals(Stress.PSEUDO_STRESS_RATING);
		if (isPriorityRatingQuestionColumn)
			return new PriorityRatingQuestion(columnTag);
		
		return null;
	}
	
	public static final String[] COLUMN_TAGS = new String[] {
		Stress.TAG_LABEL,
		Stress.TAG_SHORT_LABEL, 
		Stress.TAG_SCOPE, 
		Stress.TAG_SEVERITY, 
		Stress.PSEUDO_STRESS_RATING
	};
}
