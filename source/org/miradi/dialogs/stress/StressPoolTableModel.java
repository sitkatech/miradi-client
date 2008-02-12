/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.stress;

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.objects.Stress;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StressRatingChoiceQuestion;
import org.miradi.questions.StressScopeChoiceQuestion;
import org.miradi.questions.StressSeverityChoiceQuestion;

public class StressPoolTableModel extends ObjectPoolTableModel
{
	public StressPoolTableModel(Project projectToUse)
	{
		super(projectToUse, Stress.getObjectType(), COLUMN_TAGS);
	}
	
	public ChoiceQuestion getColumnQuestion(int column)
	{
		return createQuestionForTag(getColumnTag(column));
	}

	public static ChoiceQuestion createQuestionForTag(String columnTag)
	{
		if (columnTag.equals(Stress.TAG_SCOPE))
			return new StressScopeChoiceQuestion();
		
		if (columnTag.equals(Stress.TAG_SEVERITY))
			return new StressSeverityChoiceQuestion();
		
		if (columnTag.equals(Stress.PSEUDO_STRESS_RATING))
			return new StressRatingChoiceQuestion();
		
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
