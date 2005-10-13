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
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.NodeDataMap;
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
	public NodePropertiesDialog(Frame parent, String title, NodeDataMap nodeDataMap)
			throws HeadlessException
	{
		super(parent, title);
		UiVBox bigBox = new UiVBox();
		bigBox.add(createTextField(nodeDataMap.getString(EAMGraphCell.TEXT)));
		bigBox.add(createThreatLevelDropdown(nodeDataMap.getInt(DiagramNode.PRIORITY)));
		bigBox.add(createButtonBar());

		Container contents = getContentPane();
		contents.add(bigBox);
		pack();
		setLocation(nodeDataMap.getPoint(EAMGraphCell.LOCATION));
		setResizable(true);
		setModal(true);
	}
	
	private UiTextField createTextField(String initialText)
	{
		textField = new UiTextField(initialText);
		textField.requestFocus(true);
		textField.selectAll();
		return textField;
	}
	
	private Component createThreatLevelDropdown(int currentPriority)
	{
		if(currentPriority == ThreatPriority.PRIORITY_NOT_USED)
			return new UiLabel("");
		UiLabel textThreatLevel = new UiLabel(EAM.text("Label|Threat Level"));
		dropdownThreatPriority = new UiComboBox();
		dropdownThreatPriority.setRenderer(new ThreatRenderer());
		String[] items = ThreatPriority.getPriorityStringList();
		for (int i = 0; i < items.length; i++) 
		{
			dropdownThreatPriority.addItem(items[i]);
		}

		int selectedPriority = currentPriority;
		dropdownThreatPriority.setSelectedIndex(selectedPriority);
		
		Box threatLevelBar = Box.createHorizontalBox();
		Component[] components = new Component[] {Box.createHorizontalGlue(), textThreatLevel, dropdownThreatPriority};
		Utilities.addComponentsRespectingOrientation(threatLevelBar, components);
		return threatLevelBar;
	}
	
	class ThreatRenderer extends DefaultListCellRenderer
	{
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
		{
			Component cell = super.getListCellRendererComponent(list, value, index, isSelected,	cellHasFocus);
			setIcon(new ThreatPriorityIcon((String)value));
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
	
	public int getPriority()
	{
		return dropdownThreatPriority.getSelectedIndex();
	}

	boolean result;
	UiTextField textField;
	UiComboBox dropdownThreatPriority;
	UiButton okButton;
	UiButton cancelButton;
}
