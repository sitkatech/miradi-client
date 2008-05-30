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
import java.util.Vector;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;
import org.miradi.utils.PointList;

public class CreateOutGoingJunctionDoer extends AbstractCreateJunctionDoer
{
	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			EAMGraphCell[] selectedCells = getSelectedCells();
			for (int cellIndex = 0; cellIndex < selectedCells.length; ++cellIndex)
			{
				EAMGraphCell graphCell = selectedCells[cellIndex];
				if (graphCell.isFactor())
					createOutgoingJunction((FactorCell) graphCell);
			}
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private void createOutgoingJunction(FactorCell factorCell) throws CommandFailedException
	{
		DiagramFactor diagramFactor = factorCell.getDiagramFactor();
		Vector<DiagramLink> outgoingDiagramLinks = getDiagramLinks(diagramFactor, FactorLink.FROM);
		for (int index = 0; index < outgoingDiagramLinks.size(); ++index)
		{
			createJunction(diagramFactor, outgoingDiagramLinks.get(index));
		}
	}

	private void createJunction(DiagramFactor diagramFactor, DiagramLink diagramLink) throws CommandFailedException
	{
		Point junctionPoint = getOutgoingJunctionPoint(diagramFactor);
		PointList bendPoints = new PointList(diagramLink.getBendPoints());
		if (bendPoints.contains(junctionPoint))
			return; 
		
		if (bendPoints.size() > 1)
			bendPoints.set(0, junctionPoint);
		else
			bendPoints.add(junctionPoint);
		
		CommandSetObjectData setBendPoints = new CommandSetObjectData(diagramLink, DiagramLink.TAG_BEND_POINTS, bendPoints.toString());
		getProject().executeCommand(setBendPoints);
	}
	
	private Point getOutgoingJunctionPoint(DiagramFactor diagramFactor)
	{
		Point location = diagramFactor.getLocation();
		Dimension size = diagramFactor.getSize();
		
		int junctionX = location.x + size.width;
		int middleOfHeigth = size.height / 2;
		int junctionY = location.y + middleOfHeigth;
		
		Point junctionPoint = new Point(junctionX, junctionY);
		junctionPoint.translate(Project.DEFAULT_GRID_SIZE * 4, 0);
		
		return junctionPoint;
	}
}
