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
		int adjustBendPointIndex = getBendPointIndexForControlPointIndex(bendPointIndex);
		
		if (adjustBendPointIndex < 0)
			return;
		
		if (event.isControlDown())
			return;
		
		if (event.isShiftDown())
			return;
		
		if (! selectionIndexes.contains(bendPointIndex))
			return;

		clearSelection();
		selectionIndexes.add(adjustBendPointIndex);
	}

	public void mouseWasPressed(MouseEvent mouseEvent, int pointIndexPressed)
	{	
		int adjustBendPointIndex = getBendPointIndexForControlPointIndex(pointIndexPressed);
		if (adjustBendPointIndex < 0)
			return;
		
		updateSelectionList(mouseEvent, adjustBendPointIndex);
	}
	
	private int getBendPointIndexForControlPointIndex(int currentIndex)
	{
		return currentIndex - 1;
	}
	
	public void updateSelectionList(MouseEvent mouseEvent, int bendPointIndex)
	{	
		boolean shiftDown = mouseEvent.isShiftDown();
		boolean controlDown = mouseEvent.isControlDown();
		boolean contains = selectionIndexes.contains(bendPointIndex);
		
		if (contains)
			updateSelectionWasAlreadySelected(bendPointIndex, controlDown);
		else
			updateSelectionWasNotAlreadySelected(bendPointIndex, controlDown, shiftDown);
	}
	
	private void updateSelectionWasNotAlreadySelected(int bendPointIndex, boolean controlDown, boolean shiftDown)
	{
		if (!controlDown && !shiftDown)
			clearSelection();
		
		selectionIndexes.add(bendPointIndex);
	}

	private void updateSelectionWasAlreadySelected(int bendPointIndex, boolean controlDown)
	{
		if (! controlDown)
			return;
		
		removeSelectionIndex(bendPointIndex);
	}

	public void removeSelectionIndex(int bendPointIndex)
	{
		selectionIndexes.remove(bendPointIndex);
	}

	public void addToSelectionIndexList(int bendPointIndex)
	{
		selectionIndexes.add(bendPointIndex);
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
