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
import java.util.Vector;

import javax.swing.JTextPane;

import org.conservationmeasures.eam.diagram.DiagramConstants;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.DiagramModelEvent;
import org.conservationmeasures.eam.diagram.DiagramModelListener;
import org.conservationmeasures.eam.diagram.renderers.MultilineCellRenderer;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.GroupBox;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.jgraph.graph.GraphConstants;

public class DiagramGroupBoxCell extends FactorCell implements CommandExecutedListener, DiagramModelListener
{
	public DiagramGroupBoxCell(DiagramModel modelToUse, GroupBox groupBox, DiagramFactor diagramFactor)
	{
		super(groupBox, diagramFactor);
		model = modelToUse;
		
		getProject().addCommandExecutedListener(this);
		
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
		return metadata.getShortProjectVision();
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
		if (getDiagramFactor().getGroupBoxChildrenRefs().size() == 0)
			return;
			
		Rectangle2D targetBounds = computeCurrentChildrenBounds();
		Rectangle newBounds = new Rectangle(0,0,0,0);
		if(!targetBounds.equals(newBounds))
		{
			shortScopeHeight = calculateShortScopeHeight(targetBounds.getBounds().width);
			Point location = new Point((int)targetBounds.getX() - SIDE_MARGIN, (int)targetBounds.getY()  - shortScopeHeight);
			Dimension size = new Dimension((int)targetBounds.getWidth() + 2*SIDE_MARGIN, (int)targetBounds.getHeight() + shortScopeHeight  + BOTTOM_MARGIN);
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
		Vector<FactorCell> factorCells = model.getAllDiagramFactors();
		for(int i = 0; i < factorCells.size(); ++i)
		{
			FactorCell node = factorCells.get(i);
			if(groupBoxChildren.contains(node.getDiagramFactorRef()))
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
		autoSurroundTargets();
	}

	public void factorDeleted(DiagramModelEvent event)
	{
		autoSurroundTargets();
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
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		if (event.isSetDataCommandWithThisTypeAndTag(DiagramFactor.getObjectType(), DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS))
			autoSurroundTargets();
	}

	private final static int SIDE_MARGIN = 5;
	private final static int BOTTOM_MARGIN = 5;
	public final static int VISION_HEIGHT = 2 * MultilineCellRenderer.ANNOTATIONS_HEIGHT;
	
	private DiagramModel model;
	private int shortScopeHeight;
}
