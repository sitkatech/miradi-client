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

import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramComponent;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.DiagramObject;
import org.miradi.views.diagram.DiagramView;

//TODO extract out a parent class for zoom in and zoom out, maybe zoomtofit
public class ZoomOut extends AbstractZoomDoer
{
	@Override
	public boolean isAvailable()
	{
		return true;
	}

	@Override
	public void doIt() throws CommandFailedException
	{
		try
		{
			DiagramView view = getDiagramView();
			DiagramComponent currentDiagramComponent = view.getCurrentDiagramComponent();
			double newScale = currentDiagramComponent.getScale() * (1.0/ZoomIn.ZOOM_FACTOR);
			DiagramObject diagramObject = currentDiagramComponent.getDiagramObject();
			CommandSetObjectData setZoom = new CommandSetObjectData(diagramObject, DiagramObject.TAG_ZOOM_SCALE, Double.toString(newScale));
			getProject().executeCommand(setZoom);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
}
