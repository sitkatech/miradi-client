/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.awt.event.MouseEvent;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.cells.LinkCell;

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
	
	public void mousePressed(MouseEvent mouseEvent, int currentIndex)
	{	
		updateSelectionList(mouseEvent, currentIndex);
	}
	
	public void updateSelectionList(MouseEvent mouseEvent, int index)
	{
		Integer bendPointIndex = new Integer(index - 1);
		
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
	
	public int[] getSelectedIndexes()
	{
		int[] selection = new int[selectionIndexes.size()];
		for (int i = 0; i < selectionIndexes.size(); ++i)
		{
			selection[i] = ((Integer) selectionIndexes.get(i)).intValue();
		}
		
		return selection;
	}
	
	Vector selectionIndexes;
	LinkCell linkCell;
}
