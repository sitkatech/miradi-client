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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Vector;

import org.miradi.diagram.cells.FactorCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.DiagramFactor;
import org.miradi.project.Project;
import org.miradi.views.ObjectsDoer;
import org.miradi.views.diagram.NudgeDoer;

public class CreateMarginDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
		
		if (getAllFactorCells().size() <= 0)
			return false;
		
		return allowMargin();
	}

	private boolean allowMargin()
	{
		Dimension deltaMargin = getDeltasToEnsureMargins();
		if (deltaMargin.width > 0)
			return true;
		
		if (deltaMargin.height > 0)
			return true;
		
		return false;
	}

	private Dimension getDeltasToEnsureMargins()
	{
		Rectangle diagramFactorBounds = getDiagramFactorBounds();
		
		int deltaX = 0;
		int deltaY = 0;
		if (diagramFactorBounds.x < getLeftMargin())
			 deltaX = getLeftMargin() - diagramFactorBounds.x;
		
		if (diagramFactorBounds.y < getTopMargin())
			 deltaY = getTopMargin() - diagramFactorBounds.y;

		return new Dimension(deltaX, deltaY);
	}

	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			Dimension deltaMargin = getDeltasToEnsureMargins();
			getDiagramView().getCurrentDiagramComponent().selectAll();
			NudgeDoer.moveSelectedItems(getProject(), getDiagramView().getDiagramPanel(), deltaMargin.width, deltaMargin.height);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	private Rectangle getDiagramFactorBounds()
	{
		Vector<FactorCell> allFactorCells = getAllFactorCells();
		Rectangle allCellsBound = null;
		for (int index = 0; index < allFactorCells.size(); ++index)
		{
			Rectangle factorCellBounds = (Rectangle) allFactorCells.get(index).getDiagramFactor().getBounds().clone();
			if (allCellsBound == null)
				allCellsBound = new Rectangle(factorCellBounds);
			
			allCellsBound = allCellsBound.union(factorCellBounds);			
		}
		
		return allCellsBound;
	}

	private Vector getAllFactorCells()
	{
		return getDiagramView().getDiagramModel().getAllFactorCells();
	}
	
	private int getTopMargin()
	{
		return 2 * (DiagramFactor.getDefaultSize().height + Project.DEFAULT_GRID_SIZE);
	}
	
	private int getLeftMargin()
	{
		return 2 * (DiagramFactor.getDefaultSize().width  + Project.DEFAULT_GRID_SIZE);
	} 
}
