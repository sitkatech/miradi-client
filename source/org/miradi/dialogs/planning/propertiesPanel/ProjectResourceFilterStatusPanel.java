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

import java.awt.BorderLayout;
import java.awt.Color;

import org.miradi.dialogfields.AbstractWorkPlanStringMapEditorDoer;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
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
		filterStatusLabel = new PanelTitleLabel();
		updateStatusLabel();
		setLayout(new BorderLayout());
		add(filterStatusLabel);
	}
	
	public void updateStatusLabel()
	{
		ORefList projectResourceRefs = getProjectResourceFilterRefs();
		String statusText = "";
		panelBackgroundColor = super.getBackground();
		if (projectResourceRefs.size() > 0)
		{
			statusText = EAM.text("Project Resource Filter Is On");
			panelBackgroundColor = Color.red;
		}
		
		filterStatusLabel.setText(statusText);
		filterStatusLabel.invalidate();
	}
	
	@Override
	public Color getBackground()
	{
		return panelBackgroundColor;
	}
		
	private ORefList getProjectResourceFilterRefs()
	{
		try
		{
			TableSettings tableSettings = TableSettings.findOrCreate(getProject(), AbstractWorkPlanStringMapEditorDoer.getTabSpecificModelIdentifier());
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
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
	private PanelTitleLabel filterStatusLabel;
	private Color panelBackgroundColor;
}
