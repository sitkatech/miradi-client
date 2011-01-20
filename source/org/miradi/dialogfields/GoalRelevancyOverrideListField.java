/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogfields;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class GoalRelevancyOverrideListField extends RefListEditorField
{
	public GoalRelevancyOverrideListField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, refToUse, questionToUse, tagToUse);
	}
	
	@Override
	public String getText()
	{
		try
		{
			Strategy strategy = Strategy.find(getProject(), getORef());
			ORefList all = new ORefList(refListEditor.getText());
			return strategy.getCalculatedRelevantGoalOverrides(all).toString();
		}
		catch(Exception e)
		{
			EAM.unexpectedErrorDialog(e);
			EAM.logException(e);
			return "";
		}
	}

	@Override
	public void setText(String codes)
	{
		try
		{
			Strategy strategy = Strategy.find(getProject(), getORef());
			ORefList relevantRefList = strategy.getRelevantGoalRefs();
			refListEditor.setText(relevantRefList.toString());
		}
		catch(Exception e)
		{
			EAM.unexpectedErrorDialog(e);
			EAM.logException(e);
		}	
	}
}
