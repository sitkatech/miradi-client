/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.diagram.renderers.MultilineCellRenderer;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Target;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

abstract public class FactorCell extends EAMGraphCell
{
	
	protected FactorCell(Factor factorToWrap, DiagramFactor diagramFactorToUse)
	{
		underlyingObject = factorToWrap;
		id = diagramFactorToUse.getDiagramFactorId();
		diagramFactor = diagramFactorToUse;
		
		port = new DefaultPort();
		add(port);
		setColors();
		setFont();
		updateFromDiagramFactor();
	}
	
	public void updateFromDiagramFactor()
	{
		setLocation(diagramFactor.getLocation());
		Dimension size = diagramFactor.getSize();
		setSize(size);
		setPreviousSize(size);
	}
	
	public Rectangle getRectangle()
	{
		return new Rectangle(getLocation(), getSize());
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
	
	public DiagramFactor getDiagramFactor()
	{
		return diagramFactor;
	}

	public ORef getWrappedORef()
	{
		return underlyingObject.getRef();
	}

	public int getUnderlyingFactorType()
	{
		return underlyingObject.getType();
	}
	
	public FactorId getWrappedId()
	{
		return underlyingObject.getFactorId();
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

	public void setNodeType(FactorType typeToUse)
	{
		setColors();
	}
	
	public boolean isStatusDraft()
	{
		return underlyingObject.isStatusDraft();
	}

	public IdList getIndicators()
	{
		return underlyingObject.getDirectOrIndirectIndicators();
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
		if (underlyingObject.canHaveGoal())
			return ((Target)underlyingObject).getGoals();
		return  new IdList();
	}
	
	public IdList getObjectives()
	{
		return underlyingObject.getObjectives();
	}

	abstract public Color getColor();

	public boolean isTextBox()
	{
		return underlyingObject.isTextBox();
	}
	
	public boolean isIntermediateResult()
	{
		return underlyingObject.isIntermediateResult();
	}
	
	public boolean isThreatRedectionResult()
	{
		return underlyingObject.isThreatReductionResult();
	}
	
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

		Dimension newSize = new Dimension(forceEvenSnap(size.width), forceEvenSnap(size.height));
		Rectangle bounds = new Rectangle(location, newSize);
		GraphConstants.setBounds(getAttributes(), bounds);
	}

	private int forceEvenSnap(int value)
	{
		//TODO this null check is here for test code
		if (getUnderlyingObject().getObjectManager()==null)
			return value;
		int gridSize = getUnderlyingObject().getProject().getGridSize()*2;
		if (value%gridSize ==0)
			return value;
		return (value-(value%gridSize) + gridSize);
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
		return getObjectiveRectWithinNode().contains(p);
	}
	
	public boolean isPointInIndicator(Point p)
	{
		return getIndicatorRectWithinNode().contains(p);
	}
	
	public boolean isPointInGoal(Point p)
	{
		if(getGoals().size() == 0)
			return false;
		return getGoalRectWithinNode().contains(p);
	}
	
	public boolean isPointInViability(Point p)
	{
		if (isTarget() && ((Target)getUnderlyingObject()).isViabilityModeTNC())
		{
			return (isPointInIndicator(p) || isPointInGoal(p));
		}
		return false;
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
	
	public Rectangle getGoalRectWithinNode()
	{
		return getAnnotationRectWithinNode();
	}

	public Rectangle getObjectiveRectWithinNode()
	{
		return getAnnotationRectWithinNode();
	}
	
	private Rectangle getAnnotationRectWithinNode()
	{
		Rectangle rect = new Rectangle();
		rect.x = MultilineCellRenderer.INDICATOR_WIDTH;
		rect.y = getSize().height - MultilineCellRenderer.ANNOTATIONS_HEIGHT;
		rect.width = getSize().width -  2*MultilineCellRenderer.INDICATOR_WIDTH;
		rect.height = MultilineCellRenderer.ANNOTATIONS_HEIGHT;
		return rect;
	}
	
	public Rectangle getIndicatorRectWithinNode()
	{
		Rectangle smallTriangle = new Rectangle();
		smallTriangle.x = 0;
		smallTriangle.y = getSize().height - MultilineCellRenderer.INDICATOR_HEIGHT;
		int avoidGoingPastClippingEdge = 1;
		smallTriangle.width = MultilineCellRenderer.INDICATOR_WIDTH - avoidGoingPastClippingEdge;
		smallTriangle.height = MultilineCellRenderer.INDICATOR_HEIGHT - avoidGoingPastClippingEdge;
		return smallTriangle;
	}

	//TODO: this logic should not refere to INDICATOR_WIDTH...but maybe indicator width should be general annoation box width?
	public Rectangle getResultChainRectWithinNode()
	{
		strategyInResultsChain = true;
		Rectangle annotationsRectangle = getIndicatorRectWithinNode();
		annotationsRectangle.x =  getSize().width - MultilineCellRenderer.INDICATOR_WIDTH ;
		annotationsRectangle.y =  getSize().height/2 - MultilineCellRenderer.INDICATOR_HEIGHT/2;
		return annotationsRectangle;
	}
	
	boolean strategyInResultsChain;
	
	DefaultPort port;
	Dimension previousSize;
	Point previousLocation;
	
	DiagramFactorId id;
	Factor underlyingObject;
	DiagramFactor diagramFactor;
}

