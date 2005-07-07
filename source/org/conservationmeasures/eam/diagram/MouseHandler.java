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
		if(selectedNodes == null)
		{
			EAM.logDebug("No nodes");
			return;
		}

		Point dragEndedAt = event.getPoint();
		int deltaX = dragEndedAt.x - dragStartedAt.x; 
		int deltaY = dragEndedAt.y - dragStartedAt.y;
		EAM.logDebug("dragged " + deltaX + ", " + deltaY);
	}

	public void mouseClicked(MouseEvent event)
	{
		EAM.logDebug("MouseHandler.mouseClicked");
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
		EAM.logDebug("MouseHandler.valueChanged (selection changed)");
		selectedNodes = diagram.getSelectionCells();
	}
	
	DiagramComponent diagram;
	Object[] selectedNodes;
	Point dragStartedAt;
}
