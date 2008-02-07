/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.ViewDoer;
import org.conservationmeasures.eam.views.diagram.DiagramView;

public class ZoomToFitDoer extends ViewDoer
{
	@Override
	public boolean isAvailable()
	{
		return true;
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
