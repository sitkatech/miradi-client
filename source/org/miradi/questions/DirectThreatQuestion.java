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
package org.miradi.questions;

import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Factor;
import org.miradi.project.Project;

public class DirectThreatQuestion extends StaticChoiceQuestion
{
	public DirectThreatQuestion(Project project)
	{
		super(getDirectThreatChoices(project));
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
