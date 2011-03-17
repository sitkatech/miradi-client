/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.objecthelpers;

import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.AbstractStringKeyMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringChoiceMap;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;

public class DashboardEffectiveMapCacheManager implements CommandExecutedListener
{
	public DashboardEffectiveMapCacheManager(Project projectToUse) throws Exception
	{
		project = projectToUse;
	}
	
	public void enable()
	{
		getProject().addCommandExecutedListener(this);
	}
	
	public void disable()
	{
		getProject().removeCommandExecutedListener(this);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			invalidateEffectiveMapCache();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private void invalidateEffectiveMapCache() throws Exception
	{
		effectiveStatusMapCache = null;
	}

	private void rebuildEffectiveMapCache() throws Exception
	{
		Dashboard dashboard = getDashboardSingletonObject();
		effectiveStatusMapCache = dashboard.calculateEffectiveStatusMap();
	}

	private Dashboard getDashboardSingletonObject()
	{
		ORef dashboardRef = getProject().getSingletonObjectRef(Dashboard.getObjectType()); 
		Dashboard dashboard = Dashboard.find(getProject(), dashboardRef);
		return dashboard;
	}

	public AbstractStringKeyMap calculateEffectiveMap() throws Exception
	{
		if (effectiveStatusMapCache == null)
			rebuildEffectiveMapCache();
		
		return effectiveStatusMapCache;
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
	private StringChoiceMap effectiveStatusMapCache;
}
