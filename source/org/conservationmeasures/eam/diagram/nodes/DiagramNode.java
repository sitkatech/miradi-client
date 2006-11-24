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
import java.text.ParseException;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandSetNodeSize;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.diagram.renderers.MultilineCellRenderer;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objects.ConceptualModelCluster;
import org.conservationmeasures.eam.objects.ConceptualModelFactor;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ConceptualModelTarget;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DataMap;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

abstract public class DiagramNode extends EAMGraphCell
{
	public static DiagramNode wrapConceptualModelObject(DiagramNodeId idToUse, ConceptualModelNode cmObject)
	{
		if(cmObject.isIntervention())
			return new DiagramIntervention(idToUse, (ConceptualModelIntervention)cmObject);
		else if(cmObject.isFactor())
			return new DiagramFactor(idToUse, (ConceptualModelFactor)cmObject);
		else if(cmObject.isTarget())
			return new DiagramTarget(idToUse, (ConceptualModelTarget)cmObject);
		else if(cmObject.isCluster())
			return new DiagramCluster(idToUse, (ConceptualModelCluster)cmObject);
			
		throw new RuntimeException("Tried to wrap unknown cmObject: " + cmObject);
	}
	
	public static DiagramNode createFromJson(Project project, EnhancedJsonObject json) throws Exception
	{
		DiagramNodeId id = new DiagramNodeId(json.getId(TAG_ID).asInt());
		ModelNodeId wrappedId = new ModelNodeId(json.getId(TAG_WRAPPED_ID).asInt());
		ConceptualModelNode cmNode = project.findNode(wrappedId);
		DiagramNode node = wrapConceptualModelObject(id, cmNode);
		node.fillFrom(json);
		return node;
	}

	protected DiagramNode(DiagramNodeId idToUse, ConceptualModelNode cmObjectToUse)
	{
		underlyingObject = cmObjectToUse;
		id = idToUse;
		
		port = new DefaultPort();
		add(port);
		setColors();
		setFont();
		setLocation(new Point(0, 0));
		Dimension defaultNodeSize = getDefaultSize();
		setSize(defaultNodeSize);
		setPreviousSize(defaultNodeSize);
	}
	
	public Rectangle getRectangle()
	{
		return new Rectangle(getLocation(), getSize());
	}

	public static Dimension getDefaultSize()
	{
		return new Dimension(120, 60);
	}
	
	public boolean isNode()
	{
		return true;
	}
	
	public ConceptualModelNode getUnderlyingObject()
	{
		return underlyingObject;
	}
	
	public DiagramNodeId getDiagramNodeId()
	{
		return id;
	}

	public int getType()
	{
		return getWrappedType();
	}
	
	public ModelNodeId getWrappedId()
	{
		return underlyingObject.getModelNodeId();
	}
	
	public int getWrappedType()
	{
		return getUnderlyingObject().getType();
	}
	
	public String getLabel()
	{
		return underlyingObject.getLabel();
	}
	
	public void setLabel(String name) throws Exception
	{
		underlyingObject.setLabel(name);
	}

	public NodeType getNodeType()
	{
		return underlyingObject.getNodeType();
	}

	public void setNodeType(NodeType typeToUse)
	{
		underlyingObject.setNodeType(typeToUse);
		setColors();
	}
	
	public boolean isStatusDraft()
	{
		return underlyingObject.isStatusDraft();
	}

	public IdList getIndicators()
	{
		return underlyingObject.getIndicators();
	}
	
	public boolean canHaveObjectives()
	{
		return underlyingObject.canHaveObjectives();
	}

	public boolean canHaveGoal()
	{
		return underlyingObject.canHaveGoal();
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
	
	public String toString()
	{
		return getLabel();
	}
	
	public String getComment()
	{
		return underlyingObject.getComment();
	}

	public IdList getGoals()
	{
		return underlyingObject.getGoals();
	}
	
	public IdList getObjectives()
	{
		return underlyingObject.getObjectives();
	}

	public int getAnnotationRows()
	{
		if(getGoals().size() > 0 || getObjectives().size() > 0 || getIndicators().size() > 0)
			return 1;

		return 0;
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
	
	public boolean isCluster()
	{
		return underlyingObject.isCluster();
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
		GraphConstants.setBorderColor(getAttributes(), Color.black);
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
	
	public boolean isPointInObjective(Point p)
	{
		if(getObjectives().size() == 0)
			return false;
		return getAnnotationsRect().contains(p);
	}
	
	public boolean isPointInIndicator(Point p)
	{
		return getIndicatorRectWithinNode().contains(p);
	}
	
	public boolean isPointInGoal(Point p)
	{
		if(getGoals().size() == 0)
			return false;
		return getAnnotationsRect().contains(p);
	}
	
	public Rectangle getAnnotationsRect()
	{
		
		Rectangle rect = new Rectangle(getSize());
		
		Rectangle annotationsRectangle = new Rectangle();
		int annotationLeftInset = getAnnotationLeftOffset();
		annotationsRectangle.x = rect.x + annotationLeftInset;
		int annotationsHeight = getAnnotationRows() * MultilineCellRenderer.ANNOTATIONS_HEIGHT;
		annotationsRectangle.y = rect.y + (rect.height - annotationsHeight);
		int avoidGoingPastClippingEdge = 1;
		annotationsRectangle.width = rect.width - annotationLeftInset - getAnnotationRightInset() - avoidGoingPastClippingEdge;
		annotationsRectangle.height = annotationsHeight - avoidGoingPastClippingEdge;
		return annotationsRectangle;
	}

	int getAnnotationLeftOffset()
	{
		if(getIndicators().size() == 0)
			return MultilineCellRenderer.INDICATOR_WIDTH / 2;
		
		int indicatorOffset = getInsetDimension().width - MultilineCellRenderer.INDICATOR_WIDTH / 2;
		if(indicatorOffset < 0)
			indicatorOffset = 0;
		int annotationOffset = indicatorOffset + MultilineCellRenderer.INDICATOR_WIDTH;
		return annotationOffset;
	}
	
	int getAnnotationRightInset()
	{
		return MultilineCellRenderer.INDICATOR_WIDTH / 2;
	}

	public Dimension getInsetDimension()
	{
		return new Dimension(0, 0);
	}
	
	public Rectangle getIndicatorRectWithinNode()
	{
		Rectangle annotationsRectangle = getAnnotationsRect();
		Rectangle smallTriangle = new Rectangle();
		smallTriangle.x = annotationsRectangle.x-MultilineCellRenderer.INDICATOR_WIDTH;
		smallTriangle.y = annotationsRectangle.y;
		int avoidGoingPastClippingEdge = 1;
		smallTriangle.width = MultilineCellRenderer.INDICATOR_WIDTH - avoidGoingPastClippingEdge;
		smallTriangle.height = MultilineCellRenderer.INDICATOR_HEIGHT - avoidGoingPastClippingEdge;
		return smallTriangle;
	}

	public Command[] buildCommandsToClear()
	{
		int x = getLocation().x;
		int y = getLocation().y;
		return new Command[] {
			new CommandSetNodeSize(getDiagramNodeId(), getDefaultSize(), getSize()),
			new CommandDiagramMove(-x, -y, new DiagramNodeId[] {getDiagramNodeId()}),
			new CommandSetObjectData(getWrappedType(), getWrappedId(), TAG_VISIBLE_LABEL, EAMBaseObject.DEFAULT_LABEL),
		};
	}
	
	public NodeDataMap createNodeDataMap()
	{
		NodeDataMap dataMap = new NodeDataMap();
		dataMap.putId(TAG_ID, getDiagramNodeId());
		dataMap.putId(TAG_WRAPPED_ID, getWrappedId());
		
		// FIXME: This is a crude hack, to preserve the node type information
		// here so we can re-create the node if it gets pasted. 
		// Really, for each node copied to the clipboard, we should copy 
		// the json for both the ConceptualModelNode and for the DiagramNode.
		// That will also fix the current bug that objectives and goals are not copied
		dataMap.put(TAG_NODE_TYPE, NodeDataMap.convertNodeTypeToInt(getNodeType()));
		
		
		dataMap.putPoint(TAG_LOCATION, getLocation());
		dataMap.putDimension(TAG_SIZE, getSize());
		dataMap.putString(TAG_VISIBLE_LABEL, getLabel());
		
		return dataMap;
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject dataMap = new DataMap();
		dataMap.putId(TAG_ID, getDiagramNodeId());
		dataMap.putId(TAG_WRAPPED_ID, getWrappedId());
		dataMap.putPoint(TAG_LOCATION, getLocation());
		dataMap.putDimension(TAG_SIZE, getSize());
		return dataMap;
	}
	
	public void fillFrom(EnhancedJsonObject json) throws ParseException
	{
		NodeDataMap dataMap = new NodeDataMap(json);
		setLocation(dataMap.getPoint(TAG_LOCATION));
		setSize(dataMap.getDimension(TAG_SIZE));
	}
	
	public static final String TAG_ID = "Id";
	public static final String TAG_WRAPPED_ID = "WrappedId";
	public static final String TAG_LOCATION = "Location";
	public static final String TAG_SIZE = "Size";

	// FIXME: cut/copy/paste need to be overhauled, because right now they
	// only memorize a small subset of all available data (e.g. label but not 
	// comments, objectives, etc.)
	public static final String TAG_VISIBLE_LABEL = "Label";

	public static final String TAG_NODE_TYPE = "NodeType";

	DefaultPort port;
	Dimension previousSize;
	Point previousLocation;
	
	DiagramNodeId id;
	ConceptualModelNode underlyingObject;
}

