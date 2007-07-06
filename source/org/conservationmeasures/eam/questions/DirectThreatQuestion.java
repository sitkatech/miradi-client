/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;

public class DirectThreatQuestion extends ChoiceQuestion
{
	public DirectThreatQuestion(Project project, String tagToUse)
	{
		super(tagToUse, "Direct Threat", getViabilityModeChoices(project));
	}

	static ChoiceItem[] getViabilityModeChoices(Project project)
	{
		EAMObjectPool pool = project.getPool(ObjectType.CAUSE);
		ORefList orefList = pool.getORefList();
		Vector choiceItems = new Vector();
		ChoiceItem notSpecifiedChoice = new ChoiceItem(ORef.INVALID.toString(), EAM.text("Not Specified"));
		choiceItems.add(notSpecifiedChoice);
		for (int i = 0; i < orefList.size(); ++i)
		{
			Factor factor = (Factor) project.findObject(orefList.get(i));
			if (factor.isDirectThreat())
				choiceItems.add(new ChoiceItem(factor.getRef().toString(), factor.getLabel()));
		}
		
		return (ChoiceItem[]) choiceItems.toArray(new ChoiceItem[0]);
	}
}
