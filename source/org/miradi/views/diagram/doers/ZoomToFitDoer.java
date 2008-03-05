/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram.doers;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.miradi.diagram.DiagramComponent;
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
		
		return getDiagramComponent().getDiagramModel().getAllFactorCells().size() > 0;
	}

	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		DiagramComponent diagram = getDiagramComponent();
		Rectangle2D totalBounds = diagram.getTotalBoundsUsed();		
		Rectangle originalBounds = diagram.getVisibleRect();
		
		double verticalRatio = originalBounds.getHeight() / totalBounds.getHeight() ;
		double horizontalRatio =  originalBounds.getWidth() / totalBounds.getWidth();
		
		double scaleRatio = Math.min(verticalRatio, horizontalRatio);
		diagram.setZoomScale(scaleRatio);
		
		diagram.toScreen(totalBounds);
		diagram.scrollRectToVisible(totalBounds.getBounds());
	}
	
	private DiagramComponent getDiagramComponent()
	{
		DiagramView view = (DiagramView)getView();
		return view.getDiagramComponent();
	}	
}
