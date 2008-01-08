/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;

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
			return objective.getText(all).toString();
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
			ORefList relevantRefList = objective.getRelevantRefList();
			refListEditor.setText(relevantRefList.toString());
		}
		catch(Exception e)
		{
			//FIXME do something else with this exception
			EAM.logException(e);
		}	
	}
}
