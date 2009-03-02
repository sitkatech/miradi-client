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
package org.miradi.views.umbrella;

import org.miradi.commands.Command;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.Project;

public class ThreatStressRatingDeletor
{
	public ThreatStressRatingDeletor(Project projectToUse)
	{
		project = projectToUse;
	}
		
	public void deleteThreatStressRating(ThreatStressRating threatStressRating) throws CommandFailedException
	{
		Command[] commandsToClear = threatStressRating.createCommandsToClear();
		getProject().executeCommandsWithoutTransaction(commandsToClear);
		
		CommandDeleteObject deleteThreatStressRating = new CommandDeleteObject(threatStressRating.getRef());
		getProject().executeCommand(deleteThreatStressRating);
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
