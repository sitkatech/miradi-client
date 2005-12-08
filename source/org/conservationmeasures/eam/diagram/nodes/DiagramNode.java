/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.conservationmeasures.eam.diagram.nodes.types.NodeType;
import org.conservationmeasures.eam.diagram.nodes.types.NodeTypeDirectThreat;
import org.conservationmeasures.eam.diagram.nodes.types.NodeTypeIndirectFactor;
import org.conservationmeasures.eam.diagram.nodes.types.NodeTypeIntervention;
import org.conservationmeasures.eam.diagram.nodes.types.NodeTypeStress;
import org.conservationmeasures.eam.diagram.nodes.types.NodeTypeTarget;
import org.conservationmeasures.eam.project.IdAssigner;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

abstract public class DiagramNode extends EAMGraphCell
{
	public static DiagramNode wrapConceptualModelObject(ConceptualModelObject cmObject)
	{
		if(cmObject.isIntervention())
			return new DiagramIntervention((ConceptualModelIntervention)cmObject);
		else if(cmObject.isFactor())
			return new DiagramFactor((ConceptualModelFactor)cmObject);
		else if(cmObject.isTarget())
			return new DiagramTarget((ConceptualModelTarget)cmObject);
			
		throw new RuntimeException("Tried to wrap unknown cmObject: " + cmObject);
	}

	protected DiagramNode(ConceptualModelObject cmObjectToUse)
	{
		underlyingObject = cmObjectToUse;
		
		id = IdAssigner.INVALID_ID;
		port = new DefaultPort();
		add(port);
		setColors();
		setFont();
		setLocation(new Point(0, 0));
		Dimension defaultNodeSize = new Dimension(120, 60);
		setSize(defaultNodeSize);
		setPreviousSize(defaultNodeSize);
		setText("");
		if(canHavePriority())
			setNodePriority(ThreatPriority.createPriorityNone());
	}
	
	public boolean isNode()
	{
		return true;
	}
	
	public void setId(int idToUse)
	{
		id = idToUse;
	}

	public int getId()
	{
		return id;
	}

	public NodeType getType()
	{
		return underlyingObject.getType();
	}

	public void setType(NodeType typeToUse)
	{
		underlyingObject.setType(typeToUse);
		setColors();
	}

	public ThreatPriority getThreatPriority()
	{
		return underlyingObject.getThreatPriority();
	}
	
	public void setNodePriority(ThreatPriority priorityToUse)
	{
		underlyingObject.setNodePriority(priorityToUse);
	}
	
	public boolean canHavePriority()
	{
		return underlyingObject.canHavePriority();
	}
	
	public Indicator getIndicator()
	{
		return underlyingObject.getIndicator();
	}
	
	public void setIndicator(Indicator indicatorToUse)
	{
		underlyingObject.setIndicator(indicatorToUse);
	}

	public boolean canHaveObjectives()
	{
		return underlyingObject.canHaveObjectives();
	}

	public boolean canHaveGoal()
	{
		return underlyingObject.canHaveGoal();
	}

	public Goals getGoals()
	{
		return underlyingObject.getGoals();
	}
	
	public boolean isFactor()
	{
		return underlyingObject.isFactor();
	}

	public Point getLocation()
	{
		Rectangle2D bounds = GraphConstants.getBounds(getAttributes());
		if(bounds == null)
			return new Point(0, 0);
		
		return new Point((int)bounds.getX(), (int)bounds.getY());
	}

	public void setLocation(Point2D snappedLocation)
	{
		Rectangle2D bounds = GraphConstants.getBounds(getAttributes());
		if(bounds == null)
			bounds = new Rectangle(0, 0, 0, 0);
		
		double width = bounds.getWidth();
		double height = bounds.getHeight();
		Dimension size = new Dimension((int)width, (int)height);
		Point location = new Point((int)snappedLocation.getX(), (int)snappedLocation.getY());
		GraphConstants.setBounds(getAttributes(), new Rectangle(location, size));
	}
	
	public void setText(String text)
	{
		GraphConstants.setValue(getAttributes(), text);
	}

	public String getText()
	{
		return (String)GraphConstants.getValue(getAttributes());
	}

	public void setGoals(Goals goalsToUse)
	{
		underlyingObject.setGoals(goalsToUse);
	}

	public Objectives getObjectives()
	{
		return underlyingObject.getObjectives();
	}

	public void setObjectives(Objectives objectivesToUse)
	{
		underlyingObject.setObjectives(objectivesToUse);
	}
	
	public int getAnnotationRows()
	{
		int numberOfAnnotations = 0;
		numberOfAnnotations = getAnnotationCount(getObjectives()) + getAnnotationCount(getGoals());

		if(getIndicator() != null && getIndicator().hasIndicator() && numberOfAnnotations == 0)
			numberOfAnnotations = 1;

		return numberOfAnnotations;
	}

	private int getAnnotationCount(NodeAnnotations annotation)
	{
		int numberOfAnnotations = 0;
		if(annotation != null)
		{
			for(int i = 0; i < annotation.size(); ++i)
			{
				if(annotation.getAnnotation(i).hasAnnotation())
					++numberOfAnnotations;
			}
		}
		return numberOfAnnotations;
	}
	
	abstract public Color getColor();

	public boolean isTarget()
	{
		return underlyingObject.isTarget();
	}
	
	public boolean isIndirectFactor()
	{
		return underlyingObject.isIndirectFactor();
	}
	
	public boolean isDirectThreat()
	{
		return underlyingObject.isDirectThreat();
	}
	
	public boolean isStress()
	{
		return underlyingObject.isStress();
	}
	
	public boolean isIntervention()
	{
		return underlyingObject.isIntervention();
	}
	
	public DefaultPort getPort()
	{
		return port;
	}
	
	private void setFont()
	{
//		Font font = originalFont.deriveFont(Font.BOLD);
//		GraphConstants.setFont(map, font);
	}

	private void setColors()
	{
		Color color = getColor();
		GraphConstants.setBorderColor(getAttributes(), Color.black);
		GraphConstants.setBackground(getAttributes(), color);
		GraphConstants.setForeground(getAttributes(), Color.black);
		GraphConstants.setOpaque(getAttributes(), true);
	}
	
	public Dimension getSize()
	{
		return getBounds().getBounds().getSize();
	}
	
	public void setSize(Dimension size)
	{
		Point location = getLocation();
		Rectangle bounds = new Rectangle(location, size);
		GraphConstants.setBounds(getAttributes(), bounds);
	}
	
	public Dimension getPreviousSize()
	{
		return previousSize;
	}
	
	public void setPreviousSize(Dimension size)
	{
		previousSize = size;
	}
	
	public Point getPreviousLocation()
	{
		return previousLocation;
	}

	public void setPreviousLocation(Point location)
	{
		previousLocation = location;
	}
	
	public Rectangle2D getBounds()
	{
		return GraphConstants.getBounds(getAttributes());
	}
	
	public boolean hasMoved()
	{
		return !getLocation().equals(getPreviousLocation());
	}
	
	public boolean sizeHasChanged()
	{
		return !getSize().equals(getPreviousSize());
	}
	
	public NodeDataMap createNodeDataMap()
	{
		NodeDataMap dataMap = new NodeDataMap();
		dataMap.putNodeType(getType());
		dataMap.putInt(TAG_ID, getId());
		dataMap.putPoint(TAG_LOCATION, getLocation());
		dataMap.putString(TAG_VISIBLE_LABEL, getText());
		
		int priorityValue = getThreatPriority().getValue();
		dataMap.putInt(TAG_PRIORITY, priorityValue);
		return dataMap;
	}
	
	public static final NodeType TYPE_INVALID = null;
	public static final NodeType TYPE_TARGET = new NodeTypeTarget();
	public static final NodeType TYPE_INDIRECT_FACTOR = new NodeTypeIndirectFactor();
	public static final NodeType TYPE_INTERVENTION = new NodeTypeIntervention();
	public static final NodeType TYPE_DIRECT_THREAT = new NodeTypeDirectThreat();
	public static final NodeType TYPE_STRESS = new NodeTypeStress();

	public static final int INT_TYPE_INVALID = -1;
	public static final int INT_TYPE_TARGET = 1;
	public static final int INT_TYPE_INDIRECT_FACTOR = 2;
	public static final int INT_TYPE_INTERVENTION = 3;
	public static final int INT_TYPE_DIRECT_THREAT = 4;
	public static final int INT_TYPE_STRESS = 5;

	public static final String TAG_ID = "id";
	public static final String TAG_LOCATION = "location";
	public static final String TAG_NODE_TYPE = "NodeType";
	public static final String TAG_PRIORITY = "Priority";
	public static final String TAG_VISIBLE_LABEL = "text";

	DefaultPort port;
	Dimension previousSize;
	Point previousLocation;
	
	ConceptualModelObject underlyingObject;

	protected int id;
}

