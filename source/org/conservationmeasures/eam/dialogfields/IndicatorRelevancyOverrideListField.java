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
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		try
		{
			ORefList all = new ORefList(refListEditor.getText());
			ORefList defaultRelevantRefList = new ORefList(getProject().getObjectData(getORef(), Objective.PSEUDO_DEFAULT_RELEVANT_INDICATOR_REFS));
			relevantOverrides.addAll(getRelevancyOverrides(all, defaultRelevantRefList, true));
			relevantOverrides.addAll(getRelevancyOverrides(defaultRelevantRefList, all , false));	
		}
		catch(Exception e)
		{
			//FIXME do something else with this exception
			EAM.logException(e);
		}
		
		return relevantOverrides.toString();
	}

	private RelevancyOverrideSet getRelevancyOverrides(ORefList refList1, ORefList refList2, boolean relevancyValue)
	{
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		ORefList overrideRefs = ORefList.subtract(refList1, refList2);
		for (int i = 0; i < overrideRefs.size(); ++i)
		{
			RelevancyOverride thisOverride = new RelevancyOverride(overrideRefs.get(i), relevancyValue);
			relevantOverrides.add(thisOverride);
		}
		
		return relevantOverrides;
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
