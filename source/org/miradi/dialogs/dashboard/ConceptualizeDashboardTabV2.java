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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnGridLayout;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.utils.FillerLabel;

public class ConceptualizeDashboardTabV2 extends ObjectDataInputPanel
{
	public ConceptualizeDashboardTabV2(Project projectToUse) throws Exception
	{
		super(projectToUse, Dashboard.getObjectType());
		
		setLayout(new TwoColumnGridLayout());
		clickableComponentToContentsFileNameMap = new HashMap<SelectableRow, String>();
		
		addLeftPanel(createLeftPanel());
		
		createRightPanel();
		addRightPanel();		
	}
	
	private void addRightPanel() throws Exception
	{
		add(rightSideDescriptionPanel);
	}

	protected void createRightPanel() throws Exception
	{
		rightSideDescriptionPanel = new DashboardRightSideDescriptionPanel(getMainWindow());
		rightSideDescriptionPanel.setRightSidePanelContent("dashboard/1.html");
	}

	private void addLeftPanel(TwoColumnPanel leftMainPanel)
	{
		add(leftMainPanel);
	}

	protected TwoColumnPanel createLeftPanel()
	{
		TwoColumnPanel leftMainPanel = new TwoColumnPanel();
		TwoColumnPanel header = new TwoColumnPanel();
		header.setBorder(BorderFactory.createEtchedBorder());
		header.add(new PanelTitleLabel(EAM.text("1. Conceptualize")));
		header.add(new FillerLabel());
		leftMainPanel.add(header);
		leftMainPanel.add(new FillerLabel());

		addTeamMembersRow(leftMainPanel);
		addScopeVisionAndTargetsRow(leftMainPanel);
		addIdentifyCriticalThreatsRow(leftMainPanel);
		
		return leftMainPanel;
	}

	private void addTeamMembersRow(TwoColumnPanel leftMainPanel)
	{
		addSubHeaderRow(leftMainPanel, EAM.text("1A. Define Initial Project Team"), TEAM_RIGHT_PANEL_FILE_NAME);
	
		Box box = createBorderedBox(TEAM_RIGHT_PANEL_FILE_NAME);
		box.add(new PanelTitleLabel(EAM.text("Team Members:")), BorderLayout.BEFORE_FIRST_LINE);
		box.add(new PanelTitleLabel(getDashboardData(Dashboard.PSEUDO_TEAM_MEMBER_COUNT)));
		leftMainPanel.add(new FillerLabel());
		leftMainPanel.add(box);
	}

	private void addScopeVisionAndTargetsRow(TwoColumnPanel leftMainPanel)
	{
		addSubHeaderRow(leftMainPanel, EAM.text("1B. Define Scope Vision and Targets"), SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME);
		addDefineScopeRow(leftMainPanel);
		addTargetRow(leftMainPanel);
		addHumanWelfareTargetRow(leftMainPanel);
		addTargetStatusRow(leftMainPanel);
		addTargetViabilityRow(leftMainPanel);
	}

	private void addTargetViabilityRow(TwoColumnPanel leftMainPanel)
	{
		Box firstColumnBox = createBoxWithIndent(SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME);
		HashMap<String, String> statusDescriptionTokenReplacementMap2 = new HashMap<String, String>();
		statusDescriptionTokenReplacementMap2.put("%targetWithSimpleViabilityCount", getDashboardData(Dashboard.PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT));
		firstColumnBox.add(new PanelTitleLabel(EAM.substitute(EAM.text("%targetWithSimpleViabilityCount targets have simple viablity information"), statusDescriptionTokenReplacementMap2)));
		leftMainPanel.add(firstColumnBox);
		leftMainPanel.add(new FillerLabel());
	}

	private void addTargetStatusRow(TwoColumnPanel leftMainPanel)
	{
		Box firstColumnBox = createBoxWithIndent(SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME);
		String text = EAM.text("Describe Status of Targets:");
		HashMap<String, String> statusDescriptionTokenReplacementMap1 = new HashMap<String, String>();
		statusDescriptionTokenReplacementMap1.put("%targetWithKeaCount", getDashboardData(Dashboard.PSEUDO_TARGET_WITH_KEA_COUNT));
		firstColumnBox.add(new PanelTitleLabel(text));
		
		Box secondColumnBox = createBorderedBox(SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME);
		String targetStatusDescription = EAM.substitute(EAM.text("%targetWithKeaCount targets have KEA"), statusDescriptionTokenReplacementMap1);
		secondColumnBox.add(new PanelTitleLabel(targetStatusDescription));
		
		leftMainPanel.add(firstColumnBox);
		leftMainPanel.add(secondColumnBox);
	}

	private void addHumanWelfareTargetRow(TwoColumnPanel leftMainPanel)
	{
		Box firstColumnBox = createBoxWithIndent(SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME);
		firstColumnBox.add(new PanelTitleLabel(EAM.text("Add Human Welfare Targets:")));
		
		Box secondColumnBox = createBorderedBox(SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME);
		String humanWelfareTargetCount = EAM.substitute(EAM.text("%s created"), getDashboardData(Dashboard.PSEUDO_HUMAN_WELFARE_TARGET_COUNT));
		secondColumnBox.add(new PanelTitleLabel(humanWelfareTargetCount));
		
		leftMainPanel.add(firstColumnBox);
		leftMainPanel.add(new FillerLabel());
	}

	private void addTargetRow(TwoColumnPanel leftMainPanel)
	{
		Box firstColumnBox = createBoxWithIndent(SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME);
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%targetCount", getDashboardData(Dashboard.PSEUDO_TARGET_COUNT));
		firstColumnBox.add(new PanelTitleLabel(EAM.text("Select Conservation Targets:")));
		
		Box secondColumnBox = createBorderedBox(SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME);
		secondColumnBox.add(new PanelTitleLabel(EAM.substitute(EAM.text("%targetCount created"), tokenReplacementMap)));
		
		leftMainPanel.add(firstColumnBox);
		leftMainPanel.add(secondColumnBox);
	}

	private void addDefineScopeRow(TwoColumnPanel leftMainPanel)
	{
		Box firstColumnBox = createBoxWithIndent(SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME);
		firstColumnBox.add(new PanelTitleLabel(EAM.text("Define Project Scope:")), BorderLayout.BEFORE_FIRST_LINE);
		String scopeVisionCount = getDashboardData(Dashboard.PSEUDO_TEAM_MEMBER_COUNT);
		Box secondColumnBox = createBorderedBox(SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME);
		secondColumnBox.add(new PanelTitleLabel(EAM.substitute(EAM.text("Created (%s chars)"), scopeVisionCount)));
		leftMainPanel.add(firstColumnBox);
		leftMainPanel.add(secondColumnBox);
	}
	
	private void addIdentifyCriticalThreatsRow(TwoColumnPanel leftMainPanel)
	{
		addSubHeaderRow(leftMainPanel, EAM.text("1C. Identify Critical Threats"), CRITICAL_THREATS_RIGHT_PANEL_FILE_NAME);
		
		HashMap<String, String> threatsTokenReplacementMap = new HashMap<String, String>();
		threatsTokenReplacementMap.put("%threatCount", getDashboardData(Dashboard.PSEUDO_THREAT_COUNT));
		threatsTokenReplacementMap.put("%threatWithTaxonomyCount", getDashboardData(Dashboard.PSEUDO_THREAT_WITH_TAXONOMY_COUNT));
		
		addDirectThreats(leftMainPanel, threatsTokenReplacementMap);
		
		Box box = createBoxWithIndent(CRITICAL_THREATS_RIGHT_PANEL_FILE_NAME);
		box.add(new PanelTitleLabel(EAM.substitute(EAM.text("%threatWithTaxonomyCount of %threatCount have taxonomy assignments"), threatsTokenReplacementMap)));
		leftMainPanel.add(box);
		leftMainPanel.add(new FillerLabel());
		
		addThreatRank(leftMainPanel);
	}

	private void addDirectThreats(TwoColumnPanel leftMainPanel, HashMap<String, String> threatsTokenReplacementMap)
	{
		Box leftColumnBox = createBoxWithIndent(CRITICAL_THREATS_RIGHT_PANEL_FILE_NAME);
		leftColumnBox.add(new PanelTitleLabel(EAM.text("Identify Direct Threats:")));
		leftMainPanel.add(leftColumnBox);
		Box rightColumnBox = createBorderedBox(CRITICAL_THREATS_RIGHT_PANEL_FILE_NAME);
		rightColumnBox.add(new PanelTitleLabel(EAM.substitute(EAM.text("%threatCount Direct Threats created"), threatsTokenReplacementMap)));
		leftMainPanel.add(rightColumnBox);
	}

	private void addThreatRank(TwoColumnPanel leftMainPanel)
	{
		Box firstColumnBox = createBoxWithIndent(CRITICAL_THREATS_RIGHT_PANEL_FILE_NAME);
		String text = EAM.text("Rank Direct Threats:");
		firstColumnBox.add(new PanelTitleLabel(text));
		HashMap<String, String> threatTargetLinksTokenReplacementMap = new HashMap<String, String>();
		threatTargetLinksTokenReplacementMap.put("%threatTargetLinkCount", getDashboardData(Dashboard.PSEUDO_THREAT_TARGET_LINK_COUNT));
		threatTargetLinksTokenReplacementMap.put("%threatTargetWithRatingCount", getDashboardData(Dashboard.PSEUDO_THREAT_TARGET_LINK_WITH_RATING_COUNT));
		String threatTargetLinkDescription = EAM.substitute(EAM.text("%threatTargetWithRatingCount of %threatTargetLinkCount threat/target links ranked"), threatTargetLinksTokenReplacementMap);

		Box secondColumnBox = createBorderedBox(CRITICAL_THREATS_RIGHT_PANEL_FILE_NAME);
		secondColumnBox.add(new PanelTitleLabel(threatTargetLinkDescription));
		leftMainPanel.add(firstColumnBox);
		leftMainPanel.add(secondColumnBox);
	}
	
	private Box createBorderedBox(String rightPanelHtmlFileName)
	{
		Box box = Box.createHorizontalBox();
		box.setBorder(BorderFactory.createEtchedBorder());
		SelectableRow selectableComponentToUse = new SelectableRow(box, new FillerLabel());
		box.addMouseListener(new ClickHandler(selectableComponentToUse));
		clickableComponentToContentsFileNameMap.put(selectableComponentToUse, rightPanelHtmlFileName);
		
		return box;
	}

	private void addSubHeaderRow(TwoColumnPanel leftMainPanel, String text, String rightPanelHtmlFileName)
	{
		Box box = createBorderedBox(rightPanelHtmlFileName);
		setSubheaderBackground(box);
		box.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		PanelTitleLabel label = new PanelTitleLabel(text);
		box.add(label, BorderLayout.BEFORE_FIRST_LINE);
		leftMainPanel.add(box);
		leftMainPanel.add(new FillerLabel());
	}
	
	private void setSubheaderBackground(JComponent component)
	{
		component.setOpaque(true);
		component.setBackground(Color.GREEN.darker());
	}

	private Box createBoxWithIndent(String rightPanelHtmlFileName)
	{
		Box box = createBorderedBox(rightPanelHtmlFileName);
		box.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		return box;
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
		return "Conceptualize";
	}
	
	private class ClickHandler extends MouseAdapter
	{
		public ClickHandler(SelectableRow clickableComponentToUse)
		{
			selectableComponent = clickableComponentToUse;
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			super.mouseClicked(e);
			
			try
			{
				clearSelection();
				selectableComponent.selectRow();
				
				String resourceFileName = clickableComponentToContentsFileNameMap.get(selectableComponent);
				rightSideDescriptionPanel.setRightSidePanelContent(resourceFileName);
			}
			catch (Exception exception)
			{
				EAM.logException(exception);
				EAM.unexpectedErrorDialog(exception);
			}
		}
	
		private void clearSelection() throws Exception
		{
			Set<SelectableRow> selectableRows = clickableComponentToContentsFileNameMap.keySet();
			for(SelectableRow selectableRow : selectableRows)
			{
				selectableRow.clearSelection();
			}
		}
		
		private SelectableRow selectableComponent;
	}
	
	private class SelectableRow
	{
		protected SelectableRow(JComponent leftSideToUse, JComponent rightSideToUse)
		{
			leftSide = leftSideToUse;
			rightSide = rightSideToUse;
		}
		
		protected void selectRow()
		{
			leftSide.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
			rightSide.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
		}
		
		protected void clearSelection()
		{
			leftSide.setBorder(BorderFactory.createEtchedBorder());
			rightSide.setBorder(BorderFactory.createEtchedBorder());
		}
		
		private JComponent leftSide;
		private JComponent rightSide;
	}
	
	private HashMap<SelectableRow, String> clickableComponentToContentsFileNameMap;
	private DashboardRightSideDescriptionPanel rightSideDescriptionPanel;
	private static final int INDENT_PER_LEVEL = 20;
	private static final String TEAM_RIGHT_PANEL_FILE_NAME = "dashboard/1A.html";
	private static final String SCOPE_AND_VISION_RIGHT_PANEL_FILE_NAME = "dashboard/1B.html";
	private static final String CRITICAL_THREATS_RIGHT_PANEL_FILE_NAME = "dashboard/1C.html";
}
