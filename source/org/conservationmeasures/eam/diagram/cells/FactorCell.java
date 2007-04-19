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
	
	public void markAsRemoved()
	{
		id = null;
		diagramFactor = null;
		underlyingObject = null;
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

//FIXME this class has two methods with identical returns.  
	public int getFactorType()
	{
		return underlyingObject.getType();
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
			return ((Target)underlyingObject).getDirectOrIndirectGoals();
		return  new IdList();
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
	
	public boolean isPointInViability(Point p)
	{
		if (isTarget() && ((Target)getUnderlyingObject()).isViabilityModeTNC())
		{
			return (isPointInIndicator(p) || isPointInGoal(p));
		}
		return false;
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

	DefaultPort port;
	Dimension previousSize;
	Point previousLocation;
	
	DiagramFactorId id;
	Factor underlyingObject;
	DiagramFactor diagramFactor;
}

