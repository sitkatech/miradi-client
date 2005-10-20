/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JDialog;
import javax.swing.JList;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.Indicator;
import org.conservationmeasures.eam.diagram.nodes.Objective;
import org.conservationmeasures.eam.diagram.nodes.Objectives;
import org.conservationmeasures.eam.diagram.nodes.ThreatPriority;
import org.conservationmeasures.eam.icons.ThreatPriorityIcon;
import org.martus.swing.UiButton;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiLabel;
import org.martus.swing.UiTextField;
import org.martus.swing.UiVBox;
import org.martus.swing.Utilities;

public class NodePropertiesDialog extends JDialog implements ActionListener
{
	public NodePropertiesDialog(Frame parent, String title, DiagramNode node)
			throws HeadlessException
	{
		super(parent, title);
		
		UiVBox bigBox = new UiVBox();
		bigBox.add(createTextField(node.getText()));
		bigBox.add(createIndicator(node.getIndicator()));
		if(node.canHaveObjective())
			bigBox.add(createObjectiveDropdown(Objectives.getAllObjectives(node),node.getObjective()));
		if(node.canHavePriority())
			bigBox.add(createThreatLevelDropdown(node.getThreatPriority()));
		bigBox.add(createButtonBar());

		Container contents = getContentPane();
		contents.add(bigBox);
		pack();
		setLocation(node.getLocation());
		setResizable(true);
		setModal(true);
	}
	
	private Component createTextField(String initialText)
	{
		UiLabel textLabel = new UiLabel(EAM.text("Label|Label"));
		textField = new UiTextField(initialText);
		textField.requestFocus(true);
		textField.selectAll();

		Box labelBar = Box.createHorizontalBox();
		Component[] components = new Component[] {textLabel, new UiLabel(" "), textField, Box.createHorizontalGlue()};
		Utilities.addComponentsRespectingOrientation(labelBar, components);
		return labelBar;
	}
	
	private Component createThreatLevelDropdown(ThreatPriority currentPriority)
	{
		UiLabel textThreatLevel = new UiLabel(EAM.text("Label|Threat Level"));
		dropdownThreatPriority = new UiComboBox();
		dropdownThreatPriority.setRenderer(new ThreatRenderer());
		
		dropdownThreatPriority.addItem(ThreatPriority.createPriorityVeryHigh());
		dropdownThreatPriority.addItem(ThreatPriority.createPriorityHigh());
		dropdownThreatPriority.addItem(ThreatPriority.createPriorityMedium());
		dropdownThreatPriority.addItem(ThreatPriority.createPriorityLow());
		dropdownThreatPriority.addItem(ThreatPriority.createPriorityNone());

		dropdownThreatPriority.setSelectedItem(currentPriority);
		
		Box threatLevelBar = Box.createHorizontalBox();
		Component[] components = new Component[] {textThreatLevel, new UiLabel(" "), dropdownThreatPriority, Box.createHorizontalGlue()};
		Utilities.addComponentsRespectingOrientation(threatLevelBar, components);
		return threatLevelBar;
	}
	
	public Component createObjectiveDropdown(Objectives objectives, Objective currentObjective)
	{
		UiLabel textObjective = new UiLabel(EAM.text("Label|Objective"));
		dropdownObjective = new UiComboBox();
		for(int i = 0; i < objectives.getSize(); ++i)
		{
			dropdownObjective.addItem(objectives.get(i));
		}
		dropdownObjective.setSelectedItem(currentObjective);
		
		Box ObjectiveBar = Box.createHorizontalBox();
		Component[] components = new Component[] {textObjective, new UiLabel(" "), dropdownObjective, Box.createHorizontalGlue()};
		Utilities.addComponentsRespectingOrientation(ObjectiveBar, components);
		return ObjectiveBar;
	}
	
	public Component createIndicator(Indicator indicator)
	{
		UiLabel textIndicator = new UiLabel(EAM.text("Label|Indicator"));
		dropdownIndicator = new UiComboBox();
		dropdownIndicator.addItem(new Indicator());
		dropdownIndicator.addItem(new Indicator(1));
		dropdownIndicator.addItem(new Indicator(2));
		dropdownIndicator.addItem(new Indicator(3));

		dropdownIndicator.setSelectedItem(indicator);
		
		Box indicatorBar = Box.createHorizontalBox();
		Component[] components = new Component[] {textIndicator, new UiLabel(" "), dropdownIndicator,Box.createHorizontalGlue()};
		Utilities.addComponentsRespectingOrientation(indicatorBar, components);
		return indicatorBar;
	}
	
	class ThreatRenderer extends DefaultListCellRenderer
	{
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
		{
			Component cell = super.getListCellRendererComponent(list, value, index, isSelected,	cellHasFocus);
			setIcon(new ThreatPriorityIcon((ThreatPriority)value));
			return cell;
		}
	}

	private Box createButtonBar()
	{
		okButton = new UiButton(EAM.text("Button|OK"));
		okButton.addActionListener(this);
		getRootPane().setDefaultButton(okButton);
		cancelButton = new UiButton(EAM.text("Button|Cancel"));
		cancelButton.addActionListener(this);

		Box buttonBar = Box.createHorizontalBox();
		Component[] components = new Component[] {Box.createHorizontalGlue(), okButton, cancelButton};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
	}

	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == okButton)
			result = true;
		dispose();
	}
	
	public boolean getResult()
	{
		return result;
	}
	
	public String getText()
	{
		return textField.getText();
	}
	
	public ThreatPriority getPriority()
	{
		return (ThreatPriority)dropdownThreatPriority.getSelectedItem();
	}
	
	public Indicator getIndicator()
	{
		return (Indicator)dropdownIndicator.getSelectedItem();
	}

	public Objective getObjective()
	{
		return (Objective)dropdownObjective.getSelectedItem();
	}

	boolean result;
	UiTextField textField;
	UiComboBox dropdownThreatPriority;
	UiComboBox dropdownIndicator;
	UiComboBox dropdownObjective;
	UiButton okButton;
	UiButton cancelButton;
}
