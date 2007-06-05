/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.awt.event.MouseEvent;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
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
		selectionIndexes = new Vector();
	}
	
	public void mouseWasPressed(MouseEvent mouseEvent, int pointIndexPressed)
	{	
		updateSelectionList(mouseEvent, pointIndexPressed);
	}
	
	public void updateSelectionList(MouseEvent mouseEvent, int pointIndexPressed)
	{
		Integer bendPointIndex = new Integer(pointIndexPressed - 1);
		
		if (bendPointIndex.intValue() < 0)
			return;
		
		boolean shiftDown = mouseEvent.isShiftDown();
		//FIXME: Nima: all isShiftDown()s should be isControlDown()s (do the test for this class as well)
		if (! selectionIndexes.contains(bendPointIndex) && ! shiftDown)
		{
			clearSelection();
			addToSelectionIndexList(bendPointIndex);
			return;
		}
		
		//FIXME: Nima: all isShiftDown()s should be isControlDown()s (do the test for this class as well)
		if (! selectionIndexes.contains(bendPointIndex) && shiftDown)
		{
			addToSelectionIndexList(bendPointIndex);
			return;
		}

		//FIXME: Nima: all isShiftDown()s should be isControlDown()s (do the test for this class as well)
		if (selectionIndexes.contains(bendPointIndex) && shiftDown)
		{
			selectionIndexes.remove(bendPointIndex);
			return;
		}
	}

	public void addToSelectionIndexList(Integer bendPointIndex)
	{
		selectionIndexes.add(bendPointIndex);
	}
	
	public void removeSelectionIndex(int bendPointIndex)
	{
		selectionIndexes.remove(new Integer(bendPointIndex));
	}
	
	public int[] getSelectedIndexes()
	{
		int[] selection = new int[selectionIndexes.size()];
		for (int i = 0; i < selectionIndexes.size(); ++i)
		{
			selection[i] = ((Integer) selectionIndexes.get(i)).intValue();
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
			selectionIndexes.add(new Integer(i));
		}
	}
	
	Vector selectionIndexes;
	LinkCell linkCell;
}
