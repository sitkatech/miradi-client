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
import org.conservationmeasures.eam.main.EAM;
import org.jgraph.graph.GraphConstants;

public class ProjectScopeBox extends EAMGraphCell implements DiagramModelListener
{
	public ProjectScopeBox(DiagramModel modelToUse)
	{
		model = modelToUse;
		autoSurroundTargets();
		vision = "";
		
		setText(EAM.text("Project Scope"));
		GraphConstants.setBorderColor(getAttributes(), Color.black);
		GraphConstants.setBackground(getAttributes(), PROJECT_SCOPE_BOX_COLOR);
		GraphConstants.setForeground(getAttributes(), Color.black);
		GraphConstants.setOpaque(getAttributes(), true);

		model.addDiagramModelListener(this);
	}
	
	public void setText(String text)
	{
		setUserObject(text);
	}

	public String getText()
	{
		return (String)getUserObject();
	}

	public boolean isProjectScope()
	{
		return true;
	}
	
	public boolean hasVision()
	{
		return vision.length()>0;
	}
	
	public String getVision()
	{
		return vision;
	}
	
	public void setVision(String visionStatement)
	{
		vision = visionStatement;
		autoSurroundTargets();
	}
	
	public Rectangle2D getBounds()
	{
		return GraphConstants.getBounds(getAttributes());
	}
	
	private void autoSurroundTargets()
	{
		Rectangle2D targetBounds = computeCurrentTargetBounds();
		Rectangle newBounds = new Rectangle(0,0,0,0);
		if(!targetBounds.equals(newBounds))
		{
			Point location = new Point((int)targetBounds.getX() - SIDE_MARGIN, (int)targetBounds.getY() - TOP_MARGIN);
			Dimension size = new Dimension((int)targetBounds.getWidth() + 2*SIDE_MARGIN, (int)targetBounds.getHeight() + TOP_MARGIN + BOTTOM_MARGIN);
			newBounds = new Rectangle(location, size);
		}
		
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
					bounds = (Rectangle2D)node.getBounds().clone();

				Rectangle tempRect = new Rectangle();
				Rectangle.union(bounds, node.getBounds(), tempRect);
				bounds = tempRect;
			}
		}
		
		if(bounds == null)
			return new Rectangle();

		double height = bounds.getHeight();
		if (hasVision())
			height += VISION_HEIGHT;
		
		Rectangle result = new Rectangle();
		result.setRect(bounds.getX(), bounds.getY(), bounds.getWidth(), height);
		return result;
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

	final static Color PROJECT_SCOPE_BOX_COLOR = new Color(0, 255, 0);
	final static int SIDE_MARGIN = 5;
	final static int TOP_MARGIN = 20;
	final static int BOTTOM_MARGIN = 5;
	final static int VISION_HEIGHT = 40;
	
	DiagramModel model;
	
	private String vision;
}
