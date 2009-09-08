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

package org.miradi.views.diagram.doers;

import org.miradi.diagram.arranger.MeglerArranger;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.views.ViewDoer;

public class ArrangeConceptualModelDoer extends ViewDoer
{
	@Override
	public boolean isAvailable()
	{
		if(!isInDiagram())
			return false;
		
		return ConceptualModelDiagram.is(getCurrentDiagramObject().getType());
	}

	@Override
	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		getProject().executeBeginTransaction();
		try
		{
			new MeglerArranger(getCurrentDiagramObject()).arrange();
		}
		catch(Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeEndTransaction();
		}
	}

	private DiagramObject getCurrentDiagramObject()
	{
		DiagramObject currentDiagramObject = getDiagramView().getCurrentDiagramObject();
		return currentDiagramObject;
	}
	
}
