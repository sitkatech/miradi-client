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
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ProjectDoer;
import org.conservationmeasures.eam.views.diagram.Properties;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;


public class MouseEventHandler implements MouseListener, GraphSelectionListener
{
	public MouseEventHandler(DiagramComponent owner, Project projectToUse, Actions actionsToUse)
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
		for(int i = 0; i < selectedCells.length; ++i)
		{
			if(((EAMGraphCell)selectedCells[i]).isNode())
			{
				DiagramNode node = (DiagramNode)selectedCells[i];
				node.setPreviousLocation(node.getLocation());
				node.setPreviousSize(node.getSize());
			}
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
		
		Vector selectedNodes = new Vector();
		for(int i = 0; i < selectedCells.length; ++i)
		{
			if(((EAMGraphCell)selectedCells[i]).isNode())
				selectedNodes.add(selectedCells[i]);
		}
		
		if(selectedNodes.size() == 0)
			return;
		
		int[] selectedNodeIds = new int[selectedNodes.size()];
		for(int i = 0; i < selectedNodes.size(); ++i)
		{
			selectedNodeIds[i] = ((DiagramNode)selectedNodes.get(i)).getId();
		}

		Point dragEndedAt = event.getPoint();
		int deltaX = dragEndedAt.x - dragStartedAt.x; 
		int deltaY = dragEndedAt.y - dragStartedAt.y;
		
		if(deltaX == 0 && deltaY == 0)
			return;

		// adjust for snap
		DiagramNode node = (DiagramNode)selectedNodes.get(0);
		deltaX = node.getLocation().x - node.getPreviousLocation().x;
		deltaY = node.getLocation().y - node.getPreviousLocation().y;
		
		project.nodesWereMovedOrResized(deltaX, deltaY, selectedNodeIds);
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
				ProjectDoer doer = new Properties(diagram);
				doer.setProject(project);
				doer.doIt();
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

	Project project;
	DiagramComponent diagram;
	Actions actions;
	Point dragStartedAt;
	Object[] selectedCells;
}
