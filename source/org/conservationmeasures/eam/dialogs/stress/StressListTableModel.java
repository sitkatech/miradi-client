/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.stress;

import org.conservationmeasures.eam.dialogs.base.ObjectListTableModel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Stress;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.PriorityRatingQuestion;

public class StressListTableModel extends ObjectListTableModel
{
	public StressListTableModel(Project projectToUse, ORef nodeRef)
	{
		super(projectToUse, nodeRef, Target.TAG_STRESS_REFS, Stress.getObjectType(), getColumnTags());
	}

	public ChoiceItem getChoiceItem(int column, String dataToDisplay)
	{
		return new PriorityRatingQuestion(getColumnTag(column)).findChoiceByCode(dataToDisplay);
	}

	public boolean isChoiceItemColumn(int column)
	{
		String columnTag = getColumnTag(column);
		boolean isChoiceItemColumn = columnTag.equals(Stress.TAG_SCOPE) ||
									 columnTag.equals(Stress.TAG_SEVERITY) ||
									 columnTag.equals(Stress.PSEUDO_STRESS_RATING);
		return isChoiceItemColumn;
	}
	
	private static String[] getColumnTags()
	{
		return new String[]{Stress.TAG_LABEL, Stress.TAG_SCOPE, Stress.TAG_SEVERITY, Stress.PSEUDO_STRESS_RATING};
	}
}
