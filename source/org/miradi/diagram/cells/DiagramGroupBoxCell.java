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
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

import javax.swing.JTextPane;

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
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.questions.FontFamiliyQuestion;
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
		return DiagramConstants.GROUP_BOX_COLOR;
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
		
		Rectangle2D groupBoxBounds = computeCurrentChildrenBounds();
		Rectangle newBounds = new Rectangle(0,0,0,0);
		if(!groupBoxBounds.equals(newBounds))
		{
			shortScopeHeight = calculateShortScopeHeight(groupBoxBounds.getBounds().width);
			int gridSize = getProject().getGridSize();
			Point location = new Point((int)groupBoxBounds.getX() - gridSize, (int)groupBoxBounds.getY()  - shortScopeHeight);
			location = getProject().getSnapped(location);
			Dimension size = new Dimension((int)groupBoxBounds.getWidth() + 2*gridSize, (int)groupBoxBounds.getHeight() + shortScopeHeight  + gridSize);
			Dimension newSize = new Dimension(getProject().forceNonZeroEvenSnap(size.width), getProject().forceNonZeroEvenSnap(size.height));
			newBounds = new Rectangle(location, newSize);
			
			GraphConstants.setBounds(getAttributes(), newBounds);
			Hashtable nest = new Hashtable();
			nest.put(this, getAttributes());
			model.edit(nest, null, null, null);
			model.toBackGroupBox(new Object[] {this});

			
			saveLocationAndSize(location, newSize);
		}		
	}
	
	private void saveLocationAndSize(Point location, Dimension size)
	{
		try
		{
			CommandSetObjectData setLocation = new CommandSetObjectData(diagramFactor.getRef(), DiagramFactor.TAG_LOCATION, EnhancedJsonObject.convertFromPoint(location));
			model.getProject().executeInsideListener(setLocation);
			
			CommandSetObjectData setSize = new CommandSetObjectData(diagramFactor.getRef(), DiagramFactor.TAG_SIZE, EnhancedJsonObject.convertFromDimension(size));
			model.getProject().executeInsideListener(setSize);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			//FIXME do something with this exception
		}
	}
	
	/*TODO: should change MultilineCellRenderer and this method to use the same component to display html 
	 * : see AboutBox that uses the HtmlViewer as in  About.class */
	public int calculateShortScopeHeight(int width) 
	{
		JTextPane ja = new JTextPane();
		String fontFamilCode = getProject().getMetadata().getData(ProjectMetadata.TAG_DIAGRAM_FONT_FAMILY);
		String fontFamily = new FontFamiliyQuestion().findChoiceByCode(fontFamilCode).getLabel();
		int size = getProject().getMetadata().getDiagramFontSize();
		if (size==0)
			size = ja.getFont().getSize();
		ja.setFont(new Font(fontFamily, Font.PLAIN, size));
		ja.setSize(width, ja.getMaximumSize().height);
		ja.setText(getText());
		return ja.getPreferredSize().height + getProject().getGridSize();
	}
	
	public Rectangle2D computeCurrentChildrenBounds()
	{
		Rectangle bounds = null;
		ORefList groupBoxChildren = getDiagramFactor().getGroupBoxChildrenRefs();
		for (int childIndex = 0; childIndex < groupBoxChildren.size(); ++childIndex)
		{
			DiagramFactor groupBoxChild = DiagramFactor.find(getProject(), groupBoxChildren.get(childIndex));
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
}
