/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;

public class DirectThreatQuestion extends StaticChoiceQuestion
{
	public DirectThreatQuestion(Project project, String tagToUse)
	{
		super(tagToUse, "Direct Threat", getDirectThreatChoices(project));
	}

	static ChoiceItem[] getDirectThreatChoices(Project project)
	{
		Vector choiceItems = new Vector();
		ChoiceItem notSpecifiedChoice = new ChoiceItem(ORef.INVALID.toString(), EAM.text("Not Specified"));
		choiceItems.add(notSpecifiedChoice);
		
		Factor[] directThreats = project.getCausePool().getDirectThreats();
		for (int i = 0; i < directThreats.length; ++i)
		{
			Factor directThreat = directThreats[i];
			choiceItems.add(new ChoiceItem(directThreat.getRef().toString(), directThreat.getLabel()));
		}
		
		return (ChoiceItem[]) choiceItems.toArray(new ChoiceItem[0]);
	}
}
