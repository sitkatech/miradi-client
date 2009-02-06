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
		super(projectToUse, nodeRef, Target.TAG_STRESS_REFS, Stress.getObjectType(), getColumnTags(projectToUse));
	}

	public ChoiceQuestion getColumnQuestion(int column)
	{
		return StressPoolTableModel.createQuestionForTag(getColumnTag(column));
	}
	
	private static String[] getColumnTags(Project projectToUse)
	{
		return StressPoolTableModel.getStressColumnTags(projectToUse);
	}
}
