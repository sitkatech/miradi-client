/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.ParseException;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandSetFactorSize;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.diagram.renderers.MultilineCellRenderer;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objects.FactorCluster;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DataMap;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

abstract public class DiagramFactor extends EAMGraphCell
{
	public static DiagramFactor wrapConceptualModelObject(DiagramFactorId idToUse, Factor cmObject)
	{
		if(cmObject.isStrategy())
			return new DiagramStrategy(idToUse, (Strategy)cmObject);
		else if(cmObject.isCause())
			return new DiagramCause(idToUse, (Cause)cmObject);
		else if(cmObject.isTarget())
			return new DiagramTarget(idToUse, (Target)cmObject);
		else if(cmObject.isFactorCluster())
			return new DiagramFactorCluster(idToUse, (FactorCluster)cmObject);
			
		throw new RuntimeException("Tried to wrap unknown cmObject: " + cmObject);
	}
	
	public static DiagramFactor createFromJson(Project project, EnhancedJsonObject json) throws Exception
	{
		DiagramFactorId id = new DiagramFactorId(json.getId(TAG_ID).asInt());
		FactorId wrappedId = new FactorId(json.getId(TAG_WRAPPED_ID).asInt());
		Factor factor = project.findNode(wrappedId);
		DiagramFactor diagramFactor = wrapConceptualModelObject(id, factor);
		diagramFactor.fillFrom(json);
		return diagramFactor;
	}

	protected DiagramFactor(DiagramFactorId idToUse, Factor factorToWrap)
	{
		underlyingObject = factorToWrap;
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
	
	public boolean isFactor()
	{
		return true;
	}
	
	public Factor getUnderlyingObject()
	{
		return underlyingObject;
	}
	
	public DiagramFactorId getDiagramFactorId()
	{
		return id;
	}

	public int getType()
	{
		return getWrappedType();
	}
	
	public FactorId getWrappedId()
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

	public FactorType getFactorType()
	{
		return underlyingObject.getNodeType();
	}

	public void setNodeType(FactorType typeToUse)
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

	public boolean isCause()
	{
		return underlyingObject.isCause();
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
	
	public boolean isContributingFactor()
	{
		return underlyingObject.isContributingFactor();
	}
	
	public boolean isDirectThreat()
	{
		return underlyingObject.isDirectThreat();
	}
	
	public boolean isStress()
	{
		return underlyingObject.isStress();
	}
	
	public boolean isStrategy()
	{
		return underlyingObject.isStrategy();
	}
	
	public boolean isFactorCluster()
	{
		return underlyingObject.isFactorCluster();
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
			new CommandSetFactorSize(getDiagramFactorId(), getDefaultSize(), getSize()),
			new CommandDiagramMove(-x, -y, new DiagramFactorId[] {getDiagramFactorId()}),
			new CommandSetObjectData(getWrappedType(), getWrappedId(), TAG_VISIBLE_LABEL, EAMBaseObject.DEFAULT_LABEL),
		};
	}
	
	public FactorDataMap createFactorDataMap()
	{
		FactorDataMap dataMap = new FactorDataMap();
		dataMap.putId(TAG_ID, getDiagramFactorId());
		dataMap.putId(TAG_WRAPPED_ID, getWrappedId());
		
		// FIXME: This is a crude hack, to preserve the node type information
		// here so we can re-create the node if it gets pasted. 
		// Really, for each node copied to the clipboard, we should copy 
		// the json for both the ConceptualModelNode and for the DiagramNode.
		// That will also fix the current bug that objectives and goals are not copied
		dataMap.put(TAG_NODE_TYPE, FactorDataMap.convertNodeTypeToInt(getFactorType()));
		
		
		dataMap.putPoint(TAG_LOCATION, getLocation());
		dataMap.putDimension(TAG_SIZE, getSize());
		dataMap.putString(TAG_VISIBLE_LABEL, getLabel());
		
		return dataMap;
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject dataMap = new DataMap();
		dataMap.putId(TAG_ID, getDiagramFactorId());
		dataMap.putId(TAG_WRAPPED_ID, getWrappedId());
		dataMap.putPoint(TAG_LOCATION, getLocation());
		dataMap.putDimension(TAG_SIZE, getSize());
		return dataMap;
	}
	
	public void fillFrom(EnhancedJsonObject json) throws ParseException
	{
		FactorDataMap dataMap = new FactorDataMap(json);
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
	
	DiagramFactorId id;
	Factor underlyingObject;
}

