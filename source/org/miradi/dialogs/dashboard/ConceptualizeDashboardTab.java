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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.utils.FillerLabel;
import org.miradi.utils.FlexibleWidthHtmlViewer;
import org.miradi.utils.Translation;
import org.miradi.wizard.MiradiHtmlViewer;

import com.jhlabs.awt.GridLayoutPlus;

public class ConceptualizeDashboardTab extends ObjectDataInputPanel
{
	public ConceptualizeDashboardTab(Project projectToUse) throws Exception
	{
		super(projectToUse, Dashboard.getObjectType());
		
		setLayout(new GridLayoutPlus(0, 2, 3, 3));
		
		add(createFieldsPanel());
		add(createDescriptionPanel(), BorderLayout.AFTER_LINE_ENDS);
	}

	private JPanel createFieldsPanel()
	{
		JPanel dataPanel = new JPanel(new GridLayoutPlus(0, 1, 3, 3));
		dataPanel.add(createTeamMembersRow());
		dataPanel.add(createScopeVisionAndTargetsRow());
		dataPanel.add(createIdentifyCriticalThreatsRow());
		
		return dataPanel;
	}

	private MiradiHtmlViewer createDescriptionPanel() throws Exception
	{
		MiradiHtmlViewer descriptionPanel = new FlexibleWidthHtmlViewer(getMainWindow(), getMainWindow().getHyperlinkHandler());
		String htmlText = Translation.getHtmlContent("DashboardDescription.html");
		descriptionPanel.setText(htmlText);
		
		return descriptionPanel;
	}

	private TwoColumnPanel createTeamMembersRow()
	{
		TwoColumnPanel rows = new TwoColumnPanel();
		rows.add(new PanelTitleLabel(EAM.text("1A. Define Initial Project Team")), BorderLayout.BEFORE_FIRST_LINE);
		rows.add(new FillerLabel());
		
		rows.add(new FillerLabel());
		rows.add(createRow(EAM.text("Team Members:"), getDashboard().getData(Dashboard.PSEUDO_TEAM_MEMBER_COUNT)));
		
		return rows;
	}
	
	private TwoColumnPanel createScopeVisionAndTargetsRow()
	{
		TwoColumnPanel rows = new TwoColumnPanel();
		rows.add(new PanelTitleLabel(EAM.text("1B. Define Scope Vision and Targets")));
		rows.add(new FillerLabel());
		
		
		String scopeVisionCount = getDashboardData(Dashboard.PSEUDO_TEAM_MEMBER_COUNT);
		rows.add(new FillerLabel());
		rows.add(createRow(EAM.text("Define Project Scope:"), EAM.substitute(EAM.text("Created (%s chars)"), scopeVisionCount)));
		
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%targetCount", getDashboardData(Dashboard.PSEUDO_TARGET_COUNT));
		String targetStats = EAM.substitute(EAM.text("%targetCount created"), tokenReplacementMap);
		rows.add(new FillerLabel());
		rows.add(createRow(EAM.text("Select Conservation Targets:"), targetStats));
		
		String humanWelfareTargetCount = EAM.substitute(EAM.text("%s created"), getDashboardData(Dashboard.PSEUDO_HUMAN_WELFARE_TARGET_COUNT));
		rows.add(new FillerLabel());
		rows.add(createRow(EAM.text("Add Human Welfare Targets:"), humanWelfareTargetCount));
		
		String text = EAM.text("Describe Status of Targets:");
		HashMap<String, String> statusDescriptionTokenReplacementMap1 = new HashMap<String, String>();
		statusDescriptionTokenReplacementMap1.put("%targetWithKeaCount", getDashboardData(Dashboard.PSEUDO_TARGET_WITH_KEA_COUNT));
		String targetStatusDescription = EAM.substitute(EAM.text("%targetWithKeaCount targets have KEA"), statusDescriptionTokenReplacementMap1);
		rows.add(new FillerLabel());
		rows.add(createRow(text, targetStatusDescription));
		
		HashMap<String, String> statusDescriptionTokenReplacementMap2 = new HashMap<String, String>();
		statusDescriptionTokenReplacementMap2.put("%targetWithSimpleViabilityCount", getDashboardData(Dashboard.PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT));
		rows.add(new FillerLabel());
		rows.add(createRow("", EAM.substitute(EAM.text("%targetWithSimpleViabilityCount targets have simple viablity information"), statusDescriptionTokenReplacementMap2)));
		
		return rows;
	}
	
	private TwoColumnPanel createIdentifyCriticalThreatsRow()
	{
		TwoColumnPanel rows = new TwoColumnPanel();
		rows.add(new PanelTitleLabel(EAM.text("1C. Identify Critical Threats")));
		rows.add(new FillerLabel());
		
		HashMap<String, String> threatsTokenReplacementMap = new HashMap<String, String>();
		threatsTokenReplacementMap.put("%threatCount", getDashboardData(Dashboard.PSEUDO_THREAT_COUNT));
		threatsTokenReplacementMap.put("%threatWithTaxonomyCount", getDashboardData(Dashboard.PSEUDO_THREAT_WITH_TAXONOMY_COUNT));
		rows.add(new FillerLabel());
		rows.add(createRow(EAM.text("Identify Direct Threats:"), EAM.substitute(EAM.text("%threatCount Direct Threats created"), threatsTokenReplacementMap)));
		
		rows.add(new FillerLabel());
		rows.add(createRow("", EAM.substitute(EAM.text("%threatWithTaxonomyCount of %threatCount have taxonomy assignments"), threatsTokenReplacementMap)));
		
		String text = EAM.text("Rank Direct Threats:");
		HashMap<String, String> threatTargetLinksTokenReplacementMap = new HashMap<String, String>();
		threatTargetLinksTokenReplacementMap.put("%threatTargetLinkCount", getDashboardData(Dashboard.PSEUDO_THREAT_TARGET_LINK_COUNT));
		threatTargetLinksTokenReplacementMap.put("%threatTargetWithRatingCount", getDashboardData(Dashboard.PSEUDO_THREAT_TARGET_LINK_WITH_RATING_COUNT));
		String threatTargetLinkDescription = EAM.substitute(EAM.text("%threatTargetWithRatingCount of %threatTargetLinkCount threat/target links ranked"), threatTargetLinksTokenReplacementMap);
		rows.add(new FillerLabel());
		rows.add(createRow(text, threatTargetLinkDescription));
		
		return rows;
	}
	
	private TwoColumnPanel createRow(String leftText, String rightText)
	{
		HoverOverPanel row = new HoverOverPanel();
		row.add(new PanelTitleLabel(leftText), BorderLayout.BEFORE_FIRST_LINE);
		row.add(new PanelTitleLabel(rightText), BorderLayout.CENTER);
		
		return row;
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
	
	private class HoverOverPanel extends TwoColumnPanel implements MouseListener
	{
		public HoverOverPanel()
		{
			setBackground(AppPreferences.getDataPanelBackgroundColor());
			addMouseListener(this);
		}

		public void mouseClicked(MouseEvent e)
		{
		}

		public void mouseEntered(MouseEvent e)
		{
			setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.blue));
		}

		public void mouseExited(MouseEvent e)
		{
			setBorder(BorderFactory.createEmptyBorder());
		}

		public void mousePressed(MouseEvent e)
		{
		}

		public void mouseReleased(MouseEvent e)
		{
		}
	}
}
