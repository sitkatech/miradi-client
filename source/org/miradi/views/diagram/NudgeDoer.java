/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.HashSet;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.dialogs.diagram.DiagramPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.DiagramFactorId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramLink;
import org.miradi.project.FactorMoveHandler;
import org.miradi.utils.PointList;

public class NudgeDoer extends LocationDoer
{

	public NudgeDoer(int directionToNudge)
	{
		direction = directionToNudge;
	}
	
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
		
		return isAnythingSelectedInDiagram();
	}

	private boolean isAnythingSelectedInDiagram()
	{
		DiagramPanel diagramPanel = getDiagramView().getDiagramPanel();
		return diagramPanel.getOnlySelectedFactorCells().length > 0 || diagramPanel.getOnlySelectedLinkCells().size() > 0;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;

		try
		{
			int deltaX = 0;
			int deltaY = 0;
			switch(direction)
			{
				case KeyEvent.VK_UP:
					deltaY = -getProject().getGridSize();
					break;
				case KeyEvent.VK_DOWN:
					deltaY = getProject().getGridSize();
					break;
				case KeyEvent.VK_LEFT:
					deltaX = -getProject().getGridSize();
					break;
				case KeyEvent.VK_RIGHT:
					deltaX = getProject().getGridSize();
					break;
			}
			EAM.logVerbose("NudgeNodes ("+deltaX + ","+deltaY+")");
			moveSelectedItems(deltaX, deltaY);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	private boolean isFutureCellLocationInsideDiagramBounds(Point currentLocation, int deltaX, int deltaY)
	{
		int futureX = currentLocation.x + deltaX;
		int futureY = currentLocation.y + deltaY;
		
		if (futureX < 0 || futureY < 0)
			return false;
	
		return true;
	}
	
	private void moveSelectedItems(int deltaX, int deltaY) throws Exception
	{
		DiagramPanel diagramPanel = getDiagramView().getDiagramPanel();
		FactorCell[] factorCells = diagramPanel.getOnlySelectedFactorCells();
		HashSet<FactorCell> selectedFactorAndChildren = diagramPanel.getOnlySelectedFactorAndGroupChildCells();
		
		HashSet<LinkCell> allLinkCells = new HashSet();
		allLinkCells.addAll(getAllLinksInGroupBoxes(diagramPanel.getDiagramModel(), selectedFactorAndChildren));
		allLinkCells.addAll(diagramPanel.getOnlySelectedLinkCells());
		
		DiagramFactorId[] ids = new DiagramFactorId[factorCells.length];
		for(int i = 0; i < factorCells.length; ++i)
		{
			ids[i] = factorCells[i].getDiagramFactorId();
			if (!isFutureCellLocationInsideDiagramBounds(factorCells[i].getLocation(), deltaX, deltaY))
				return;	
			
			factorCells[i].setPreviousLocation(factorCells[i].getLocation());
			factorCells[i].setPreviousPortLocation(factorCells[i].getPortLocation(getDiagramView().getDiagramComponent().getGraphLayoutCache()));
		}
		
		if (wouldMoveBendPointsOutOfBounds(allLinkCells.toArray(new LinkCell[0]), deltaX, deltaY))
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			diagramPanel.moveFactors(deltaX, deltaY, ids);
			moveBendPoints(allLinkCells.toArray(new LinkCell[0]), deltaY, deltaX);
			FactorMoveHandler factorMoveHandler = new FactorMoveHandler(getProject(), getDiagramView().getDiagramModel());
			factorMoveHandler.factorsWereMovedOrResized(ids);
			factorMoveHandler.ensureLevelSegementToFirstBendPoint(ids);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException("Unable to move selected items");
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	public static HashSet<LinkCell> getAllLinksInGroupBoxes(DiagramModel model, HashSet<FactorCell> selectedFactorAndChildren)
	{
		HashSet<LinkCell> linksInsideGroupBoxes = new HashSet();
		FactorCell[] factorCells = selectedFactorAndChildren.toArray(new FactorCell[0]);
		for (int i = 0; i < factorCells.length; ++i)
		{
			FactorCell factorCell = factorCells[i];
			linksInsideGroupBoxes.addAll(getAllLinkCells(model, factorCell, factorCells));
		}
			
		return linksInsideGroupBoxes;
	}

	private static HashSet<LinkCell> getAllLinkCells(DiagramModel model, FactorCell factorCell, FactorCell[] factorCells)
	{
		HashSet<LinkCell> linksInGroupBoxes = new HashSet();
		ORefList diagramLinkReferrerRefs = factorCell.getDiagramFactor().findObjectsThatReferToUs(DiagramLink.getObjectType());
		for (int referrrerIndex = 0; referrrerIndex < diagramLinkReferrerRefs.size(); ++referrrerIndex)
		{
			DiagramLink diagramLink = DiagramLink.find(model.getProject(), diagramLinkReferrerRefs.get(referrrerIndex));
			for (int cellIndex = 0; cellIndex < factorCells.length; ++cellIndex)
			{
				FactorCell thisFactorCell = factorCells[cellIndex];
				if (thisFactorCell.getDiagramFactorRef().equals(factorCell.getDiagramFactorRef()))
					continue;
				
				if (!diagramLink.isToOrFrom(thisFactorCell.getDiagramFactorRef()))
					continue;
						
				LinkCell linkCell = model.findLinkCell(diagramLink);
				linkCell.getBendPointSelectionHelper().selectAll();
				linksInGroupBoxes.add(linkCell);
			}
		}
		
		return linksInGroupBoxes;
	}

	private void moveBendPoints(LinkCell[] links, int deltaY, int deltaX) throws Exception
	{
		LinkBendPointsMoveHandler bendPointsMoveHandler = new LinkBendPointsMoveHandler(getProject());
		bendPointsMoveHandler.moveLinkBendPoints(links, deltaX, deltaY);
	}

	private boolean wouldMoveBendPointsOutOfBounds(LinkCell[] links, int deltaX, int deltaY)
	{
		for (int i = 0; i < links.length; ++i)
		{
			LinkCell linkCell = links[i];
			if (wouldMoveBendPointsOutOfBounds(linkCell, deltaX, deltaY))
				return true;
		}
		return false;
	}

	private boolean wouldMoveBendPointsOutOfBounds(LinkCell linkCell, int deltaX, int deltaY)
	{
		PointList bendPoints = linkCell.getDiagramLink().getBendPoints();
		int[] selectedIndexes = linkCell.getSelectedBendPointIndexes();
		for (int i = 0; i < selectedIndexes.length; ++i)
		{
			int selectionIndex = selectedIndexes[i];
			Point selectedBendPoint = bendPoints.get(selectionIndex);
			if (!isFutureCellLocationInsideDiagramBounds(selectedBendPoint, deltaX, deltaY))
				return true;
		}
		return false;
	}

	private int direction;
}
