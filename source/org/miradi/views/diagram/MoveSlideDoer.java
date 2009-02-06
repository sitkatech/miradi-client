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
