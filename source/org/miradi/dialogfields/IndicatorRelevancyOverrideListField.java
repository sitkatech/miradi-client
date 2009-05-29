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
package org.miradi.dialogfields;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Desire;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class IndicatorRelevancyOverrideListField extends RelevancyOverrideListField
{
	public IndicatorRelevancyOverrideListField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, questionToUse, Desire.TAG_RELEVANT_INDICATOR_SET);
	}
	
	public String getText()
	{
		try
		{
			Desire desire = Desire.findDesire(getProject(), getORef());
			ORefList all = new ORefList(refListEditor.getText());
			return desire.getCalculatedRelevantIndicatorOverrides(all).toString();
		}
		catch(Exception e)
		{
			//FIXME medium: do something else with this exception
			EAM.logException(e);
		}
		
		return "";
	}

	public void setText(String codes)
	{
		try
		{
			Desire desire = Desire.findDesire(getProject(), getORef());
			ORefList relevantRefList = desire.getRelevantIndicatorRefList();
			refListEditor.setText(relevantRefList.toString());
		}
		catch(Exception e)
		{
			//FIXME medium: do something else with this exception
			EAM.logException(e);
		}	
	}
}
