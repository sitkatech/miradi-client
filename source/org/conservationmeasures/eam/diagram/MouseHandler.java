/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;


public class MouseHandler implements MouseListener, GraphSelectionListener
{
	public MouseHandler(DiagramComponent owner)
	{
		diagram = owner;
	}
	
	public void mousePressed(MouseEvent event)
	{
		if(event.isPopupTrigger())
		{
			diagram.showContextMenu(event);
			return;
		}

		dragStartedAt = event.getPoint();
		Object cellBeingPressed = diagram.getFirstCellForLocation(dragStartedAt.getX(), dragStartedAt.getY());
		if(cellBeingPressed == null)
		{
			dragStartedAt = null;
			return;
		}
		
	}

	public void mouseReleased(MouseEvent event)
	{
		if(dragStartedAt == null)
			return;
		
		int[] selectedIds = new int[selectedCells.length];
		for(int i = 0; i < selectedCells.length; ++i)
			selectedIds[i] = diagram.getDiagramModel().getNodeId((Node)selectedCells[i]);
		
		Point dragEndedAt = event.getPoint();
		int deltaX = dragEndedAt.x - dragStartedAt.x; 
		int deltaY = dragEndedAt.y - dragStartedAt.y;
		
		if(deltaX == 0 && deltaY == 0)
			return;
		
		diagram.nodesWereMoved(deltaX, deltaY, selectedIds);
	}

	public void mouseEntered(MouseEvent arg0)
	{
	}

	public void mouseExited(MouseEvent arg0)
	{
	}

	public void mouseClicked(MouseEvent event)
	{
	}

	// valueChanged is part of the GraphSelectionListener interface.
	// It is HORRIBLY named, so we delegate to a better-named method.
	// Don't put any code in this method. Put it in selectionChanged.
	public void valueChanged(GraphSelectionEvent event)
	{
		selectionChanged(event);
	}
	
	public void selectionChanged(GraphSelectionEvent event)
	{
		selectedCells = event.getCells();
	}

	DiagramComponent diagram;
	Point dragStartedAt;
	Object[] selectedCells;
}
