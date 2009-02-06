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

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.project.Project;
import org.miradi.utils.PointList;
import org.miradi.views.diagram.LocationDoer;

abstract public class AbstractCreateJunctionDoer extends LocationDoer
{
	@Override
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
		
		return isAtleastOneSelectedFactor();
	}

	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			Vector<DiagramFactor> diagramFactors = getSelectedDiagramFactors();
			for (int index = 0; index < diagramFactors.size(); ++index)
			{
				createJunction(diagramFactors.get(index));
			}		
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private void createJunction(DiagramFactor diagramFactor) throws CommandFailedException
	{
		Vector<DiagramLink> outgoingDiagramLinks = getDiagramLinks(diagramFactor, getDirection());
		for (int index = 0; index < outgoingDiagramLinks.size(); ++index)
		{
			createJunction(diagramFactor, outgoingDiagramLinks.get(index));
		}
	}
	
	private void createJunction(DiagramFactor diagramFactor, DiagramLink diagramLink) throws CommandFailedException
	{
		Point junctionPoint = getJunctionPoint(diagramFactor.getBounds());
		PointList bendPoints = new PointList(diagramLink.getBendPoints());
		if (bendPoints.contains(junctionPoint))
			return; 
		
		if (bendPoints.size() > 0)
			bendPoints.insertAt(junctionPoint, getInsertBendPointAtIndex(bendPoints));
		else
			bendPoints.add(junctionPoint);
		
		CommandSetObjectData setBendPoints = new CommandSetObjectData(diagramLink, DiagramLink.TAG_BEND_POINTS, bendPoints.toString());
		getProject().executeCommand(setBendPoints);
	}
	
	protected Vector<DiagramLink> getDiagramLinks(DiagramFactor diagramFactor, int direction)
	{
		Vector<DiagramLink> diagramLinks = new Vector<DiagramLink>();
		ORefList diagramLinkReferrerRefs = diagramFactor.findObjectsThatReferToUs(DiagramLink.getObjectType());
		for (int refIndex = 0; refIndex < diagramLinkReferrerRefs.size(); ++refIndex)
		{
			DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkReferrerRefs.get(refIndex));
			ORef diagramFactorRef = diagramLink.getDiagramFactorRef(direction);
			if (diagramFactor.getRef().equals(diagramFactorRef))
				diagramLinks.add(diagramLink);
		}
		
		return diagramLinks;
	}
	
	private boolean isAtleastOneSelectedFactor()
	{
		return getSelectedDiagramFactors().size() > 0;
	}

	protected Vector<DiagramFactor> getSelectedDiagramFactors()
	{
		Vector<DiagramFactor> diagramFactors = new Vector();
		EAMGraphCell[] cells = getSelectedCells();
		for (int index = 0; index < cells.length; ++index)
		{
			if (cells[index].isFactor())
			{
				FactorCell factorCell = ((FactorCell)cells[index]);
				diagramFactors.add(factorCell.getDiagramFactor());
			}
		}
		
		return diagramFactors;
	}
	
	protected EAMGraphCell[] getSelectedCells()
	{
		return getDiagramView().getCurrentDiagramComponent().getSelectedAndRelatedCells();
	}
	
	private Point getJunctionPoint(Rectangle diagramFactorBounds)
	{
		int verticalCenteredY = (int) diagramFactorBounds.getCenterY();
		Point junctionPoint = new Point(calculateJunctionX(diagramFactorBounds), verticalCenteredY);
		
		return getProject().getSnapped(junctionPoint);
	}
	
	abstract protected int getDirection();
	
	abstract protected int getInsertBendPointAtIndex(PointList bendPoints);
	
	abstract protected int calculateJunctionX(Rectangle bounds);
		
	protected static final int JUNCTION_DISTANCE_FROM_FACTOR = Project.DEFAULT_GRID_SIZE * 4;
}
