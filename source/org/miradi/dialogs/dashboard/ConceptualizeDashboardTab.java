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

import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.EAM;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.views.summary.SummaryView;

public class ConceptualizeDashboardTab extends AbstractDashboardTab
{
	public ConceptualizeDashboardTab(Project projectToUse) throws Exception
	{
		super(projectToUse);
	}
	
	@Override
	protected String getMainDescriptionFileName()
	{
		return MAIN_DESCRIPTION_RIGHT_PANEL_FILE_NAME;
	}
	
	@Override
	protected TwoColumnPanel createLeftPanel()
	{
		TwoColumnPanel leftMainPanel = new TwoColumnPanel();
		createHeaderRow(leftMainPanel, EAM.text("1. Conceptualize"), "", getMainDescriptionFileName(), SummaryView.getViewName());

		addTeamMembersRow(leftMainPanel);
		addScopeVisionAndTargetsRow(leftMainPanel);
		addIdentifyCriticalThreatsRow(leftMainPanel);
		
		return leftMainPanel;
	}

	private void addTeamMembersRow(TwoColumnPanel leftMainPanel)
	{
		createSubHeaderRow(leftMainPanel, EAM.text("1A. Define Initial Project Team"), TEAM_RIGHT_PANEL_FILE_NAME);
	
		String leftColumnTranslatedText = EAM.text("Team Members:");
		String rightColumnTranslatedText = getDashboardData(Dashboard.PSEUDO_TEAM_MEMBER_COUNT);
		createDataRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, TEAM_RIGHT_PANEL_FILE_NAME);
	}

	private void addScopeVisionAndTargetsRow(TwoColumnPanel leftMainPanel)
	{
		createSubHeaderRow(leftMainPanel, EAM.text("1B. Define Scope Vision and Targets"), SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME);
		addDefineScopeRow(leftMainPanel);
		addTargetRow(leftMainPanel);
		addHumanWelfareTargetRow(leftMainPanel);
		addTargetStatusRow(leftMainPanel);
		addTargetViabilityRow(leftMainPanel);
	}

	private void addTargetViabilityRow(TwoColumnPanel leftMainPanel)
	{
		HashMap<String, String> statusDescriptionTokenReplacementMap2 = new HashMap<String, String>();
		statusDescriptionTokenReplacementMap2.put("%targetWithSimpleViabilityCount", getDashboardData(Dashboard.PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT));
		String leftColumnTranslatedText = EAM.substitute(EAM.text("%targetWithSimpleViabilityCount targets have simple viablity information"), statusDescriptionTokenReplacementMap2);
		
		createDataRow(leftMainPanel, leftColumnTranslatedText, "", SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME);
	}

	private void addTargetStatusRow(TwoColumnPanel leftMainPanel)
	{
		String leftColumnTranslatedText = EAM.text("Describe Status of Targets:");
		HashMap<String, String> statusDescriptionTokenReplacementMap1 = new HashMap<String, String>();
		statusDescriptionTokenReplacementMap1.put("%targetWithKeaCount", getDashboardData(Dashboard.PSEUDO_TARGET_WITH_KEA_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%targetWithKeaCount targets have KEA"), statusDescriptionTokenReplacementMap1);
		
		createDataRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME);
	}

	private void addHumanWelfareTargetRow(TwoColumnPanel leftMainPanel)
	{
		String leftColumnTranslatedText = EAM.text("Add Human Welfare Targets:");
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%s created"), getDashboardData(Dashboard.PSEUDO_HUMAN_WELFARE_TARGET_COUNT));
		
		createDataRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME);
	}

	private void addTargetRow(TwoColumnPanel leftMainPanel)
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%targetCount", getDashboardData(Dashboard.PSEUDO_TARGET_COUNT));
		String leftColumnTranslatedText = EAM.text("Select Conservation Targets:");
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%targetCount created"), tokenReplacementMap);
		
		createDataRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME);
	}

	private void addDefineScopeRow(TwoColumnPanel leftMainPanel)
	{
		String leftColumnTranslatedText = EAM.text("Define Project Scope:");
		String scopeVisionCount = getDashboardData(Dashboard.PSEUDO_TEAM_MEMBER_COUNT);
		String rightColumnTranslatedText = EAM.substitute(EAM.text("Created (%s chars)"), scopeVisionCount);
		
		createDataRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME);
	}
	
	private void addIdentifyCriticalThreatsRow(TwoColumnPanel leftMainPanel)
	{
		createSubHeaderRow(leftMainPanel, EAM.text("1C. Identify Critical Threats"), CRITICAL_THREATS_RIGHT_PANEL_FILE_NAME);
		
		HashMap<String, String> threatsTokenReplacementMap = new HashMap<String, String>();
		threatsTokenReplacementMap.put("%threatCount", getDashboardData(Dashboard.PSEUDO_THREAT_COUNT));
		threatsTokenReplacementMap.put("%threatWithTaxonomyCount", getDashboardData(Dashboard.PSEUDO_THREAT_WITH_TAXONOMY_COUNT));
		
		addDirectThreats(leftMainPanel, threatsTokenReplacementMap);
		String leftColumnTranslatedText = EAM.substitute(EAM.text("%threatWithTaxonomyCount of %threatCount have taxonomy assignments"), threatsTokenReplacementMap);
		createDataRow(leftMainPanel, leftColumnTranslatedText, "", CRITICAL_THREATS_RIGHT_PANEL_FILE_NAME);
		
		addThreatRank(leftMainPanel);
	}

	private void addDirectThreats(TwoColumnPanel leftMainPanel, HashMap<String, String> threatsTokenReplacementMap)
	{
		String leftColumnTranslatedText = EAM.text("Identify Direct Threats:");
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%threatCount Direct Threats created"), threatsTokenReplacementMap);

		createDataRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, CRITICAL_THREATS_RIGHT_PANEL_FILE_NAME);
	}

	private void addThreatRank(TwoColumnPanel leftMainPanel)
	{
		String leftColumnTranslatedText = EAM.text("Rank Direct Threats:");
		HashMap<String, String> threatTargetLinksTokenReplacementMap = new HashMap<String, String>();
		threatTargetLinksTokenReplacementMap.put("%threatTargetLinkCount", getDashboardData(Dashboard.PSEUDO_THREAT_TARGET_LINK_COUNT));
		threatTargetLinksTokenReplacementMap.put("%threatTargetWithRatingCount", getDashboardData(Dashboard.PSEUDO_THREAT_TARGET_LINK_WITH_RATING_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%threatTargetWithRatingCount of %threatTargetLinkCount threat/target links ranked"), threatTargetLinksTokenReplacementMap);

		createDataRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, CRITICAL_THREATS_RIGHT_PANEL_FILE_NAME);
	}
	
	private SelectableRow createDataRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName)
	{
		return createDataRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, descriptionFileName, SummaryView.getViewName());
	}
	
	private void createSubHeaderRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightPanelHtmlFileName)
	{
		createSubHeaderRow(leftMainPanel, leftColumnTranslatedText, rightPanelHtmlFileName, SummaryView.getViewName());
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Conceptualize");
	}

	private static final String MAIN_DESCRIPTION_RIGHT_PANEL_FILE_NAME = "dashboard/1.html";
	private static final String TEAM_RIGHT_PANEL_FILE_NAME = "dashboard/1A.html";
	private static final String SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME = "dashboard/1B.html";
	private static final String CRITICAL_THREATS_RIGHT_PANEL_FILE_NAME = "dashboard/1C.html";
}
