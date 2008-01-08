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
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;

public class IndicatorRelevancyOverrideListField extends RelevancyOverrideListField
{
	public IndicatorRelevancyOverrideListField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, ChoiceQuestion questionToUse, String defaultListTagToUse, String tagToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, questionToUse, tagToUse);
		
		defaultListTag = defaultListTagToUse;
	}
	
	public void setText(String codes)
	{
		try
		{
			ORefList relevantRefList = new ORefList(getProject().getObjectData(getORef(), defaultListTag));
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
			//TODO do something else with this exception
			EAM.logException(e);
		}	
	}
	
	private String defaultListTag;
}
