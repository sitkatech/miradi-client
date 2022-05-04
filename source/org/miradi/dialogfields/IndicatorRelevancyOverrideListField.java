/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class IndicatorRelevancyOverrideListField extends RefListEditorField
{
	public IndicatorRelevancyOverrideListField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, questionToUse);
	}
	
	@Override
	public String getText()
	{
		try
		{
			BaseObject baseObject = BaseObject.find(getProject(), getORef());
			ORefList all = new ORefList(refListEditor.getText());
			return baseObject.getCalculatedRelevantIndicatorOverrides(all).toString();
		}
		catch(Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
		
		return "";
	}

	@Override
	public void setText(String codes)
	{
		try
		{
			BaseObject baseObject = BaseObject.find(getProject(), getORef());
			ORefList relevantRefList = baseObject.getRelevantIndicatorRefList();
			refListEditor.setText(relevantRefList.toString());
		}
		catch(Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}	
	}
}
