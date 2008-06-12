/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.diagram.doers;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.diagram.DiagramModel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.FactorCommandHelper;

public class showStressBubbleDoer extends AbstractStressVisibilityDoer
{
	@Override
	public boolean isAvailable()
	{
		if (!isInDiagram())
			throw new RuntimeException("Added doer to wrong view");
		
		ORef selectedStressRef = getSelectedStress();
		if (selectedStressRef.isInvalid())
			return false;
		
		if (isShowing(selectedStressRef))
			return false;
		
		return true;
	}

	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		ORef selectedStressRef = getSelectedStress();
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			DiagramModel diagramModel = getDiagramView().getDiagramModel();
			FactorCommandHelper helper = new FactorCommandHelper(getProject(), diagramModel);
			helper.createDiagramFactor(diagramModel.getDiagramObject(), selectedStressRef);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}
}
