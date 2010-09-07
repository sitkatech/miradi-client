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
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnPanel;
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
		clickableComponents = new Vector<JComponent>();
		
		addLeftPanel();
		addRightPanel();		
	}
	
	private void addRightPanel() throws Exception
	{
		MiradiHtmlViewer descriptionPanel = new FlexibleWidthHtmlViewer(getMainWindow(), getMainWindow().getHyperlinkHandler());
		String htmlText = Translation.getHtmlContent("DashboardDescription.html");
		descriptionPanel.setText(htmlText);
		
		add(descriptionPanel);
	}

	private void addLeftPanel()
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
		
		add(leftMainPanel);
	}

	private void addTeamMembersRow(TwoColumnPanel leftMainPanel)
	{
		addSubHeaderRow(leftMainPanel, EAM.text("1A. Define Initial Project Team"));
	
		Box box2 = createBorderedBox();
		box2.add(new PanelTitleLabel(EAM.text("Team Members:")), BorderLayout.BEFORE_FIRST_LINE);
		box2.add(new PanelTitleLabel(getDashboardData(Dashboard.PSEUDO_TEAM_MEMBER_COUNT)));
		
		leftMainPanel.add(new FillerLabel());
		leftMainPanel.add(box2);
	}

	private Box createBorderedBox()
	{
		Box box = Box.createHorizontalBox();
		box.setBorder(BorderFactory.createEtchedBorder());
		box.addMouseListener(new ClickHandler(box));
		clickableComponents.add(box);
		return box;
	}

	private void addSubHeaderRow(TwoColumnPanel leftMainPanel, String text)
	{
		Box box1 = createBorderedBox();
		box1.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		box1.add(new PanelTitleLabel(text), BorderLayout.BEFORE_FIRST_LINE);
		box1.add(Box.createHorizontalStrut(GAP_BETWEEN_LABEL_AND_DROPDOWN));
		box1.add(new FillerLabel());
		leftMainPanel.add(box1);
		leftMainPanel.add(new FillerLabel());
	}
	
	private void addScopeVisionAndTargetsRow(TwoColumnPanel leftMainPanel)
	{
		addSubHeaderRow(leftMainPanel, EAM.text("1B. Define Scope Vision and Targets"));

		Box box1 = createBorderedBox();
		box1.add(new PanelTitleLabel(EAM.text("Define Project Scope:")), BorderLayout.BEFORE_FIRST_LINE);
		String scopeVisionCount = getDashboardData(Dashboard.PSEUDO_TEAM_MEMBER_COUNT);
		box1.add(new PanelTitleLabel(EAM.substitute(EAM.text("Created (%s chars)"), scopeVisionCount)));
		leftMainPanel.add(new FillerLabel());
		leftMainPanel.add(box1);

		Box box2 = createBorderedBox();
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%targetCount", getDashboardData(Dashboard.PSEUDO_TARGET_COUNT));
		box2.add(new PanelTitleLabel(EAM.text("Select Conservation Targets:")));
		box2.add(new PanelTitleLabel(EAM.substitute(EAM.text("%targetCount created"), tokenReplacementMap)));
		leftMainPanel.add(new FillerLabel());
		leftMainPanel.add(box2);
		
		Box box3 = createBorderedBox();
		String humanWelfareTargetCount = EAM.substitute(EAM.text("%s created"), getDashboardData(Dashboard.PSEUDO_HUMAN_WELFARE_TARGET_COUNT));
		box3.add(new PanelTitleLabel(EAM.text("Add Human Welfare Targets:")));
		box3.add(new PanelTitleLabel(humanWelfareTargetCount));
		leftMainPanel.add(new FillerLabel());
		leftMainPanel.add(box3);
		
		Box box4 = createBorderedBox();
		String text = EAM.text("Describe Status of Targets:");
		HashMap<String, String> statusDescriptionTokenReplacementMap1 = new HashMap<String, String>();
		statusDescriptionTokenReplacementMap1.put("%targetWithKeaCount", getDashboardData(Dashboard.PSEUDO_TARGET_WITH_KEA_COUNT));
		String targetStatusDescription = EAM.substitute(EAM.text("%targetWithKeaCount targets have KEA"), statusDescriptionTokenReplacementMap1);
		box4.add(new PanelTitleLabel(text));
		box4.add(new PanelTitleLabel(targetStatusDescription));
		leftMainPanel.add(new FillerLabel());
		leftMainPanel.add(box4);
		
		Box box5 = createBorderedBox();
		HashMap<String, String> statusDescriptionTokenReplacementMap2 = new HashMap<String, String>();
		statusDescriptionTokenReplacementMap2.put("%targetWithSimpleViabilityCount", getDashboardData(Dashboard.PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT));
		box5.add(new PanelTitleLabel(EAM.substitute(EAM.text("%targetWithSimpleViabilityCount targets have simple viablity information"), statusDescriptionTokenReplacementMap2)));
		leftMainPanel.add(new FillerLabel());
		leftMainPanel.add(box5);
	}
	
	private void addIdentifyCriticalThreatsRow(TwoColumnPanel leftMainPanel)
	{
		addSubHeaderRow(leftMainPanel, EAM.text("1C. Identify Critical Threats"));
		
		Box box1 = createBorderedBox();
		HashMap<String, String> threatsTokenReplacementMap = new HashMap<String, String>();
		threatsTokenReplacementMap.put("%threatCount", getDashboardData(Dashboard.PSEUDO_THREAT_COUNT));
		threatsTokenReplacementMap.put("%threatWithTaxonomyCount", getDashboardData(Dashboard.PSEUDO_THREAT_WITH_TAXONOMY_COUNT));
		box1.add(new PanelTitleLabel(EAM.text("Identify Direct Threats:")));
		box1.add(new PanelTitleLabel(EAM.substitute(EAM.text("%threatCount Direct Threats created"), threatsTokenReplacementMap)));
		leftMainPanel.add(new FillerLabel());
		leftMainPanel.add(box1);
		
		Box box2 = createBorderedBox();
		box2.add(new PanelTitleLabel(EAM.substitute(EAM.text("%threatWithTaxonomyCount of %threatCount have taxonomy assignments"), threatsTokenReplacementMap)));
		leftMainPanel.add(new FillerLabel());
		leftMainPanel.add(box2);
		
		Box box3 = createBorderedBox();
		String text = EAM.text("Rank Direct Threats:");
		HashMap<String, String> threatTargetLinksTokenReplacementMap = new HashMap<String, String>();
		threatTargetLinksTokenReplacementMap.put("%threatTargetLinkCount", getDashboardData(Dashboard.PSEUDO_THREAT_TARGET_LINK_COUNT));
		threatTargetLinksTokenReplacementMap.put("%threatTargetWithRatingCount", getDashboardData(Dashboard.PSEUDO_THREAT_TARGET_LINK_WITH_RATING_COUNT));
		String threatTargetLinkDescription = EAM.substitute(EAM.text("%threatTargetWithRatingCount of %threatTargetLinkCount threat/target links ranked"), threatTargetLinksTokenReplacementMap);
		box3.add(new PanelTitleLabel(text));
		box3.add(new PanelTitleLabel(threatTargetLinkDescription));
		leftMainPanel.add(new FillerLabel());
		leftMainPanel.add(box3);
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
		public ClickHandler(JComponent clickableComponentToUse)
		{
			clickableComponent = clickableComponentToUse;
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			super.mouseClicked(e);
			
			resetAllComponentBorders();
			clickableComponent.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
		}
	
		private void resetAllComponentBorders()
		{
			for(JComponent component : clickableComponents)
			{
				component.setBorder(BorderFactory.createEtchedBorder());
			}
		}
		
		private JComponent clickableComponent;
	}
	
	private Vector<JComponent> clickableComponents;
	private static final int INDENT_PER_LEVEL = 20;
	private static final int GAP_BETWEEN_LABEL_AND_DROPDOWN = 4;
}
