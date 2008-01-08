/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import java.text.ParseException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.RelevancyOverride;
import org.conservationmeasures.eam.objecthelpers.RelevancyOverrideSet;
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
			return objective.getText(all);
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
			ORefList relevantRefList = new ORefList(getProject().getObjectData(getORef(), Objective.PSEUDO_DEFAULT_RELEVANT_INDICATOR_REFS));
			RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet(getProject().getObjectData(getORef(), tag));
			for(RelevancyOverride override : relevantOverrides)
			{
				if (override.isOverride())
					relevantRefList.add(override.getRef());
				else
					relevantRefList.remove(override.getRef());
			}
			
			refListEditor.setText(relevantRefList.toString());
		}
		catch(ParseException e)
		{
			//FIXME do something else with this exception
			EAM.logException(e);
		}	
	}
}
