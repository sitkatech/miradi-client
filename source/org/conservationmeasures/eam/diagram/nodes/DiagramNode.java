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
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeCluster;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeFactor;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIntervention;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.diagram.renderers.MultilineCellRenderer;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.GoalIds;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.ids.NodeAnnotationIds;
import org.conservationmeasures.eam.ids.ObjectiveIds;
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

	public BaseId getIndicatorId()
	{
		return underlyingObject.getIndicatorId();
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

	public GoalIds getGoals()
	{
		return underlyingObject.getGoals();
	}
	
	public ObjectiveIds getObjectives()
	{
		return underlyingObject.getObjectives();
	}

	public int getAnnotationRows()
	{
		int numberOfAnnotations = 0;
		numberOfAnnotations = getAnnotationCount(getObjectives()) + getAnnotationCount(getGoals());

		if(!getIndicatorId().isInvalid() && numberOfAnnotations == 0)
			numberOfAnnotations = 1;

		return numberOfAnnotations;
	}

	private int getAnnotationCount(NodeAnnotationIds annotations)
	{
		int numberOfAnnotations = 0;
		if(annotations != null)
		{
			for(int i = 0; i < annotations.size(); ++i)
			{
				if(annotations.getId(i).asInt() >= 0)
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
		return getAnnotationsRect(getGoals().size()).contains(p);
	}
	
	abstract public Rectangle getAnnotationsRect();
	
	public Rectangle getAnnotationsRect(int numberLines)
	{
		if(numberLines == 0 && !getIndicatorId().isInvalid())
			numberLines = 1;
		
		Rectangle rect = new Rectangle(getSize());
		
		Rectangle annotationsRectangle = new Rectangle();
		int annotationLeftInset = getAnnotationLeftOffset();
		annotationsRectangle.x = rect.x + annotationLeftInset;
		int annotationsHeight = numberLines * MultilineCellRenderer.ANNOTATIONS_HEIGHT;
		annotationsRectangle.y = rect.y + (rect.height - annotationsHeight);
		int avoidGoingPastClippingEdge = 1;
		annotationsRectangle.width = rect.width - annotationLeftInset - getAnnotationRightInset() - avoidGoingPastClippingEdge;
		annotationsRectangle.height = annotationsHeight - avoidGoingPastClippingEdge;
		return annotationsRectangle;
	}

	int getAnnotationLeftOffset()
	{
		if(getIndicatorId().isInvalid())
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
			new CommandDiagramMove(-x, -y, new BaseId[] {getDiagramNodeId()}),
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
	
	public void fillFrom(Project project, EnhancedJsonObject json) throws ParseException
	{
		NodeDataMap dataMap = new NodeDataMap(json);
		id = new DiagramNodeId(dataMap.getId(TAG_ID).asInt());
		setLocation(dataMap.getPoint(TAG_LOCATION));
		setSize(dataMap.getDimension(TAG_SIZE));
		
		BaseId wrappedId = json.optId(TAG_WRAPPED_ID);
		underlyingObject = project.findNode(wrappedId);
	}
	
	public static final NodeType TYPE_INVALID = null;
	public static final NodeType TYPE_TARGET = new NodeTypeTarget();
	public static final NodeType TYPE_FACTOR = new NodeTypeFactor();
	public static final NodeType TYPE_INTERVENTION = new NodeTypeIntervention();
	public static final NodeType TYPE_CLUSTER = new NodeTypeCluster();

	public static final int INT_TYPE_INVALID = -1;
	public static final int INT_TYPE_TARGET = 1;
	public static final int INT_TYPE_INDIRECT_FACTOR = 2;
	public static final int INT_TYPE_INTERVENTION = 3;
	public static final int INT_TYPE_DIRECT_THREAT = 4;
	public static final int INT_TYPE_CLUSTER = 5;

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

