/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.awt.event.MouseEvent;

import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.utils.IntVector;
import org.conservationmeasures.eam.utils.PointList;

public class BendPointSelectionHelper
{
	public BendPointSelectionHelper(LinkCell linkCellToUse)
	{
		linkCell = linkCellToUse;
		clearSelection();
	}
	
	public void clearSelection()
	{
		selectionIndexes = new IntVector();
	}
	
	public void mouseClicked(MouseEvent event, int bendPointIndex)
	{	
		int adjustBendPointIndex = adjustBendPointIndex(bendPointIndex);
		if (adjustBendPointIndex < 0)
			return;
		
		boolean shiftDown = event.isShiftDown();
		boolean controlDown = event.isControlDown();
		
		if (containsBendPoint(adjustBendPointIndex) && ! controlDown && ! shiftDown)
		{
			addToClearedList(adjustBendPointIndex);
			return;	
		}
	}

	public void mouseWasPressed(MouseEvent mouseEvent, int pointIndexPressed)
	{	
		int adjustBendPointIndex = adjustBendPointIndex(pointIndexPressed);
		if (adjustBendPointIndex < 0)
			return;
		
		updateSelectionList(mouseEvent, adjustBendPointIndex);
	}
	
	private int adjustBendPointIndex(int currentIndex)
	{
		return currentIndex - 1;
	}
	
	public void updateSelectionList(MouseEvent mouseEvent, int bendPointIndex)
	{	
		boolean shiftDown = mouseEvent.isShiftDown();
		boolean controlDown = mouseEvent.isControlDown();
		
		if (! containsBendPoint(bendPointIndex) && ! controlDown && ! shiftDown)
		{
			addToClearedList(bendPointIndex);
			return;
		}
		
		if (! containsBendPoint(bendPointIndex) && (controlDown || shiftDown))
		{
			addToSelectionIndexList(bendPointIndex);
			return;
		}

		if (containsBendPoint(bendPointIndex) && controlDown)
		{
			removeSelectionIndex(bendPointIndex);
			return;
		}
	}

	private boolean containsBendPoint(int bendPointIndex)
	{
		return selectionIndexes.contains(bendPointIndex);
	}

	private void addToClearedList(int bendPointIndex)
	{
		clearSelection();
		addToSelectionIndexList(bendPointIndex);
	}

	public void addToSelectionIndexList(int bendPointIndex)
	{
		selectionIndexes.add(bendPointIndex);
	}
	
	public void removeSelectionIndex(int bendPointIndex)
	{
		selectionIndexes.remove(bendPointIndex);
	}
	
	//TODO move this method to IntVector and name it toIntArray
	public int[] getSelectedIndexes()
	{
		int[] selection = new int[selectionIndexes.size()];
		for (int i = 0; i < selectionIndexes.size(); ++i)
		{
			selection[i] = selectionIndexes.get(i);
		}
		
		return selection;
	}
	
	public void selectAll()
	{
		clearSelection();
		DiagramFactorLink diagramLink = linkCell.getDiagramFactorLink();
		PointList allBendPoints = diagramLink.getBendPoints();
		for (int i = 0; i < allBendPoints.size(); ++i)
		{
			selectionIndexes.add(i);
		}
	}
	
	IntVector selectionIndexes;
	LinkCell linkCell;
}
