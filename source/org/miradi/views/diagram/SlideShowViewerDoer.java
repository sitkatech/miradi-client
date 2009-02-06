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
package org.miradi.views.diagram;

import org.miradi.dialogs.slideshow.SlideShowViewer;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.views.ViewDoer;

public class SlideShowViewerDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
		
		if (viewer==null)
			return true;
		
		if (viewer.isVisible())
			return false;
		
		viewer = null;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			viewer = new SlideShowViewer(getMainWindow());
			viewer.setVisible(true);
		}
		catch(Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	SlideShowViewer viewer;

}