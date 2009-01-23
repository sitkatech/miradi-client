/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.GraphConstants;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramConstants;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.DiagramModelEvent;
import org.miradi.diagram.DiagramModelListener;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.GroupBox;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.DiagramFactorBackgroundQuestion;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.PointList;

public class DiagramGroupBoxCell extends FactorCell implements DiagramModelListener
{
	public DiagramGroupBoxCell(DiagramModel modelToUse, GroupBox groupBox, DiagramFactor diagramFactorToUse)
	{
		super(groupBox, diagramFactorToUse);
		model = modelToUse;
		diagramFactor = diagramFactorToUse;
		
		GraphConstants.setBorderColor(getAttributes(), Color.black);
		GraphConstants.setForeground(getAttributes(), Color.black);
		GraphConstants.setOpaque(getAttributes(), true);

		model.addDiagramModelListener(this);
		
		diagramFactorBackgroundQuestion = getProject().getQuestion(DiagramFactorBackgroundQuestion.class);
	}
	
	public void setText(String text)
	{
		setUserObject(text);
	}

	public String getText()
	{
		return (String)getUserObject();
	}
	
	public Color getColor()
	{
		ChoiceItem choiceItem = diagramFactorBackgroundQuestion.findChoiceByCode(getDiagramFactor().getBackgroundColor());
		if (choiceItem == null)
			return DiagramConstants.GROUP_BOX_COLOR;
		
		return choiceItem.getColor();
	}

	private Project getProject()
	{
		return model.getProject();
	}
	
	public Rectangle2D getBounds()
	{
		return GraphConstants.getBounds(getAttributes());
	}
	
	public void autoSurroundChildren()
	{
		if (getDiagramFactor().getGroupBoxChildrenRefs().size() == 0)
			return;
		
		int gridSize = getProject().getGridSize();
		shortScopeHeight = 2 * gridSize;
		Rectangle2D groupBoxBounds = computeCurrentChildrenBounds();
		Point location = new Point((int)groupBoxBounds.getX() - gridSize, (int)groupBoxBounds.getY()  - shortScopeHeight);
		location = getProject().getSnapped(location);
		int widthWithCushion = (int)groupBoxBounds.getWidth() + 2*gridSize;
		int heightWithCushion = (int)groupBoxBounds.getHeight() + shortScopeHeight  + gridSize;
		
		Dimension size = new Dimension(widthWithCushion, heightWithCushion);
		int forcedEvenSnappedWidth = getProject().forceNonZeroEvenSnap(size.width);
		int forcedEvenSnappedHeight = getProject().forceNonZeroEvenSnap(size.height);
		Dimension newSize = new Dimension(forcedEvenSnappedWidth, forcedEvenSnappedHeight);
		Rectangle newBounds = new Rectangle(location, newSize);
		if(newBounds.equals(getBounds()))
			return;
		
		saveLocationAndSize(location, newSize);
		updateFromDiagramFactor();
		model.toBackGroupBox(new Object[] {this});
	}

	private void saveLocationAndSize(Point location, Dimension size)
	{
		try
		{
			CommandSetObjectData setLocation = new CommandSetObjectData(diagramFactor.getRef(), DiagramFactor.TAG_LOCATION, EnhancedJsonObject.convertFromPoint(location));
			model.getProject().executeAsSideEffect(setLocation);
			
			CommandSetObjectData setSize = new CommandSetObjectData(diagramFactor.getRef(), DiagramFactor.TAG_SIZE, EnhancedJsonObject.convertFromDimension(size));
			model.getProject().executeAsSideEffect(setSize);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			//FIXME do something with this exception
		}
	}
	
	public Rectangle2D computeCurrentChildrenBounds()
	{
		boolean shouldIgnoreDrafts = getProject().isNonChainMode();
		Rectangle bounds = null;
		ORefList groupBoxChildren = getDiagramFactor().getGroupBoxChildrenRefs();
		for (int childIndex = 0; childIndex < groupBoxChildren.size(); ++childIndex)
		{
			DiagramFactor groupBoxChild = DiagramFactor.find(getProject(), groupBoxChildren.get(childIndex));
			if (shouldIgnoreDrafts && groupBoxChild.getWrappedFactor().isStatusDraft())
				continue;
			
			Rectangle childBounds = (Rectangle) groupBoxChild.getBounds().clone();
			if (bounds == null)
				bounds = childBounds;
			
			bounds = bounds.union(childBounds);
			includeBendPointsInBounds(bounds, groupBoxChildren, groupBoxChild.getWrappedORef());
		}
		
		if(bounds == null)
			return new Rectangle();		

		double height = bounds.getHeight();
		double y = bounds.getY();
		Rectangle result = new Rectangle();
		result.setRect(bounds.getX(), y, bounds.getWidth(), height);
		return result;
	}

	private void includeBendPointsInBounds(Rectangle bounds, ORefList groupBoxChildren, ORef fromRef)
	{
		for (int childIndex = 0; childIndex < groupBoxChildren.size(); ++childIndex)
		{	
			DiagramFactor groupBoxChild = DiagramFactor.find(getProject(), groupBoxChildren.get(childIndex));
			ORef toRef = groupBoxChild.getWrappedORef();
			if (model.areLinked(fromRef, toRef))
			{
				DiagramLink diagramLink = model.getDiagramLink(fromRef, toRef);
				PointList bendPoints = diagramLink.getBendPoints();
				for (int bendPointIndex = 0; bendPointIndex < bendPoints.size(); ++bendPointIndex)
				{
					bounds.add(bendPoints.get(bendPointIndex));
				}
			}
		}
	}
	
	public void factorAdded(DiagramModelEvent event)
	{
		model.toBackGroupBox(new Object[] {this});
	}

	public void factorChanged(DiagramModelEvent event)
	{
	}

	public void factorDeleted(DiagramModelEvent event)
	{
	}

	public void factorMoved(DiagramModelEvent event)
	{
	}

	public void linkAdded(DiagramModelEvent event)
	{
	}

	public void linkDeleted(DiagramModelEvent event)
	{
	}
	
	private DiagramFactor diagramFactor;
	private DiagramModel model;
	private int shortScopeHeight;
	private ChoiceQuestion diagramFactorBackgroundQuestion;
}
