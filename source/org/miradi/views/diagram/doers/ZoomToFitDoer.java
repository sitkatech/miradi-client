/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram.doers;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.views.ViewDoer;
import org.miradi.views.diagram.DiagramView;

public class ZoomToFitDoer extends ViewDoer
{
	@Override
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
		
		DiagramView view = (DiagramView)getView();
		DiagramComponent diagram = view.getDiagramComponent();
		
		return diagram.getDiagramModel().getAllFactorCells().size() > 0;
	}
	
	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		DiagramView view = (DiagramView)getView();
		DiagramComponent diagram = view.getDiagramComponent();
		
		Rectangle totalBounds = new Rectangle(0, 0, 0, 0);
		Vector<FactorCell> allCells = diagram.getDiagramModel().getAllFactorCells();
		for (int i = 0 ; i < allCells.size(); ++i)
		{
			Rectangle2D cellBounds = allCells.get(i).getBounds();
			totalBounds.add(cellBounds);
		}
		
		Rectangle originalBounds = diagram.getVisibleRect();
		double verticalRatio = originalBounds.getHeight() / totalBounds.getHeight() ;
		double horizontalRatio =  originalBounds.getWidth() / totalBounds.getWidth();
		
		diagram.setZoomScale(Math.min(verticalRatio, horizontalRatio));
	}
}
