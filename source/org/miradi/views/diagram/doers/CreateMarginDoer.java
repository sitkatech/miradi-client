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

import java.awt.Rectangle;
import java.util.Vector;

import org.miradi.diagram.cells.FactorCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.views.ObjectsDoer;
import org.miradi.views.diagram.NudgeDoer;

public class CreateMarginDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
		
		return allowMargin();
	}

	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			Rectangle allDiagramFactorBounds = getDiagramFactorBounds();
			int deltaToEnsureLeftMargin = getCreateMarginAmount(allDiagramFactorBounds.x, MINIMUM_LEFT_MARGIN);
			int deltaToEnsureTopMargin = getCreateMarginAmount(allDiagramFactorBounds.y, MINIMUM_TOP_MARGIN);
			
			getDiagramView().getCurrentDiagramComponent().selectAll();
			NudgeDoer.moveSelectedItems(getProject(), getDiagramView().getDiagramPanel(), deltaToEnsureLeftMargin, deltaToEnsureTopMargin);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	private int getCreateMarginAmount(int currentPosition, int minimumMargin)
	{
		if (currentPosition < minimumMargin)
			return CREATE_MARGIN_AMOUNT;
		
		return 0;
	}
	
	private boolean allowMargin()
	{
		Rectangle diagramFactorBounds = getDiagramFactorBounds();
		if (diagramFactorBounds == null)
			return false;
		
		if (diagramFactorBounds.getX() < MINIMUM_LEFT_MARGIN)
			return true;
		
		if (diagramFactorBounds.getY() < MINIMUM_TOP_MARGIN)
			return true;
		
		return false;
	}

	private Rectangle getDiagramFactorBounds()
	{
		Vector<FactorCell> allFactorCells = getDiagramView().getDiagramModel().getAllFactorCells();
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
	
	private static final int MINIMUM_TOP_MARGIN = 30;
	private static final int MINIMUM_LEFT_MARGIN = 30;
	private static final int CREATE_MARGIN_AMOUNT = 30;
}
