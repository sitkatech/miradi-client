/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
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
import com.jhlabs.awt.GridLayoutPlus;

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
		JPanel jpanel = new JPanel(new GridLayoutPlus(0,3));
		
		addIconLineWithCheckBox(jpanel, SCOPE_BOX_TEXT, new ProjectScopeIcon());
		
		addButtonLineWithCheckBox(jpanel, Target.OBJECT_NAME, actions.get(ActionInsertTarget.class));
		addButtonLineWithCheckBox(jpanel, Factor.OBJECT_NAME_THREAT, actions.get(ActionInsertDirectThreat.class));
		addButtonLineWithCheckBox(jpanel, Factor.OBJECT_NAME_CONTRIBUTING_FACTOR, actions.get(ActionInsertContributingFactor.class));
		addButtonLineWithCheckBox(jpanel, Strategy.OBJECT_NAME, actions.get(ActionInsertStrategy.class));
		addButtonLineWithCheckBox(jpanel, FactorLink.OBJECT_NAME, actions.get(ActionInsertFactorLink.class));
		addTargetLinkLine(jpanel, TARGET_LINKS_TEXT);
		
		addIconLineWithCheckBox(jpanel, Goal.OBJECT_NAME, new GoalIcon());
		addIconLineWithCheckBox(jpanel, Objective.OBJECT_NAME, new ObjectiveIcon());
		addIconLineWithCheckBox(jpanel, Indicator.OBJECT_NAME, new IndicatorIcon());
		addIconLineWithoutCheckBox(jpanel, "Stress", new StressIcon());
		
		return jpanel;
	}
	
	private void addTargetLinkLine(JPanel panel, String text)
	{
		panel.add(new JLabel(""));
		panel.add(new UiLabel(EAM.text(text)));
		targetLinkCheckBox = createCheckBox(text);
		panel.add(targetLinkCheckBox);
	}
	
	private void addButtonLineWithCheckBox(JPanel jpanel, String text, EAMAction action)
	{
		JButton button = new LocationButton(action);
		jpanel.add(button);
		jpanel.add(new UiLabel(EAM.text(text)));
		jpanel.add(createCheckBox(text));
	}
	
	private void addIconLineWithCheckBox(JPanel jpanel, String text, Icon icon)
	{
		addIconLine(jpanel, text, icon, createCheckBox(text));
	}


	private void addIconLineWithoutCheckBox(JPanel jpanel, String text, Icon icon)
	{
		addIconLine(jpanel, text, icon, new UiLabel(""));
	}

	private JCheckBox createCheckBox(String text)
	{
		JCheckBox component = new JCheckBox();
		component.setSelected(true);
		component.putClientProperty(LAYER, new String(text));
		component.addActionListener(this);
		return component;
	}
	
	private void addIconLine(JPanel jpanel, String text, Icon icon, JComponent component)
	{
		jpanel.add(new JLabel(icon));
		jpanel.add(new UiLabel(EAM.text(text)));
		jpanel.add(component);
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
		else if (property.equals(Factor.OBJECT_NAME_CONTRIBUTING_FACTOR))
			manager.setContributingFactorsVisible(checkBox.isSelected());
		else if (property.equals(Factor.OBJECT_NAME_THREAT))
			manager.setDirectThreatsVisible(checkBox.isSelected());
		else if (property.equals(Target.OBJECT_NAME))
			manager.setVisibility(DiagramTarget.class, checkBox.isSelected());
		else if (property.equals(FactorLink.OBJECT_NAME))
		{
			manager.setFactorLinksVisible(checkBox.isSelected());
			targetLinkCheckBox.setEnabled(checkBox.isSelected());
		}
		else if (property.equals(TARGET_LINKS_TEXT))
			manager.setTargetLinksVisible(checkBox.isSelected());
		else if (property.equals(Goal.OBJECT_NAME))
			manager.setGoalsVisible(checkBox.isSelected());
		else if (property.equals(Objective.OBJECT_NAME))
			manager.setObjectivesVisible(checkBox.isSelected());
		else if (property.equals(Indicator.OBJECT_NAME))
			manager.setIndicatorsVisible(checkBox.isSelected());
		else if (property.equals(SCOPE_BOX_TEXT))
			manager.setScopeBoxVisible(checkBox.isSelected());
		
		mainWindow.getProject().updateVisibilityOfFactors();
		mainWindow.updateStatusBar();
			
	}
	
	class LocationButton extends UiButton implements LocationHolder
	{
		LocationButton(EAMAction action)
		{
			super(action);
			setText(null);
			setMargin(new Insets(0, 0, 0 ,0));
		}
		
		public boolean hasLocation()
		{
			return false;
		}
	}
	
	final static String LAYER = "LAYER";
	final static String TARGET_LINKS_TEXT = "Target link";
	final static String SCOPE_BOX_TEXT = "Scope Box";
	
	MainWindow mainWindow;
	JCheckBox targetLinkCheckBox;
}