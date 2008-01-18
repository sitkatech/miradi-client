/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.SlideShow;
import org.conservationmeasures.eam.views.ObjectsDoer;

abstract public class MoveSlideDoer extends ObjectsDoer
{
	abstract public int getDirection();

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			SlideShow object = getDiagramView().getSlideShow();
			ORefList list = new ORefList(object.getData(SlideShow.TAG_SLIDE_REFS));
			ORef slideRef = getObjects()[0].getRef();
			moveInList(list, slideRef);
			getProject().executeCommand(new CommandSetObjectData(object.getRef(), SlideShow.TAG_SLIDE_REFS, list.toString()));
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
	
	public void moveInList(ORefList list, ORef oref)
	{
		int index = list.find(oref);
		list.remove(oref);
		list.add(index + getDirection(),oref);
	}

}
