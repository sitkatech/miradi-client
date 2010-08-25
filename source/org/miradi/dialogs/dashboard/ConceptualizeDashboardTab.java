/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.dashboard;

import java.util.HashMap;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.utils.FillerLabel;

import com.jhlabs.awt.GridLayoutPlus;

public class ConceptualizeDashboardTab extends ObjectDataInputPanel
{
	public ConceptualizeDashboardTab(Project projectToUse)
	{
		super(projectToUse, Dashboard.getObjectType());
		
		setLayout(new GridLayoutPlus(0, 3));		
		addTeamMembersRow();
		addScopeVisionAndTargetsRow();
	}

	private void addTeamMembersRow()
	{
		add(new PanelTitleLabel(EAM.text("1A. Define Initial Project Team")));
		add(new PanelTitleLabel(EAM.text("Team Members:")));
		add(new PanelTitleLabel(getDashboard().getData(Dashboard.PSEUDO_TEAM_MEMBER_COUNT)));
	}
	
	private void addScopeVisionAndTargetsRow()
	{
		add(new PanelTitleLabel(EAM.text("1B. Define Scope Vision and Targets")));
		add(new FillerLabel());
		add(new FillerLabel());
		
		add(new FillerLabel());
		add(new PanelTitleLabel(EAM.text("Define Project Scope:")));
		String scopeVisionCount = getDashboardData(Dashboard.PSEUDO_TEAM_MEMBER_COUNT);
		add(new PanelTitleLabel(EAM.substitute(EAM.text("Created (%s chars)"), scopeVisionCount)));
		
		add(new FillerLabel());
		add(new PanelTitleLabel(EAM.text("Select Conservation Targets:")));
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%targetCount", getDashboardData(Dashboard.PSEUDO_TARGET_COUNT));
		tokenReplacementMap.put("%targetWithAssignedStandardClassificationCount", getDashboardData(Dashboard.PSEUDO_TARGET_WITH_ASSIGNED_STANDARD_CLASSIFICATION_COUNT));
		String targetTaxonomyStats = EAM.substitute(EAM.text("%targetCount created, %targetWithAssignedStandardClassificationCount of %targetCount have current status justifications"), tokenReplacementMap);
		add(new PanelTitleLabel(EAM.text(targetTaxonomyStats)));
	}

	private String getDashboardData(String tag)
	{
		return getDashboard().getData(tag);
	}

	private Dashboard getDashboard()
	{
		ORef dashboardRef = getProject().getSingletonObjectRef(Dashboard.getObjectType());
		return Dashboard.find(getProject(), dashboardRef);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Conceptualize");
	}
}
