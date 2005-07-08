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
		mousePressedAt = event.getPoint();
		if(event.isPopupTrigger())
		{
			diagram.showContextMenu(event);
			return;
		}
	}

	public void mouseReleased(MouseEvent event)
	{
		Point mouseReleasedAt = event.getPoint();
		int deltaX = mouseReleasedAt.x - mousePressedAt.x; 
		int deltaY = mouseReleasedAt.y - mousePressedAt.y;
		diagram.mouseWasReleased(deltaX, deltaY);
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
		diagram.selectionHasChanged();
	}

	DiagramComponent diagram;
	Point mousePressedAt;
}
