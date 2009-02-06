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
package org.miradi.diagram;

import java.awt.Point;
import java.awt.event.MouseEvent;

import org.miradi.diagram.cells.LinkCell;
import org.miradi.objects.DiagramLink;
import org.miradi.utils.IntVector;
import org.miradi.utils.PointList;

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
	
	public void mouseClicked(MouseEvent event, int controlPointIndex)
	{	
		int bendPointIndex = getBendPointIndexForControlPointIndex(controlPointIndex);
		
		if (bendPointIndex < 0)
			return;
		
		if (event.isControlDown())
			return;
		
		if (event.isShiftDown())
			return;
		
		if (! selectionIndexes.contains(bendPointIndex))
			return;

		clearSelection();
		selectionIndexes.add(bendPointIndex);
	}

	public boolean mouseWasPressed(MouseEvent mouseEvent, int controlPointIndex)
	{	
		int bendPointIndex = getBendPointIndexForControlPointIndex(controlPointIndex);
		if (bendPointIndex < 0)
			return false;
		
		updateSelectionList(mouseEvent, bendPointIndex);
		return true;
	}
	
	private int getBendPointIndexForControlPointIndex(int currentIndex)
	{
		return currentIndex - 1;
	}
	
	public void updateSelectionList(MouseEvent mouseEvent, int bendPointIndex)
	{	
		boolean contains = selectionIndexes.contains(bendPointIndex);
		
		if (contains)
			updateSelectionWasAlreadySelected(bendPointIndex, mouseEvent);
		else
			updateSelectionWasNotAlreadySelected(bendPointIndex, mouseEvent);
	}
	
	private void updateSelectionWasNotAlreadySelected(int bendPointIndex, MouseEvent event)
	{
		if (!event.isControlDown() && !event.isShiftDown())
			clearSelection();
		
		selectionIndexes.add(bendPointIndex);
	}

	private void updateSelectionWasAlreadySelected(int bendPointIndex, MouseEvent event)
	{
		if (! event.isControlDown())
			return;
		
		removeSelectionIndex(bendPointIndex);
	}

	public void removeSelectionIndex(int bendPointIndex)
	{
		selectionIndexes.remove(bendPointIndex);
	}

	public void addToSelectionIndexList(int bendPointIndex)
	{
		if (selectionIndexes.contains(bendPointIndex))
			return;
		
		selectionIndexes.add(bendPointIndex);
	}
	
	public void addToSelection(Point pointToSelect)
	{
		PointList bendPoints = linkCell.getDiagramLink().getBendPoints();
		for (int i = 0; i < bendPoints.size(); ++i)
		{
			Point point = bendPoints.get(i);
			if (point.equals(pointToSelect))
				addToSelectionIndexList(i);
		}
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
		DiagramLink diagramLink = linkCell.getDiagramLink();
		PointList allBendPoints = diagramLink.getBendPoints();
		for (int i = 0; i < allBendPoints.size(); ++i)
		{
			selectionIndexes.add(i);
		}
	}
	
	private IntVector selectionIndexes;
	private LinkCell linkCell;
}
