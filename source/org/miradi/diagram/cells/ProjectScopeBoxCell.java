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
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

import javax.swing.JTextPane;

import org.jgraph.graph.GraphConstants;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.DiagramModelEvent;
import org.miradi.diagram.DiagramModelListener;
import org.miradi.diagram.renderers.MultilineCellRenderer;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.FontFamiliyQuestion;

public class ProjectScopeBoxCell extends EAMGraphCell implements DiagramModelListener
{
	public ProjectScopeBoxCell(DiagramModel modelToUse)
	{
		model = modelToUse;
		textPane = new JTextPane();
		autoSurroundTargets();
		
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
	
	public String getToolTipString() 
	{
		String toolTip = "<HTML>" + getText() +  
						 "<BR>" + EAM.text("Vision") + ": " + getProjectVision();

		return toolTip;
	}
	
	public Color getColor()
	{
		return EAM.getMainWindow().getColorPreference(AppPreferences.TAG_COLOR_SCOPE_BOX);
	}

	public boolean isProjectScopeBox()
	{
		return true;
	}
	
	public boolean hasVision()
	{
		return getVision().length()>0;
	}
	
	public String getVision()
	{
		String projectVision = getProjectVision();
		if (projectVision.length() == 0)
			return "";
		
		return EAM.text("Vision");
	}

	private String getProjectVision()
	{
		ProjectMetadata metadata = getProject().getMetadata();
		String projectVision = metadata.getProjectVision();
		return projectVision;
	}

	private Project getProject()
	{
		return model.getProject();
	}
	
	public Rectangle2D getBounds()
	{
		return GraphConstants.getBounds(getAttributes());
	}
	
	public int getShortScopeHeight()
	{
		return shortScopeHeight;
	}
	
	public void autoSurroundTargets()
	{
		int gridSize = getProject().getGridSize();
		Rectangle2D targetBounds = computeCurrentTargetBounds();
		Rectangle newBounds = new Rectangle(0,0,0,0);
		if(!targetBounds.equals(newBounds))
		{
			shortScopeHeight = calculateShortScopeHeight(targetBounds.getBounds().width);
			Point location = new Point((int)targetBounds.getX() - 2*gridSize, (int)targetBounds.getY()  - shortScopeHeight);
			location = getProject().getSnapped(location);
			Dimension size = new Dimension((int)targetBounds.getWidth() + 4*gridSize, (int)targetBounds.getHeight() + shortScopeHeight  + 2*gridSize);
			newBounds = new Rectangle(location, size);
		}
		
		GraphConstants.setBounds(getAttributes(), newBounds);
		Hashtable nest = new Hashtable();
		nest.put(this, getAttributes());
		model.edit(nest, null, null, null);
		model.sortLayers();
	}
	
	/*TODO: should change MultilineCellRenderer and this method to use the same component to display html 
	 * : see AboutBox that uses the HtmlViewer as in  About.class */
	public int calculateShortScopeHeight(int width) 
	{
		String fontFamilCode = getProject().getMetadata().getData(ProjectMetadata.TAG_DIAGRAM_FONT_FAMILY);
		String fontFamily = new FontFamiliyQuestion().findChoiceByCode(fontFamilCode).getLabel();
		int size = getProject().getMetadata().getDiagramFontSize();
		if (size==0)
			size = textPane.getFont().getSize();
		textPane.setFont(new Font(fontFamily, Font.PLAIN, size));
		textPane.setSize(width, textPane.getMaximumSize().height);
		textPane.setText(getText());
		return textPane.getPreferredSize().height + HEIGHT_CUSION;
	}
	
	
	public Rectangle2D computeCurrentTargetBounds()
	{
		DiagramObject diagramObject = model.getDiagramObject();
		if(diagramObject == null)
			return new Rectangle();

		Rectangle2D bounds = null;
		
		ORefList allDiagramFactorRefs = diagramObject.getAllDiagramFactorRefs();
		for(int i=0; i < allDiagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(getProject(), allDiagramFactorRefs.get(i));
			if(!Target.is(diagramFactor.getWrappedType()))
				continue;
			
			Rectangle targetBounds = getBoundsOfDiagramFactorOrItsGroupBox(diagramFactor);
			bounds = getSafeUnion(bounds, targetBounds);
		}
		
		if(bounds == null)
			return new Rectangle();

		double height = bounds.getHeight();
		double y = bounds.getY();
		if (hasVision())
		{
			height += VISION_HEIGHT;
			y -= VISION_HEIGHT;
		}
		
		Rectangle result = new Rectangle();
		result.setRect(bounds.getX(), y, bounds.getWidth(), height);
		return result;
	}

	private Rectangle2D getSafeUnion(Rectangle2D bounds, Rectangle targetBounds)
	{
		if(bounds == null)
			return new Rectangle(targetBounds);

		Rectangle unionResult = new Rectangle();
		Rectangle.union(bounds, targetBounds, unionResult);
		return unionResult;
	}

	private Rectangle getBoundsOfDiagramFactorOrItsGroupBox(DiagramFactor diagramFactor)
	{
		ORef groupBoxRef = diagramFactor.getOwningGroupBoxRef();
		if(!groupBoxRef.isInvalid())
			diagramFactor = DiagramFactor.find(getProject(), groupBoxRef);
		
		return diagramFactor.getBounds();
	}

	public void factorAdded(DiagramModelEvent event)
	{
		autoSurroundTargets();
	}

	public void factorChanged(DiagramModelEvent event)
	{
	}

	public void factorDeleted(DiagramModelEvent event)
	{
		autoSurroundTargets();
	}

	public void factorMoved(DiagramModelEvent event)
	{
		autoSurroundTargets();
	}

	public void linkAdded(DiagramModelEvent event)
	{
	}

	public void linkDeleted(DiagramModelEvent event)
	{
	}

	public final static int VISION_HEIGHT = 2 * MultilineCellRenderer.ANNOTATIONS_HEIGHT;
	public final static int HEIGHT_CUSION = 6;
	
	private DiagramModel model;
	private JTextPane textPane;
	private int shortScopeHeight;
}
