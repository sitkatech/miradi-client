/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cells;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

import javax.swing.JTextPane;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.DiagramModelEvent;
import org.conservationmeasures.eam.diagram.DiagramModelListener;
import org.conservationmeasures.eam.diagram.renderers.MultilineCellRenderer;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.jgraph.graph.GraphConstants;

public class ProjectScopeBox extends EAMGraphCell implements DiagramModelListener
{
	public ProjectScopeBox(DiagramModel modelToUse)
	{
		model = modelToUse;
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
						 "<BR> Vision: " + getVision();

		return toolTip;
	}
	
	public Color getColor()
	{
		return EAM.getMainWindow().getColorPreference(AppPreferences.TAG_COLOR_SCOPE);
	}


	public boolean isProjectScope()
	{
		return true;
	}
	
	public boolean hasVision()
	{
		return getVision().length()>0;
	}
	
	public String getVision()
	{
		ProjectMetadata metadata = getProject().getMetadata();
		if (metadata.getProjectVision().length() == 0)
			return metadata.getShortProjectVision();
		
		return EAM.text("[Vision]") + metadata.getShortProjectVision();
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
		model.toBack(new Object[] {this});
	}
	
	/*TODO: should change MultilineCellRenderer and this method to use the same component to display html 
	 * : see AboutBox that uses the HtmlViewer as in  About.class */
	public int calculateShortScopeHeight(int width) 
	{
		JTextPane ja = new JTextPane();
		String fontFamily = getProject().getMetadata().getData(ProjectMetadata.PSEUDO_TAG_DIAGRAM_FONT_FAMILY);
		int size = getProject().getMetadata().getDiagramFontSize();
		if (size==0)
			size = ja.getFont().getSize();
		ja.setFont(new Font(fontFamily, Font.PLAIN, size));
		ja.setSize(width, ja.getMaximumSize().height);
		ja.setText(getText());
		return ja.getPreferredSize().height + HEIGHT_CUSION;
	}
	
	
	public Rectangle2D computeCurrentTargetBounds()
	{
		Rectangle2D bounds = null;
		FactorCell[] factorCells = model.getAllDiagramTargets();
		for(int i=0; i < factorCells.length; ++i)
		{
			FactorCell node = factorCells[i];
			if(node.isTarget())
			{
				if(bounds == null)
					bounds = (Rectangle2D)node.getBounds().clone();

				Rectangle tempRect = new Rectangle();
				Rectangle.union(bounds, node.getBounds(), tempRect);
				bounds = tempRect;
			}
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
	
	DiagramModel model;
	int shortScopeHeight;
}
