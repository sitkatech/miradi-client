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
package org.miradi.views.diagram.doers;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramComponent;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.DiagramObject;
import org.miradi.views.ViewDoer;

public class ZoomToFitDoer extends ViewDoer
{
	@Override
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
		
		return getDiagramView().getCurrentDiagramComponent().getDiagramModel().getAllFactorCells().size() > 0;
	}

	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		DiagramComponent diagram = getDiagramView().getCurrentDiagramComponent();
		Rectangle2D totalBounds = diagram.getTotalBoundsUsed();		
		Rectangle originalBounds = diagram.getVisibleRect();

		double verticalRatio = originalBounds.getHeight() / totalBounds.getHeight() ;
		double horizontalRatio =  originalBounds.getWidth() / totalBounds.getWidth();
		double scaleRatio = Math.min(verticalRatio, horizontalRatio);

		CommandSetObjectData setZoom = new CommandSetObjectData(diagram.getDiagramObject(), DiagramObject.TAG_ZOOM_SCALE, Double.toString(scaleRatio));
		getProject().executeCommand(setZoom);

		diagram.toScreen(totalBounds);
		diagram.scrollRectToVisible(totalBounds.getBounds());
	}	
}
