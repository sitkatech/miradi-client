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
package org.miradi.views.diagram;

import java.awt.Point;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.DiagramLinkId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramLink;
import org.miradi.utils.PointList;

public class DeleteBendPointDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
	
		if (! isInDiagram())
			return false;
		
		DiagramLink[] links = getDiagramView().getDiagramPanel().getOnlySelectedLinks();
		if (links.length == 0)
			return false;

		if (! hasSelectedBendPoint(links))
			return false;
			
		return true;
	}

	private boolean hasSelectedBendPoint(DiagramLink[] links)
	{
		DiagramModel diagramModel = getDiagramView().getDiagramModel();
		for (int i = 0; i < links.length; ++i)
		{
			LinkCell linkCell = diagramModel.getLinkCell(links[i]);
			if (linkCell == null)
			{
				EAM.logError("LinkCell is null due to link not being removed from selection list.");
				continue;
			}
			
			int bendPointSelectionCount = linkCell.getSelectedBendPointIndexes().length;
			if (bendPointSelectionCount > 0)
				return true;
		}
		
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;

		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			deleteBendPoints();
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}
	
	private void deleteBendPoints() throws CommandFailedException
	{
		DiagramLink[] links = getDiagramView().getDiagramPanel().getOnlySelectedLinks();
		
		for (int i = 0; i < links.length; ++i)
		{
			DiagramLink diagramFactorLink = links[i];			
			PointList newBendPoints = getBendPointListMinusDeletedPoint(diagramFactorLink);
			
			String newBendPointList = newBendPoints.toJson().toString();
			DiagramLinkId linkId = diagramFactorLink.getDiagramLinkId();
			CommandSetObjectData removeBendPointCommand = new CommandSetObjectData(ObjectType.DIAGRAM_LINK, linkId, DiagramLink.TAG_BEND_POINTS, newBendPointList);
			getProject().executeCommand(removeBendPointCommand);
		}
	}
	
	private PointList getBendPointListMinusDeletedPoint(DiagramLink diagramFactorLink)
	{
		LinkCell linkCell = getDiagramView().getDiagramModel().getLinkCell(diagramFactorLink);
		PointList currentBendPoints = diagramFactorLink.getBendPoints();
		int[] selectedBendPointIndexes = linkCell.getSelectedBendPointIndexes();
		PointList newBendPoints = new PointList();
		newBendPoints.addAll(currentBendPoints.getAllPoints());
		
		for (int i = 0; i < selectedBendPointIndexes.length; ++i)
		{
			Point pointToRemove = currentBendPoints.get(selectedBendPointIndexes[i]);
			newBendPoints.removePoint(pointToRemove);
			linkCell.getBendPointSelectionHelper().removeSelectionIndex(selectedBendPointIndexes[i]);
		}
		
		return newBendPoints;
	}
}
