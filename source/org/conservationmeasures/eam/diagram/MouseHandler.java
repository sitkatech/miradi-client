/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.views.diagram.NodeProperties;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;


public class MouseHandler implements MouseListener, GraphSelectionListener
{
	public MouseHandler(DiagramComponent owner, BaseProject projectToUse, Actions actionsToUse)
	{
		diagram = owner;
		project = projectToUse;
		actions = actionsToUse;
	}
	
	public void mousePressed(MouseEvent event)
	{
		dragStartedAt = null;
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
		if(event.isPopupTrigger())
		{
			diagram.showContextMenu(event);
			dragStartedAt = null;
			return;
		}

		if(dragStartedAt == null)
			return;
		
		DiagramModel model = diagram.getDiagramModel();
		Vector selectedNodes = new Vector();
		for(int i = 0; i < selectedCells.length; ++i)
		{
			if(model.isNode((EAMGraphCell)selectedCells[i]))
				selectedNodes.add(selectedCells[i]);
		}
		
		int[] selectedNodeIds = new int[selectedNodes.size()];
		for(int i = 0; i < selectedNodes.size(); ++i)
		{
			selectedNodeIds[i] = ((Node)selectedNodes.get(i)).getId();
		}

		Point dragEndedAt = event.getPoint();
		int deltaX = dragEndedAt.x - dragStartedAt.x; 
		int deltaY = dragEndedAt.y - dragStartedAt.y;
		
		if(deltaX == 0 && deltaY == 0)
			return;
		
		diagram.nodesWereMoved(deltaX, deltaY, selectedNodeIds);
	}

	public void mouseEntered(MouseEvent arg0)
	{
	}

	public void mouseExited(MouseEvent arg0)
	{
	}

	public void mouseClicked(MouseEvent event)
	{
		if(event.getClickCount() == 2)
		{
			try 
			{
				new NodeProperties(project).doIt();
			} 
			catch (CommandFailedException e) 
			{
				e.printStackTrace();
			}
			event.consume();
		}
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
		selectedCells = diagram.getSelectionCells();
		actions.updateActionStates();
	}

	BaseProject project;
	DiagramComponent diagram;
	Actions actions;
	Point dragStartedAt;
	Object[] selectedCells;
}
