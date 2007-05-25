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
		linkCell.getDiagramFactorLink().getBendPoints();
		
		if (bendPointIndex.intValue() < 0)
			return;
		
		//FIXME all isShiftDown()s should be isControlDown()s (do the test for this class as well)
		if (! selectionIndexes.contains(bendPointIndex) && ! mouseEvent.isShiftDown())
		{
			clearSelection();
			selectionIndexes.add(bendPointIndex);
			return;
		}
		
		//FIXME all isShiftDown()s should be isControlDown()s (do the test for this class as well)
		if (! selectionIndexes.contains(bendPointIndex) && mouseEvent.isShiftDown())
		{
			selectionIndexes.add(bendPointIndex);
			return;
		}
		
		//FIXME all isShiftDown()s should be isControlDown()s (do the test for this class as well)
		if (selectionIndexes.contains(bendPointIndex) && mouseEvent.isShiftDown())
		{
			selectionIndexes.remove(bendPointIndex);
			return;
		}
		
		//FIXME all isShiftDown()s should be isControlDown()s (do the test for this class as well)
		if (selectionIndexes.contains(bendPointIndex) && ! mouseEvent.isShiftDown())
		{
			clearSelection();
			selectionIndexes.add(bendPointIndex);
			return;
		}
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
