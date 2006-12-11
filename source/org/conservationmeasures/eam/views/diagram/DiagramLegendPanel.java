/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import org.conservationmeasures.eam.diagram.cells.DiagramStrategy;
import org.conservationmeasures.eam.diagram.cells.DiagramTarget;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.icons.ObjectiveIcon;
import org.conservationmeasures.eam.icons.ProjectScopeIcon;
import org.conservationmeasures.eam.icons.StressIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.Desire;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.utils.LocationHolder;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;

import com.jhlabs.awt.BasicGridLayout;

public class DiagramLegendPanel extends JPanel implements ActionListener
{
	public DiagramLegendPanel(MainWindow mainWindowToUse)
	{
		super(new BasicGridLayout(0, 1));
		setBorder(new EmptyBorder(5,5,5,5));
		
		mainWindow = mainWindowToUse;
		
		UiLabel title = new UiLabel(EAM.text("LEGEND"));
		title.setFont(title.getFont().deriveFont(Font.BOLD));
		title.setBorder(new LineBorder(Color.BLACK, 2));
		title.setHorizontalAlignment(UiLabel.CENTER);
		add(title);

		
		add(createLegendButtonPanel(mainWindow.getActions()));

		setMinimumSize(new Dimension(0,0));
	}
	
	private JPanel createLegendButtonPanel(Actions actions)
	{
		JPanel jpanel = new JPanel(new BasicGridLayout(0,3));
		addButtonLine(jpanel, "Project Scope", new ProjectScopeIcon(),false);
		
		addButtonLine(jpanel, Target.OBJECT_NAME, actions.get(ActionInsertTarget.class));
		addButtonLine(jpanel, Factor.OBJECT_NAME_THREAT, actions.get(ActionInsertDirectThreat.class));
		addButtonLine(jpanel, Factor.OBJECT_NAME_IN_CONTRIBUTING_THREAT, actions.get(ActionInsertContributingFactor.class));
		addButtonLine(jpanel, Strategy.OBJECT_NAME, actions.get(ActionInsertStrategy.class));
		addButtonLine(jpanel, FactorLink.OBJECT_NAME, actions.get(ActionInsertFactorLink.class));
		
		addButtonLine(jpanel, Goal.OBJECT_NAME, new GoalIcon(),true);
		addButtonLine(jpanel, Objective.OBJECT_NAME, new ObjectiveIcon(),true);
		addButtonLine(jpanel, Indicator.OBJECT_NAME, new IndicatorIcon(),true);
		addButtonLine(jpanel, "Stress", new StressIcon(),false);
		
		return jpanel;
	}
	
	private void addButtonLine(JPanel jpanel, String text, EAMAction action)
	{
		JButton button = new LocationButton(action);
		button.setText(null);
		jpanel.add(button);
		jpanel.add(new UiLabel(EAM.text(text)));
		JCheckBox checkBox = new JCheckBox();
		checkBox.setSelected(true);
		checkBox.putClientProperty(LAYER, new String(text));
		checkBox.addActionListener(this);
		jpanel.add(checkBox);
	}
	
	private void addButtonLine(JPanel jpanel, String text, Icon icon, boolean hasCheckBox)
	{
		JPanel centeringPanel = new JPanel();
		centeringPanel.add(new UiLabel("",icon,AbstractButton.LEFT));
		jpanel.add(centeringPanel);
		jpanel.add(new UiLabel(EAM.text(text)));
		if (hasCheckBox)
		{
			JCheckBox checkBox = new JCheckBox();
			checkBox.setSelected(true);
			checkBox.putClientProperty(LAYER, new String(text));
			checkBox.addActionListener(this);
			jpanel.add(checkBox);
		}
		else
			jpanel.add(new UiLabel(""));
	}

	
	public void actionPerformed(ActionEvent event)
	{
		JCheckBox checkBox = (JCheckBox)event.getSource();

		String property = (String) checkBox.getClientProperty(LAYER);

		LayerManager manager = mainWindow.getProject().getLayerManager();

		
		if (property.equals(Strategy.OBJECT_NAME))
			manager.setVisibility(DiagramStrategy.class, checkBox.isSelected());
		else if (property.equals(Target.OBJECT_NAME))
			manager.setVisibility(DiagramTarget.class, checkBox.isSelected());
		else if (property.equals(Factor.OBJECT_NAME_IN_CONTRIBUTING_THREAT))
			manager.setContributingFactorsVisible(checkBox.isSelected());
		else if (property.equals(Factor.OBJECT_NAME_THREAT))
			manager.setDirectThreatsVisible(checkBox.isSelected());
		else if (property.equals(Target.OBJECT_NAME))
			manager.setVisibility(DiagramTarget.class, checkBox.isSelected());
		else if (property.equals(FactorLink.OBJECT_NAME))
			manager.setFactorLinksVisible(checkBox.isSelected());
		else if (property.equals(Objective.OBJECT_NAME))
			manager.setDesiresVisible(checkBox.isSelected());
		else if (property.equals(Goal.OBJECT_NAME))
			manager.setDesiresVisible(checkBox.isSelected());
		else if (property.equals(Indicator.OBJECT_NAME))
			manager.setIndicatorsVisible(checkBox.isSelected());

		mainWindow.getProject().updateVisibilityOfFactors();
		mainWindow.updateStatusBar();
			
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
	final static String LAYER = "LAYER";
	MainWindow mainWindow;
}