/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.main.EAM;
import org.jgraph.graph.GraphConstants;

public class ProjectScopeBox extends EAMGraphCell implements DiagramModelListener
{
	public ProjectScopeBox(DiagramModel modelToUse)
	{
		model = modelToUse;
		autoSurroundTargets();
		
		setText("Project Scope");
		Color color = new Color(191, 255, 191);
		GraphConstants.setBorderColor(getAttributes(), Color.black);
		GraphConstants.setBackground(getAttributes(), color);
		GraphConstants.setForeground(getAttributes(), Color.black);
		GraphConstants.setOpaque(getAttributes(), true);

		model.addDiagramModelListener(this);
	}
	
	public Rectangle2D getBounds()
	{
		return GraphConstants.getBounds(getAttributes());
	}
	
	private void autoSurroundTargets()
	{
		Rectangle2D targetBounds = computeCurrentTargetBounds();
		Point location = new Point((int)targetBounds.getX() - SIDE_MARGIN, (int)targetBounds.getY() - TOP_MARGIN);
		Dimension size = new Dimension((int)targetBounds.getWidth() + 2*SIDE_MARGIN, (int)targetBounds.getHeight() + TOP_MARGIN + BOTTOM_MARGIN);
		Rectangle newBounds = new Rectangle(location, size);
		EAM.logVerbose("ProjectScopeBox.autoSurroundTargets: " + newBounds.toString());
		GraphConstants.setBounds(getAttributes(), newBounds);
		Hashtable nest = new Hashtable();
		nest.put(this, getAttributes());
		model.edit(nest, null, null, null);
	}
	
	public Rectangle2D computeCurrentTargetBounds()
	{
		Rectangle2D bounds = null;
		Vector nodes = model.getAllNodes();
		for(int i=0; i < nodes.size(); ++i)
		{
			DiagramNode node = (DiagramNode)nodes.get(i);
			if(node.isTarget())
			{
				if(bounds == null)
				{
					bounds = node.getBounds();
				}
				else
				{
					Rectangle result = new Rectangle();
					Rectangle.union(bounds, node.getBounds(), result);
					bounds = result;
				}
			}
		}
		
		if(bounds == null)
			bounds = new Rectangle();

		return bounds;
	}

	public void nodeAdded(DiagramModelEvent event)
	{
		autoSurroundTargets();
	}

	public void nodeChanged(DiagramModelEvent event)
	{
	}

	public void nodeDeleted(DiagramModelEvent event)
	{
		autoSurroundTargets();
	}

	public void nodeMoved(DiagramModelEvent event)
	{
		autoSurroundTargets();
	}

	public void linkageAdded(DiagramModelEvent event)
	{
	}

	public void linkageDeleted(DiagramModelEvent event)
	{
	}

	final static int SIDE_MARGIN = 5;
	final static int TOP_MARGIN = 20;
	final static int BOTTOM_MARGIN = 5;
	
	DiagramModel model;
}
