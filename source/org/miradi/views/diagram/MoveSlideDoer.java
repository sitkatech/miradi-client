/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.SlideShow;
import org.miradi.views.ObjectsDoer;

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
