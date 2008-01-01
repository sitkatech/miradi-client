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

import org.conservationmeasures.eam.diagram.DiagramConstants;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.DiagramModelEvent;
import org.conservationmeasures.eam.diagram.DiagramModelListener;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.GroupBox;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.jgraph.graph.GraphConstants;

public class DiagramGroupBoxCell extends FactorCell implements DiagramModelListener
{
	public DiagramGroupBoxCell(DiagramModel modelToUse, GroupBox groupBox, DiagramFactor diagramFactor)
	{
		super(groupBox, diagramFactor);
		model = modelToUse;
		
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
			Point location = new Point((int)groupBoxBounds.getX() - SIDE_MARGIN, (int)groupBoxBounds.getY()  - shortScopeHeight);
			Dimension size = new Dimension((int)groupBoxBounds.getWidth() + 2*SIDE_MARGIN, (int)groupBoxBounds.getHeight() + shortScopeHeight  + BOTTOM_MARGIN);
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
		return ja.getPreferredSize().height;
	}
	
	public Rectangle2D computeCurrentChildrenBounds()
	{
		Rectangle2D bounds = null;
		ORefList groupBoxChildren = getDiagramFactor().getGroupBoxChildrenRefs();
		for (int i = 0; i < groupBoxChildren.size(); ++i)
		{
			DiagramFactor groupBoxChild = DiagramFactor.find(getProject(), groupBoxChildren.get(i));
			Rectangle2D childBounds = (Rectangle2D) groupBoxChild.getBounds().clone();
			if (bounds == null)
				bounds = childBounds;
			
			bounds.union(bounds, childBounds, bounds);
		}
		
		if(bounds == null)
			return new Rectangle();

		double height = bounds.getHeight();
		double y = bounds.getY();
		Rectangle result = new Rectangle();
		result.setRect(bounds.getX(), y, bounds.getWidth(), height);
		return result;
	}
	
	public void factorAdded(DiagramModelEvent event)
	{
		autoSurroundChildren();
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
	
	private final static int SIDE_MARGIN = 5;
	private final static int BOTTOM_MARGIN = 5;
	
	private DiagramModel model;
	private int shortScopeHeight;
}
