/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.planning.doers;

import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Task;
import org.miradi.objects.ViewData;
import org.miradi.utils.CodeList;
import org.miradi.views.planning.PlanningView;
import org.miradi.views.planning.RowManager;


abstract public class AbstractTreeNodeCreateTaskDoer extends AbstractTreeNodeDoer
{
	public boolean isAvailable()
	{
		try
		{
			BaseObject selected = getSingleSelectedObject();
			if(selected == null)
				return false;
			if(!canOwnTask(selected))
				return false;
			if(!childTaskWouldBeVisible(selected.getType()))
				return false;
			
			return true;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}
	
	protected boolean canOwnTask(BaseObject object)
	{
		if(object.getType() == Task.getObjectType())
			return true;
		
		return false;
	}

	private boolean childTaskWouldBeVisible(int parentType) throws Exception
	{
		ViewData viewData = getProject().getViewData(PlanningView.getViewName());
		CodeList visibleRowCodes = RowManager.getVisibleRowCodes(viewData);
		return (visibleRowCodes.contains(Task.getChildTaskTypeCode(parentType)));
	}
}
