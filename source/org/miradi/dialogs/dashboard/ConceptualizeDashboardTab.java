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
		addIdentifyCriticalThreatsRow();
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
		String targetStats = EAM.substitute(EAM.text("%targetCount created"), tokenReplacementMap);
		add(new PanelTitleLabel(targetStats));
		
		add(new FillerLabel());
		add(new PanelTitleLabel(EAM.text("Add Human Welfare Targets:")));
		String humanWelfareTargetCount = EAM.substitute(EAM.text("%s created"), getDashboardData(Dashboard.PSEUDO_HUMAN_WELFARE_TARGET_COUNT));
		add(new PanelTitleLabel(humanWelfareTargetCount));
		
		add(new FillerLabel());
		add(new PanelTitleLabel(EAM.text("Describe Status of Targets:")));
		HashMap<String, String> statusDescriptionTokenReplacementMap = new HashMap<String, String>();
		statusDescriptionTokenReplacementMap.put("%targetWithKeaCount", getDashboardData(Dashboard.PSEUDO_TARGET_WITH_KEA_COUNT));
		statusDescriptionTokenReplacementMap.put("%targetWithSimpleViabilityCount", getDashboardData(Dashboard.PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT));
		String targetStatusDescription = EAM.substitute(EAM.text("%targetWithKeaCount targets have KEA %targetWithSimpleViabilityCount targets have simple viablity information"), statusDescriptionTokenReplacementMap);
		add(new PanelTitleLabel(targetStatusDescription));
	}
	
	private void addIdentifyCriticalThreatsRow()
	{
		add(new PanelTitleLabel(EAM.text("1C. Identify Critical Threats")));
		add(new FillerLabel());
		add(new FillerLabel());
		
		add(new FillerLabel());
		add(new PanelTitleLabel(EAM.text("Identify Direct Threats:")));
		HashMap<String, String> threatsTokenReplacementMap = new HashMap<String, String>();
		threatsTokenReplacementMap.put("%threatCount", getDashboardData(Dashboard.PSEUDO_THREAT_COUNT));
		threatsTokenReplacementMap.put("%threatWithTaxonomyCount", getDashboardData(Dashboard.PSEUDO_THREAT_WITH_TAXONOMY_COUNT));
		String directThreatDescription = EAM.substitute(EAM.text("%threatCount Direct Threats created, %threatWithTaxonomyCount of %threatCount have taxonomy assignments"), threatsTokenReplacementMap);
		add(new PanelTitleLabel(directThreatDescription));
		
		add(new FillerLabel());
		add(new PanelTitleLabel(EAM.text("Rank Direct Threats:")));
		HashMap<String, String> threatTargetLinksTokenReplacementMap = new HashMap<String, String>();
		threatTargetLinksTokenReplacementMap.put("%threatTargetLinkCount", getDashboardData(Dashboard.PSEUDO_THREAT_TARGET_LINK_COUNT));
		threatTargetLinksTokenReplacementMap.put("%threatTargetWithRatingCount", getDashboardData(Dashboard.PSEUDO_THREAT_TARGET_LINK_WITH_RATING_COUNT));
		String threatTargetLinkDescription = EAM.substitute(EAM.text("%threatTargetWithRatingCount of %threatTargetLinkCount target/threat links ranked  (In Simple Mode/Stress Based Mode)"), threatTargetLinksTokenReplacementMap);
		add(new PanelTitleLabel(threatTargetLinkDescription));
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
