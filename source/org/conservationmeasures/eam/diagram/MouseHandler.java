/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.conservationmeasures.eam.main.EAM;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;


public class MouseHandler implements MouseListener, MouseMotionListener, GraphSelectionListener
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
		EAM.logDebug("MouseHandler.mousePressed at " + dragStartedAt);
	}

	public void mouseReleased(MouseEvent event)
	{
		EAM.logDebug("MouseHandler.mouseReleased");
		Point dragEndedAt = event.getPoint();
		int deltaX = dragEndedAt.x - dragStartedAt.x; 
		int deltaY = dragEndedAt.y - dragStartedAt.y;
		EAM.logDebug("dragged " + deltaX + ", " + deltaY);
	}

	public void mouseClicked(MouseEvent event)
	{
	}

	public void mouseEntered(MouseEvent event)
	{
	}

	public void mouseExited(MouseEvent event)
	{
	}

	public void mouseDragged(MouseEvent event)
	{
	}
	
	public void mouseMoved(MouseEvent event)
	{
	}

	public void valueChanged(GraphSelectionEvent event)
	{
		selectionChanged(event);
	}
	
	public void selectionChanged(GraphSelectionEvent event)
	{
		EAM.logDebug("MouseHandler.selectionChanged");
		Object[] selectedNodes = diagram.getSelectionCells();
		if(selectedNodes == null)
			selectedNodes = new Object[0];

		String selection = "";
		int[] selectedIds = new int[selectedNodes.length];
		for(int i=0; i < selectedNodes.length; ++i)
		{
			selectedIds[i] = diagram.getDiagramModel().getNodeId((Node)selectedNodes[i]);
			selection += selectedIds[i] + ",";
		}
		EAM.logDebug("Selected: " + selection);
	}
	
	DiagramComponent diagram;
	Point dragStartedAt;
}
