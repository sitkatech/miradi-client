/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Slide;
import org.conservationmeasures.eam.objects.SlideShow;
import org.conservationmeasures.eam.views.ObjectsDoer;

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