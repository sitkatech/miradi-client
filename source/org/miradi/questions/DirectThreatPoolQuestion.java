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

import java.util.Arrays;
import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

public class DirectThreatPoolQuestion extends ObjectQuestion
{
	public DirectThreatPoolQuestion(Project projectToUse)
	{
		super(getDirectThreats(projectToUse));
	}

	private static BaseObject[] getDirectThreats(Project projectToUse)
	{
		return projectToUse.getCausePool().getDirectThreats();
	}
	
	@Override
	public ChoiceItem[] getChoices()
	{
		ChoiceItem[] choices = super.getChoices();
		Vector<ChoiceItem> choicesWithUnspecified = new Vector<ChoiceItem>();
		choicesWithUnspecified.add(new ChoiceItem("", EAM.text("Unspecified")));
		choicesWithUnspecified.addAll(Arrays.asList(choices));
		
		return choicesWithUnspecified.toArray(new ChoiceItem[0]);
	}
}
