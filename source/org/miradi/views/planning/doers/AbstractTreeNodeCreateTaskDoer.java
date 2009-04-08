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
package org.miradi.views.planning.doers;

import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Task;


abstract public class AbstractTreeNodeCreateTaskDoer extends AbstractTreeNodeDoer
{
	public boolean isAvailable()
	{
		if(!super.isAvailable())
			return false;
		
		try
		{
			BaseObject selectedObject = getSingleSelectedObject();
			if(selectedObject == null)
				return false;
			if(!canOwnTask(selectedObject))
				return false;
			if(!childWouldBeVisible(Task.getChildTaskTypeCode(selectedObject.getType())))
				return false;
			
			return true;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}
	
	protected boolean canOwnTask(BaseObject selectedObject)
	{
		if(selectedObject.getType() == Task.getObjectType())
			return true;
		
		return false;
	}
}
