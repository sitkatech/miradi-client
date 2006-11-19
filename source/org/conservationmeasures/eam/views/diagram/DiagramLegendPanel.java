/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.icons.ConnectionIcon;
import org.conservationmeasures.eam.icons.DirectThreatIcon;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.icons.IndirectFactorIcon;
import org.conservationmeasures.eam.icons.InterventionIcon;
import org.conservationmeasures.eam.icons.ObjectiveIcon;
import org.conservationmeasures.eam.icons.ProjectScopeIcon;
import org.conservationmeasures.eam.icons.StressIcon;
import org.conservationmeasures.eam.icons.TargetIcon;
import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.UiLabel;

import com.jhlabs.awt.BasicGridLayout;

public class DiagramLegendPanel extends JPanel
{
	public DiagramLegendPanel()
	{
		super(new BasicGridLayout(0, 1));
		setBorder(new EmptyBorder(5,5,5,5));
		
		UiLabel title = new UiLabel(EAM.text("LEGEND"));
		title.setFont(title.getFont().deriveFont(Font.BOLD));
		title.setBorder(new LineBorder(Color.BLACK, 2));
		title.setHorizontalAlignment(UiLabel.CENTER);
		add(title);
		add(new UiLabel(EAM.text("Project Scope"), new ProjectScopeIcon(), UiLabel.LEADING));
		add(new UiLabel(EAM.text("Target"), new TargetIcon(), UiLabel.LEADING));
		add(new UiLabel(EAM.text("Direct Threat"), new DirectThreatIcon(), UiLabel.LEADING));
		add(new UiLabel(EAM.text("Indirect Factor"), new IndirectFactorIcon(), UiLabel.LEADING));
		add(new UiLabel(EAM.text("Strategy"), new InterventionIcon(), UiLabel.LEADING));
		add(new UiLabel(EAM.text("Link"), new ConnectionIcon(), UiLabel.LEADING));
		
		add(new UiLabel(EAM.text("Goal"), new GoalIcon(), UiLabel.LEADING));
		add(new UiLabel(EAM.text("Objective"), new ObjectiveIcon(), UiLabel.LEADING));
		add(new UiLabel(EAM.text("Indicator"), new IndicatorIcon(), UiLabel.LEADING));
		add(new UiLabel(EAM.text("Stress"), new StressIcon(), UiLabel.LEADING));
		
		setMinimumSize(new Dimension(0,0));
	}

}