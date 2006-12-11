/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.actions.ActionInsertContributingFactor;
import org.conservationmeasures.eam.actions.ActionInsertDirectThreat;
import org.conservationmeasures.eam.actions.ActionInsertFactorLink;
import org.conservationmeasures.eam.actions.ActionInsertStrategy;
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.icons.ObjectiveIcon;
import org.conservationmeasures.eam.icons.ProjectScopeIcon;
import org.conservationmeasures.eam.icons.StressIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.LocationHolder;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;

import com.jhlabs.awt.BasicGridLayout;

public class DiagramLegendPanel extends JPanel
{
	public DiagramLegendPanel(Actions actions)
	{
		super(new BasicGridLayout(0, 1));
		setBorder(new EmptyBorder(5,5,5,5));
		
		UiLabel title = new UiLabel(EAM.text("LEGEND"));
		title.setFont(title.getFont().deriveFont(Font.BOLD));
		title.setBorder(new LineBorder(Color.BLACK, 2));
		title.setHorizontalAlignment(UiLabel.CENTER);
		add(title);

		
		add(createLegendButtonPanel(actions));

		setMinimumSize(new Dimension(0,0));
	}
	
	private JPanel createLegendButtonPanel(Actions actions)
	{
		JPanel jpanel = new JPanel(new BasicGridLayout(0,3));
		addButtonLine(jpanel, "Project Scope", new ProjectScopeIcon());
		
		addButtonLine(jpanel, "Target", actions.get(ActionInsertTarget.class));
		addButtonLine(jpanel, "Direct Threat", actions.get(ActionInsertDirectThreat.class));
		addButtonLine(jpanel, "Contributing Factor", actions.get(ActionInsertContributingFactor.class));
		addButtonLine(jpanel, "Strategy", actions.get(ActionInsertStrategy.class));
		addButtonLine(jpanel, "Link", actions.get(ActionInsertFactorLink.class));
		
		addButtonLine(jpanel, "Goal", new GoalIcon());
		addButtonLine(jpanel, "Objective", new ObjectiveIcon());
		addButtonLine(jpanel, "Indicator", new IndicatorIcon());
		addButtonLine(jpanel, "Stress", new StressIcon());
		
		return jpanel;
	}
	
	private void addButtonLine(JPanel jpanel, String text, EAMAction action)
	{
		JButton button = new LocationButton(action);
		button.setText(null);
		jpanel.add(button);
		jpanel.add(new UiLabel(EAM.text(text)));
		jpanel.add(new JCheckBox());
	}
	
	private void addButtonLine(JPanel jpanel, String text, Icon icon)
	{
		JPanel centeringPanel = new JPanel();
		centeringPanel.add(new UiLabel("",icon,AbstractButton.LEFT));
		jpanel.add(centeringPanel);
		jpanel.add(new UiLabel(EAM.text(text)));
		jpanel.add(new UiLabel(""));
	}

	class LocationButton extends UiButton implements LocationHolder
	{
		LocationButton(Action action)
		{
			super(action);
		}
		
		public boolean hasLocation()
		{
			return false;
		}
	}
}