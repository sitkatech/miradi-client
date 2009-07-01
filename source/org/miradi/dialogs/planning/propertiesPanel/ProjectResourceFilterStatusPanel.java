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

package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.dialogs.planning.upperPanel.WorkPlanTreeTableModel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.StringMap;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;


public class ProjectResourceFilterStatusPanel extends AbstractFixedHeightDirectlyAboveTreeTablePanel
{
	public ProjectResourceFilterStatusPanel(Project projectToUse)
	{
		project = projectToUse;
	}
	
	@Override
	public String getText()
	{
		return getUpperLeftPanelText();
	}
	
	private String getUpperLeftPanelText()
	{
		ORefList projectResourceRefs = getProjectResourceFilterRefs();
		if (projectResourceRefs.size() == 0)
			return "";
		
		return EAM.text("Project Resource Filter On");
	}

	private ORefList getProjectResourceFilterRefs()
	{
		try
		{
			if (hasNotBeenConstructed())
				return new ORefList();
			
			TableSettings tableSettings = TableSettings.findOrCreate(getProject(), WorkPlanTreeTableModel.UNIQUE_TREE_TABLE_IDENTIFIER);
			StringMap tableSettingsMap = tableSettings.getTableSettingsMap();
			String refs = tableSettingsMap.get(TableSettings.WORK_PLAN_PROJECT_RESOURCE_FILTER_CODELIST_KEY);

			return new ORefList(refs);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new ORefList();
		}
	}

	private boolean hasNotBeenConstructed()
	{
		return getProject() ==  null;
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
