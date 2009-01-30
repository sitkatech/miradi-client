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
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramModel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.PointList;
import org.miradi.views.ObjectsDoer;

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
		Rectangle diagramFactorBounds = getDiagramView().getCurrentDiagramObject().getBoundsOfFactorsAndBendPoints();
		
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
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			DiagramModel model = getDiagramView().getDiagramModel();
			Dimension deltaMargin = getDeltasToEnsureMargins();
			moveDiagramFactors(model.getAllDiagramFactors(), deltaMargin);
			moveBendPoints(model.getAllDiagramFactorLinks(), deltaMargin);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}
	
	private void moveBendPoints(Vector<DiagramLink> allDiagramFactorLinks, Dimension deltaMargin) throws Exception
	{
		for (int index = 0; index < allDiagramFactorLinks.size(); ++index)
		{
			DiagramLink diagramLink = allDiagramFactorLinks.get(index);
			PointList bendPoints = diagramLink.getBendPoints().createClone();
			bendPoints.translateAll(deltaMargin.width, deltaMargin.height);
			
			CommandSetObjectData setLinkBendPointsCommand = new CommandSetObjectData(diagramLink.getRef(), DiagramLink.TAG_BEND_POINTS, bendPoints.toString());
			getProject().executeCommand(setLinkBendPointsCommand);
		}
	}

	private void moveDiagramFactors(Vector<DiagramFactor> allDiagramFactors, Dimension deltaMargin) throws Exception
	{
		for (int index = 0; index < allDiagramFactors.size(); ++index)
		{
			DiagramFactor diagramFactor = allDiagramFactors.get(index);
			Point currentLocation = (Point) diagramFactor.getLocation().clone();
			currentLocation.translate(deltaMargin.width, deltaMargin.height);
			String currentLocationAsString = EnhancedJsonObject.convertFromPoint(currentLocation);
			
			CommandSetObjectData setNewLocationCommand = new CommandSetObjectData(diagramFactor.getRef(), DiagramFactor.TAG_LOCATION, currentLocationAsString);
			getProject().executeCommand(setNewLocationCommand);
		}
	}

	private Vector getAllFactorCells()
	{
		return getDiagramView().getDiagramModel().getAllFactorCells();
	}

	//TODO these two methods (getTop/LeftMargin()) should be using project.getGridSize(), not Project.DEFAULT_GRID_SIZE.
	public static int getTopMargin()
	{
		return 2 * (DiagramFactor.getDefaultSize().height + Project.DEFAULT_GRID_SIZE);
	}
	
	public static int getLeftMargin()
	{
		return 2 * (DiagramFactor.getDefaultSize().width  + Project.DEFAULT_GRID_SIZE);
	} 
}
