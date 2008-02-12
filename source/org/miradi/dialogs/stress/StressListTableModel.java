/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.stress;

import org.miradi.dialogs.base.ObjectListTableModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class StressListTableModel extends ObjectListTableModel
{
	public StressListTableModel(Project projectToUse, ORef nodeRef)
	{
		super(projectToUse, nodeRef, Target.TAG_STRESS_REFS, Stress.getObjectType(), getColumnTags());
	}

	public ChoiceQuestion getColumnQuestion(int column)
	{
		return StressPoolTableModel.createQuestionForTag(getColumnTag(column));
	}
	
	private static String[] getColumnTags()
	{
		return StressPoolTableModel.COLUMN_TAGS;
	}
}
