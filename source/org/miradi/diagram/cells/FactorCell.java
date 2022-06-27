/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.diagram.renderers.MultilineCellRenderer;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.BaseObjectByFullNameSorter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ProgressReportShortStatusQuestion;
import org.miradi.questions.ResultReportShortStatusQuestion;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.utils.Utility;
import org.miradi.utils.XmlUtilities2;

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
	
	public String getToolTipString(Point pointRelativeToCellOrigin, boolean showZIndex)
	{
		Factor factor = getWrappedFactor();
		boolean isFactorWithProgressStatus = factor.isStrategy() || factor.isActivity() || factor.isMonitoringActivity();
		boolean isFactorWithResultStatus = factor.isThreatReductionResult() || factor.isIntermediateResult() || factor.isBiophysicalResult();

		String tip = "<html>";

		tip += "<TABLE width='400'><TR><TD>";

		tip += "<B>" + factor.getCleanedLabel() + "</B><BR>";
		
		String header = "";
		String detailsTag = "";
		ORefList bullets = new ORefList();
		String listContents = "";
		String listStyle = "margin-left: 20px;";

		DiagramObject diagramObject = EAM.getMainWindow().getDiagramView().getCurrentDiagramObject();
		boolean progressStatusDisplayed = diagramObject.isProgressStatusDisplayEnabled();
		boolean resultStatusDisplayed = diagramObject.isResultStatusDisplayEnabled();

		boolean noContentToDisplay = true;

		if(isPointInIndicator(pointRelativeToCellOrigin))
		{
			header = EAM.text("Indicators:");
			detailsTag = Indicator.TAG_DETAIL;
			bullets = new ORefList(IndicatorSchema.getObjectType(), factor.getDirectOrIndirectIndicators());
			listContents = buildListContentsFromBullets(bullets, detailsTag);
			noContentToDisplay = bullets.size() == 0;
		}
		else if(isPointInGoal(pointRelativeToCellOrigin))
		{
			header = EAM.text("Goals:");
			detailsTag = Goal.TAG_FULL_TEXT;
			bullets = factor.getGoalRefs();
			listContents = buildListContentsFromBullets(bullets, detailsTag);
			noContentToDisplay = bullets.size() == 0;
		}
		else if(isPointInObjective(pointRelativeToCellOrigin))
		{
			header = EAM.text("Objectives:");
			detailsTag = Objective.TAG_FULL_TEXT;
			bullets = new ORefList(ObjectiveSchema.getObjectType(), factor.getObjectiveIds());
			listContents = buildListContentsFromBullets(bullets, detailsTag);
			noContentToDisplay = bullets.size() == 0;
		}
		else if(isFactorWithProgressStatus && progressStatusDisplayed && isPointInStatus(pointRelativeToCellOrigin))
		{
			listStyle = "margin-left: 10px; list-style-type: none;";
			header = EAM.text("Progress Status:");
			listContents = buildListContentForProgressStatus(factor);
			noContentToDisplay = listContents.isEmpty();
		}
		else if(isFactorWithResultStatus && resultStatusDisplayed && isPointInStatus(pointRelativeToCellOrigin))
		{
			listStyle = "margin-left: 10px; list-style-type: none;";
			header = EAM.text("Result Status:");
			listContents = buildListContentForResultStatus(factor);
			noContentToDisplay = listContents.isEmpty();
		}
		else if(factor.getDetails().length() > 0)
		{
			tip += "<BR>" + EAM.text("Details:");
			tip += "<BR>" + factor.getDetails();
		}

		String zIndexInfo = "";
		if (showZIndex)
			zIndexInfo = "<BR>Z-Index: " + this.getDiagramFactor().getZIndex();

		tip += zIndexInfo;

		if(noContentToDisplay)
			return XmlUtilities2.convertXmlTextToHtmlWithoutSurroundingHtmlTags(tip);
		
		tip += "<BR>" + header + "<UL style='" + listStyle + "'>";
		tip += listContents;
		tip += "</UL>";
		tip += "</TD></TR></TABLE>";

		return XmlUtilities2.convertXmlTextToHtmlWithoutSurroundingHtmlTags(tip);
	}

	private String buildListContentsFromBullets(ORefList bullets, String detailsTag)
	{
		String listContents = "";

		Vector<BaseObject> sortedObjectsAsBullets = getSortedBullets(bullets);
		for(int i = 0; i < sortedObjectsAsBullets.size(); ++i)
		{
			BaseObject baseObject = sortedObjectsAsBullets.get(i);
			listContents += getObjectText(baseObject, baseObject.getData(detailsTag));
		}

		return listContents;
	}

	private String buildListContentForResultStatus(Factor factor)
	{
		return buildListContentForResultProgressStatus(factor, new ResultReportShortStatusQuestion(), BaseObject.TAG_RESULT_REPORT_REFS, BaseObject.PSEUDO_TAG_LATEST_RESULT_REPORT_DATE, BaseObject.PSEUDO_TAG_LATEST_RESULT_REPORT_CODE, BaseObject.PSEUDO_TAG_LATEST_RESULT_REPORT_DETAILS);
	}

	private String buildListContentForProgressStatus(Factor factor)
	{
		return buildListContentForResultProgressStatus(factor, new ProgressReportShortStatusQuestion(), BaseObject.TAG_PROGRESS_REPORT_REFS, BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_DATE, BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE, BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_DETAILS);
	}

	private String buildListContentForResultProgressStatus(Factor factor, ChoiceQuestion statusQuestion, String statusRefListTag, String dateTag, String statusTag, String detailsTag)
	{
		String listContents = "";

		ORefList statusRefList = factor.getSafeRefListData(statusRefListTag);

		if (!statusRefList.isEmpty())
		{
			String reportDate = factor.getData(dateTag);
			String reportStatusCode = factor.getData(statusTag);
			String reportStatus = statusQuestion.findChoiceByCode(reportStatusCode).getLabel();
			String reportDetails = factor.getData(detailsTag);

			listContents += "<LI>" + EAM.text("Status: ") + reportStatus + "</LI>";
			listContents += "<LI>" + EAM.text("Date: ") + reportDate + "</LI>";
			listContents += "<LI>" + EAM.text("Details: ") + reportDetails + "</LI>";
		}

		return listContents;
	}

	private Vector<BaseObject> getSortedBullets(ORefList refsAsBullets)
	{
		Vector<BaseObject> sortedObjects = new Vector<BaseObject>();
		for(int index = 0; index < refsAsBullets.size(); ++index)
		{
			BaseObject baseObject = getProject().findObject(refsAsBullets.get(index));
			sortedObjects.add(baseObject);
		}
		
		Collections.sort(sortedObjects, new BaseObjectByFullNameSorter());
		
		return sortedObjects;
	}

	private String getObjectText(BaseObject object, String details)
	{
		String text = "<LI>" + object.combineShortLabelAndLabel() + "";
		
		if (details.length() > 0)
			text += "<BR><I>" +	details + "</I>";	
		
		text += "</LI>";
		return text;
	}

	public Rectangle getRectangle()
	{
		return new Rectangle(getLocation(), getSize());
	}
	
	@Override
	public boolean isFactor()
	{
		return true;
	}
	
	public DiagramFactorId getDiagramFactorId()
	{
		return id;
	}
	
	@Override
	public ORef getDiagramFactorRef()
	{
		return getDiagramFactor().getRef();
	}
	
	@Override
	public DiagramFactor getDiagramFactor()
	{
		return diagramFactor;
	}

	public Factor getWrappedFactor()
	{
		return wrappedFactor;
	}
		
	@Override
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
	
	public boolean canHaveType(final int type)
	{
		return getWrappedFactor().canHaveType(type);
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
	
	@Override
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

	public ORefList getGoalRefs()
	{
		if (getWrappedFactor().canHaveGoal())
			return ((AbstractTarget)getWrappedFactor()).getGoalRefs();
		return  new ORefList();
	}
	
	public ORefList getObjectiveRefs()
	{
		return getWrappedFactor().getObjectiveRefs();
	}

	@Override
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
	
	public boolean isThreatReductionResult()
	{
		return getWrappedFactor().isThreatReductionResult();
	}
	
	public boolean isTarget()
	{
		return getWrappedFactor().isTarget();
	}
	
	public boolean isHumanWelfareTarget()
	{
		return getWrappedFactor().isHumanWelfareTarget();
	}
	
	public boolean isContributingFactor()
	{
		return getWrappedFactor().isContributingFactor();
	}
	
	public boolean isBiophysicalFactor()
	{
		return getWrappedFactor().isBiophysicalFactor();
	}

	public boolean isBiophysicalResult()
	{
		return getWrappedFactor().isBiophysicalResult();
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

	public boolean isMonitoringActivity()
	{
		return getWrappedFactor().isMonitoringActivity();
	}

	public boolean isStrategy()
	{
		return getWrappedFactor().isStrategy();
	}

	public boolean isAnalyticalQuestion()
	{
		return getWrappedFactor().isAnalyticalQuestion();
	}

	public boolean isAssumption()
	{
		return getWrappedFactor().isAssumption();
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

		Dimension newSize = new Dimension(getProject().calculateSnappedSize(size.width), getProject().calculateSnappedSize(size.height));
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
		if(getObjectiveRefs().size() == 0)
			return false;
		return getObjectiveRectWithinNode().contains(pointRelativeToOrigin);
	}
	
	public boolean isPointInIndicator(Point pointRelativeToOrigin)
	{
		return getIndicatorRectWithinNode().contains(pointRelativeToOrigin);
	}
	
	public boolean isPointInGoal(Point pointRelativeToOrigin)
	{
		if(getGoalRefs().size() == 0)
			return false;
		return getGoalRectWithinNode().contains(pointRelativeToOrigin);
	}
	
	public boolean isPointInViability(Point pointRelativeToOrigin)
	{
		if (isTarget() && ((Target)getWrappedFactor()).isViabilityModeKEA())
		{
			return (isPointInIndicator(pointRelativeToOrigin));
		}
		return false;
	}

	public boolean isPointInStatus(Point pointRelativeToOrigin)
	{
		return getStatusRectWithinNode().contains(pointRelativeToOrigin);
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
		rect.x = MultilineCellRenderer.ANNOTATIONS_WIDTH;
		rect.y = getSize().height - MultilineCellRenderer.ANNOTATIONS_HEIGHT;
		rect.width = getSize().width -  2*MultilineCellRenderer.ANNOTATIONS_WIDTH;
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

	public Rectangle getResultChainRectWithinNode()
	{
		Rectangle annotationsRectangle = getIndicatorRectWithinNode();
		annotationsRectangle.x =  getSize().width - MultilineCellRenderer.ANNOTATIONS_WIDTH ;
		annotationsRectangle.y =  getSize().height/2 - MultilineCellRenderer.ANNOTATIONS_HEIGHT/2;
		return annotationsRectangle;
	}

	public Rectangle getStatusRectWithinNode()
	{
		Rectangle rect = new Rectangle();
		int avoidGoingPastClippingEdge = 1;
		rect.x = getSize().width - MultilineCellRenderer.ANNOTATIONS_WIDTH - avoidGoingPastClippingEdge;
		rect.y = getSize().height - MultilineCellRenderer.ANNOTATIONS_HEIGHT - avoidGoingPastClippingEdge;
		rect.width = MultilineCellRenderer.ANNOTATIONS_WIDTH;
		rect.height = MultilineCellRenderer.ANNOTATIONS_HEIGHT;
		return rect;
	}

	private DefaultPort port;
	private Dimension previousSize;
	private Point previousLocation;
	private Point previousPortLocation;
	
	private DiagramFactorId id;
	private Factor wrappedFactor;
	private DiagramFactor diagramFactor;
}

