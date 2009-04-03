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
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ViewData;
import org.miradi.views.ViewDoer;

abstract public class AbstractPlanningViewConfigurationDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if(!super.isAvailable())
			return false;
		
		if(!getProject().isOpen())
			return false;
		
		if (!isPlanningView())
			return false;
		
		if (isInvalidConfigurationChoice())
			return false;
			
		return true;
	}
	
	private boolean isInvalidConfigurationChoice()
	{
		try
		{
			ViewData viewData = getProject().getCurrentViewData();
			ORef planningViewConfigurationRef = ORef.createFromString(viewData.getData(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF));
			return planningViewConfigurationRef.isInvalid();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return true;
		}
	}
}
