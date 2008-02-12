/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Objective;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class IndicatorRelevancyOverrideListField extends RelevancyOverrideListField
{
	public IndicatorRelevancyOverrideListField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, questionToUse, Objective.TAG_RELEVANT_INDICATOR_SET);
	}
	
	public String getText()
	{
		try
		{
			Objective objective = Objective.find(getProject(), getORef());
			ORefList all = new ORefList(refListEditor.getText());
			return objective.getCalculatedRelevantIndicatorOverrides(all).toString();
		}
		catch(Exception e)
		{
			//FIXME do something else with this exception
			EAM.logException(e);
		}
		
		return "";
	}

	public void setText(String codes)
	{
		try
		{
			Objective objective = Objective.find(getProject(), getORef());
			ORefList relevantRefList = objective.getRelevantIndicatorRefList();
			refListEditor.setText(relevantRefList.toString());
		}
		catch(Exception e)
		{
			//FIXME do something else with this exception
			EAM.logException(e);
		}	
	}
}
