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

package org.miradi.questions;

import java.util.HashMap;

import org.miradi.dialogs.dashboard.HtmlResourceLongDescriptionProvider;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.views.summary.SummaryView;
import org.miradi.wizard.WizardManager;

public class OpenStandardsConceptualizeQuestion extends DynamicChoiceWithRootChoiceItem
{
	public OpenStandardsConceptualizeQuestion(Project projectToUse, WizardManager wizardManagerToUse)
	{
		project = projectToUse;
		wizardManager = wizardManagerToUse;
	}
	
	@Override
	public ChoiceItemWithChildren createHeaderChoiceItem() throws Exception
	{
		ChoiceItemWithChildren headerChoiceItem = new ChoiceItemWithChildren("1Header", EAM.text("1. Conceptualize"), new HtmlResourceLongDescriptionProvider("dashboard/1.html", getSummaryOverviewStepName()));

		headerChoiceItem.addChild(createTeamMembersChoiceItem());
		headerChoiceItem.addChild(createScopeVisionAndTargetsChoiceItem());
		headerChoiceItem.addChild(createIdentifyCriticalThreatsChoiceItem());
	
		return headerChoiceItem;
	}
	
	protected String getMainDescriptionFileName()
	{
		return MAIN_DESCRIPTION_RIGHT_PANEL_FILE_NAME;
	}

	private ChoiceItem createTeamMembersChoiceItem() throws Exception
	{
		HtmlResourceLongDescriptionProvider provider = new HtmlResourceLongDescriptionProvider(TEAM_RIGHT_PANEL_FILE_NAME, getSummaryOverviewStepName());
		ChoiceItemWithChildren subHeaderChoiceItem = new ChoiceItemWithChildren("1A", EAM.text("1A. Define Initial Project Team"), provider);

		String leftColumnTranslatedText = EAM.text("Team Members:");
		String rightColumnTranslatedText = getDashboardData(Dashboard.PSEUDO_TEAM_MEMBER_COUNT);
		subHeaderChoiceItem.addChild(new ChoiceItemWithChildren("TeamMembers", leftColumnTranslatedText, rightColumnTranslatedText, provider));
		
		return subHeaderChoiceItem;
	}

	private ChoiceItem createScopeVisionAndTargetsChoiceItem() throws Exception
	{
		HtmlResourceLongDescriptionProvider provider = new HtmlResourceLongDescriptionProvider(SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME, getSummaryOverviewStepName());
		ChoiceItemWithChildren subHeaderChoiceItem = new ChoiceItemWithChildren("1B", EAM.text("1B. Define Scope Vision and Targets"), provider);
		
		subHeaderChoiceItem.addChild(createDefineScopeChoiceItem(provider));
		subHeaderChoiceItem.addChild(createTargetChoiceItem(subHeaderChoiceItem, provider));
		subHeaderChoiceItem.addChild(createHumanWelfareTargetChoiceItem(subHeaderChoiceItem, provider));
		subHeaderChoiceItem.addChild(createTargetStatusChoiceItem(subHeaderChoiceItem, provider));
		subHeaderChoiceItem.addChild(createTargetViabilityChoiceItem(subHeaderChoiceItem, provider));
		
		return subHeaderChoiceItem;
	}

	private ChoiceItemWithChildren createTargetViabilityChoiceItem(ChoiceItemWithChildren subHeaderChoiceItem, HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> statusDescriptionTokenReplacementMap2 = new HashMap<String, String>();
		statusDescriptionTokenReplacementMap2.put("%targetWithSimpleViabilityCount", getDashboardData(Dashboard.PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT));
		String leftColumnTranslatedText = EAM.substitute(EAM.text("%targetWithSimpleViabilityCount targets have simple viablity information"), statusDescriptionTokenReplacementMap2);
		
		return new ChoiceItemWithChildren("DescribeStatusOfTargets", leftColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createTargetStatusChoiceItem(ChoiceItemWithChildren subHeaderChoiceItem, HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Describe Status of Targets:");
		HashMap<String, String> statusDescriptionTokenReplacementMap1 = new HashMap<String, String>();
		statusDescriptionTokenReplacementMap1.put("%targetWithKeaCount", getDashboardData(Dashboard.PSEUDO_TARGET_WITH_KEA_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%targetWithKeaCount targets have KEA"), statusDescriptionTokenReplacementMap1);
		
		return new ChoiceItemWithChildren("DescribeStatusOfTargets", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createHumanWelfareTargetChoiceItem(ChoiceItemWithChildren subHeaderChoiceItem, HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Add Human Welfare Targets:");
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%s created"), getDashboardData(Dashboard.PSEUDO_HUMAN_WELFARE_TARGET_COUNT));
		
		return new ChoiceItemWithChildren("AddHumanWelfareTargets", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createTargetChoiceItem(ChoiceItemWithChildren subHeaderChoiceItem, HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%targetCount", getDashboardData(Dashboard.PSEUDO_TARGET_COUNT));
		String leftColumnTranslatedText = EAM.text("Select Conservation Targets:");
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%targetCount created"), tokenReplacementMap);
		
		return new ChoiceItemWithChildren("SelectConservationTargets", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createDefineScopeChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Define Project Scope:");
		String scopeVisionCount = getDashboardData(Dashboard.PSEUDO_TEAM_MEMBER_COUNT);
		String rightColumnTranslatedText = EAM.substitute(EAM.text("Created (%s chars)"), scopeVisionCount);
		
		return new ChoiceItemWithChildren("DefineProjectScope", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}
	
	private ChoiceItemWithChildren createIdentifyCriticalThreatsChoiceItem() throws Exception
	{
		HtmlResourceLongDescriptionProvider provider = new HtmlResourceLongDescriptionProvider(CRITICAL_THREATS_RIGHT_PANEL_FILE_NAME, getSummaryOverviewStepName());
		ChoiceItemWithChildren subHeaderChoiceItem = new ChoiceItemWithChildren("1C", EAM.text("1C. Identify Critical Threats"), provider);

		HashMap<String, String> threatsTokenReplacementMap = new HashMap<String, String>();
		threatsTokenReplacementMap.put("%threatCount", getDashboardData(Dashboard.PSEUDO_THREAT_COUNT));
		threatsTokenReplacementMap.put("%threatWithTaxonomyCount", getDashboardData(Dashboard.PSEUDO_THREAT_WITH_TAXONOMY_COUNT));
		
		subHeaderChoiceItem.addChild(createDirectThreatsChoiceItem(threatsTokenReplacementMap, provider));
		subHeaderChoiceItem.addChild(createTaxonomyCountChoiceItem(threatsTokenReplacementMap, provider));
		subHeaderChoiceItem.addChild(createThreatRankChoiceItem(provider));
		
		return subHeaderChoiceItem;
	}

	protected ChoiceItemWithChildren createTaxonomyCountChoiceItem(HashMap<String, String> threatsTokenReplacementMap,	HtmlResourceLongDescriptionProvider providerToUse)
	{
		String leftColumnTranslatedText = EAM.substitute(EAM.text("%threatWithTaxonomyCount of %threatCount have taxonomy assignments"), threatsTokenReplacementMap);
		
		return new ChoiceItemWithChildren("threatWithTaxonomyCount", leftColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createDirectThreatsChoiceItem(HashMap<String, String> threatsTokenReplacementMap, HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Identify Direct Threats:");
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%threatCount Direct Threats created"), threatsTokenReplacementMap);

		return new ChoiceItemWithChildren("IdentifyDirectThreats", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createThreatRankChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Rank Direct Threats:");
		HashMap<String, String> threatTargetLinksTokenReplacementMap = new HashMap<String, String>();
		threatTargetLinksTokenReplacementMap.put("%threatTargetLinkCount", getDashboardData(Dashboard.PSEUDO_THREAT_TARGET_LINK_COUNT));
		threatTargetLinksTokenReplacementMap.put("%threatTargetWithRatingCount", getDashboardData(Dashboard.PSEUDO_THREAT_TARGET_LINK_WITH_RATING_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%threatTargetWithRatingCount of %threatTargetLinkCount threat/target links ranked"), threatTargetLinksTokenReplacementMap);

		return new ChoiceItemWithChildren("RankDirectThreats", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Conceptualize");
	}
	
	protected String getDashboardData(String tag)
	{
		return getDashboard().getData(tag);
	}
	
	private Dashboard getDashboard()
	{
		ORef dashboardRef = getProject().getSingletonObjectRef(Dashboard.getObjectType());
		return Dashboard.find(getProject(), dashboardRef);
	}
	
	private String getSummaryOverviewStepName()
	{
		return wizardManager.getOverviewStepName(SummaryView.getViewName());
	}
	
	private Project getProject()
	{
		return project;
	}

	private Project project;
	private WizardManager wizardManager;
	public static final String MAIN_DESCRIPTION_RIGHT_PANEL_FILE_NAME = "dashboard/1.html";
	private static final String TEAM_RIGHT_PANEL_FILE_NAME = "dashboard/1A.html";
	private static final String SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME = "dashboard/1B.html";
	private static final String CRITICAL_THREATS_RIGHT_PANEL_FILE_NAME = "dashboard/1C.html";
}
