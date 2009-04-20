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
package org.miradi.views.umbrella.doers;

import java.util.Vector;

import org.miradi.dialogs.base.ObjectPoolTablePanel;
import org.miradi.dialogs.task.ShareableMethodPoolTablePanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Indicator;
import org.miradi.objects.Task;
import org.miradi.views.planning.doers.AbstractShareDoer;

public class TreeNodeShareMethodDoer extends AbstractShareDoer
{
	@Override
	protected boolean hasSharables()
	{
		ORef indicatorRef = getParentRefOfShareableObjects();
		if (!Indicator.is(indicatorRef))
			return false;
	
		Indicator indicator = Indicator.find(getProject(), indicatorRef);
		Vector<Task> methods = indicator.getMethods();
		return hasSharebles(methods, Task.METHOD_NAME);
	}

	@Override
	protected String getParentTaskIdsTag()
	{
		return Indicator.TAG_TASK_IDS;
	}

	@Override
	protected int getParentType()
	{
		return Indicator.getObjectType();
	}
	
	@Override
	protected String getShareDialogTitle()
	{
		return EAM.text("Share Method");
	}

	@Override
	protected ObjectPoolTablePanel createShareableObjectPoolTablePanel(ORef parentOfSharedObjectRefs)
	{
		return new ShareableMethodPoolTablePanel(getMainWindow(), parentOfSharedObjectRefs);
	}
}
