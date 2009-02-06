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

import java.util.Vector;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Slide;
import org.miradi.objects.SlideShow;
import org.miradi.views.ObjectsDoer;

public class DeleteSlideDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
		
		return (getObjects().length == 1);
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		Slide slide = (Slide)getObjects()[0];

		
		Vector dialogText = new Vector();
		
		dialogText.add("\nAre you sure you want to delete this slide?");

		String[] buttons = {"Yes", "No", };
		if(!EAM.confirmDialog("Delete Slide", (String[])dialogText.toArray(new String[0]), buttons))
			return;

		getProject().executeCommand(new CommandBeginTransaction());
		
		try
		{
			BaseObject slideShow = getDiagramView().getSlideShow();
			getProject().executeCommand(CommandSetObjectData.createRemoveORefCommand(slideShow, SlideShow.TAG_SLIDE_REFS, slide.getRef()));
			getProject().executeCommandsWithoutTransaction(slide.createCommandsToClear());
			getProject().executeCommand(new CommandDeleteObject(slide.getRef()));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}
}