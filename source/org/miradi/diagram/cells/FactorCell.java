/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.diagram.cells;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.Vector;

import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.PortView;
import org.miradi.diagram.factortypes.FactorType;
import org.miradi.diagram.renderers.MultilineCellRenderer;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.BaseObjectByFullNameSorter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Factor;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.utils.HtmlUtilities;
import org.miradi.utils.Utility;

abstract public class FactorCell extends EAMGraphCell
{
	
	protected FactorCell(Factor factorToWrap, DiagramFactor diagramFactorToUse)
	{
		wrappedFactor = factorToWrap;
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
		size = setSize(size);
		setPreviousSize(size);
	}
	
	public String getToolTipString(Point pointRelativeToCellOrigin) 
	{
		Factor factor = getWrappedFactor();

		String tip = "<html>";

		tip += "<TABLE width='400'><TR><TD>";
		
		tip += "<B>" + HtmlUtilities.plainStringWithNewlinesToHtml(factor.getLabel()) + "</B><BR>";
		
		String header = "";
		String detailsTag = "";
		ORefList bullets = new ORefList();
		if(isPointInIndicator(pointRelativeToCellOrigin))
		{
			header = EAM.text("Indicators:");
			detailsTag = Indicator.TAG_DETAIL;
			bullets = new ORefList(Indicator.getObjectType(), factor.getDirectOrIndirectIndicators());
		}
		else if(isPointInGoal(pointRelativeToCellOrigin))
		{
			header = EAM.text("Goals:");
			detailsTag = Goal.TAG_FULL_TEXT;
			bullets = new ORefList(Goal.getObjectType(), factor.getGoals());
		}
		else if(isPointInObjective(pointRelativeToCellOrigin))
		{
			header = EAM.text("Objectives:");
			detailsTag = Objective.TAG_FULL_TEXT;
			bullets = new ORefList(Objective.getObjectType(), factor.getObjectiveIds());
		}
		else if(factor.getDetails().length() > 0)
		{
			tip += "<BR>" + EAM.text("Details:");
			tip += "<BR>" + factor.getDetails();
		}
		
		if(bullets.size() == 0)
			return tip;
		
		tip += "<BR>" + header + "<UL>";
		
		Vector<BaseObject> sortedObjectsAsBullets = getSortedBullets(bullets);
		for(int i = 0; i < sortedObjectsAsBullets.size(); ++i)
		{
			BaseObject baseObject = sortedObjectsAsBullets.get(i);
			tip += getObjectText(baseObject, baseObject.getData(detailsTag));
		}	
		tip += "</UL>";
		
		tip += "</TD></TR></TABLE>";
		return tip;
	}

	private Vector<BaseObject> getSortedBullets(ORefList refsAsbullets)
	{
		Vector<BaseObject> sortedObjects = new Vector<BaseObject>();
		for(int index = 0; index < refsAsbullets.size(); ++index)
		{
			BaseObject baseObject = getProject().findObject(refsAsbullets.get(index));
			sortedObjects.add(baseObject);
		}
		
		Collections.sort(sortedObjects, new BaseObjectByFullNameSorter());
		
		return sortedObjects;
	}

	private String getObjectText(BaseObject object, String details)
	{
		String text = "<LI>" + HtmlUtilities.plainStringWithNewlinesToHtml(object.combineShortLabelAndLabel()) + "";
		
		if (details.length() > 0)
			text += "<BR><I>" +	HtmlUtilities.plainStringWithNewlinesToHtml(details) + "</I>";	
		
		text += "</LI>";
		return text;
	}

	public Rectangle getRectangle()
	{
		return new Rectangle(getLocation(), getSize());
	}
	
	public boolean isFactor()
	{
		return true;
	}
	
	public DiagramFactorId getDiagramFactorId()
	{
		return id;
	}
	
	public ORef getDiagramFactorRef()
	{
		return getDiagramFactor().getRef();
	}
	
	public DiagramFactor getDiagramFactor()
	{
		return diagramFactor;
	}

	public Factor getWrappedFactor()
	{
		return wrappedFactor;
	}
		
	public ORef getWrappedFactorRef()
	{
		return getWrappedFactor().getRef();
	}

	public FactorId getWrappedId()
	{
		return getWrappedFactor().getFactorId();
	}
	
	public int getWrappedType()
	{
		return getWrappedFactor().getType();
	}
	
	public String getLabel()
	{
		return getWrappedFactor().getLabel();
	}
	
	public void setLabel(String name) throws Exception
	{
		getWrappedFactor().setLabel(name);
	}

	public void setNodeType(FactorType typeToUse)
	{
		setColors();
	}
	
	public boolean isStatusDraft()
	{
		return getWrappedFactor().isStatusDraft();
	}

	public IdList getIndicators()
	{
		return getWrappedFactor().getDirectOrIndirectIndicators();
	}

	public boolean canHaveObjectives()
	{
		return getWrappedFactor().canHaveObjectives();
	}

	public boolean canHaveGoal()
	{
		return getWrappedFactor().canHaveGoal();
	}

	public boolean isCause()
	{
		return getWrappedFactor().isCause();
	}

	public Point getPortLocation(GraphLayoutCache cache)
	{
		PortView portView = (PortView) cache.getMapping(getPort(), false);
		return Utility.convertPoint2DToPoint(portView.getLocation());	
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
		return getWrappedFactor().getComment();
	}
	
	public String getDetails()
	{
		return getWrappedFactor().getDetails();
	}

	public IdList getGoals()
	{
		if (getWrappedFactor().canHaveGoal())
			return ((Target)getWrappedFactor()).getGoals();
		return  new IdList(Goal.getObjectType());
	}
	
	public IdList getObjectives()
	{
		return getWrappedFactor().getObjectiveIds();
	}

	abstract public Color getColor();

	public boolean isGroupBox()
	{
		return getWrappedFactor().isGroupBox();
	}
	
	public boolean isTextBox()
	{
		return getWrappedFactor().isTextBox();
	}
	
	public boolean isIntermediateResult()
	{
		return getWrappedFactor().isIntermediateResult();
	}
	
	public boolean isThreatRedectionResult()
	{
		return getWrappedFactor().isThreatReductionResult();
	}
	
	public boolean isTarget()
	{
		return getWrappedFactor().isTarget();
	}
	
	public boolean isContributingFactor()
	{
		return getWrappedFactor().isContributingFactor();
	}
	
	public boolean isDirectThreat()
	{
		return getWrappedFactor().isDirectThreat();
	}
	
	public boolean isStress()
	{
		return getWrappedFactor().isStress();
	}
	
	public boolean isActivity()
	{
		return getWrappedFactor().isActivity();
	}
	
	public boolean isStrategy()
	{
		return getWrappedFactor().isStrategy();
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
	
	public Dimension setSize(Dimension size)
	{
		Point location = getLocation();

		Dimension newSize = new Dimension(getProject().forceNonZeroEvenSnap(size.width), getProject().forceNonZeroEvenSnap(size.height));
		Rectangle bounds = new Rectangle(location, newSize);
		GraphConstants.setBounds(getAttributes(), bounds);
		
		return newSize;
	}

	private Project getProject()
	{
		return getWrappedFactor().getProject();
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
	
	public void setPreviousPortLocation(Point location)
	{
		previousPortLocation = location;
	}
	
	public Point getPreviousPortLocation()
	{
		return previousPortLocation;
	}
	
	public Rectangle2D getBounds()
	{
		return GraphConstants.getBounds(getAttributes());
	}
	
	public boolean hasMoved()
	{
		if(getPreviousLocation() == null)
			return false;
		
		return !getLocation().equals(getPreviousLocation());
	}
	
	public boolean sizeHasChanged()
	{
		if(getPreviousSize() == null)
			return false;
		
		return !getSize().equals(getPreviousSize());
	}
	
	public boolean isPointInObjective(Point pointRelativeToOrigin)
	{
		if(getObjectives().size() == 0)
			return false;
		return getObjectiveRectWithinNode().contains(pointRelativeToOrigin);
	}
	
	public boolean isPointInIndicator(Point pointRelativeToOrigin)
	{
		return getIndicatorRectWithinNode().contains(pointRelativeToOrigin);
	}
	
	public boolean isPointInGoal(Point pointRelativeToOrigin)
	{
		if(getGoals().size() == 0)
			return false;
		return getGoalRectWithinNode().contains(pointRelativeToOrigin);
	}
	
	public boolean isPointInViability(Point pointRelativeToOrigin)
	{
		if (isTarget() && ((Target)getWrappedFactor()).isViabilityModeTNC())
		{
			return (isPointInIndicator(pointRelativeToOrigin) || isPointInGoal(pointRelativeToOrigin));
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
		Rectangle annotationsRectangle = getIndicatorRectWithinNode();
		annotationsRectangle.x =  getSize().width - MultilineCellRenderer.INDICATOR_WIDTH ;
		annotationsRectangle.y =  getSize().height/2 - MultilineCellRenderer.INDICATOR_HEIGHT/2;
		return annotationsRectangle;
	}
	
	private DefaultPort port;
	private Dimension previousSize;
	private Point previousLocation;
	private Point previousPortLocation;
	
	private DiagramFactorId id;
	private Factor wrappedFactor;
	private DiagramFactor diagramFactor;
}

