/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.conservationmeasures.eam.main.EAM;

import com.jhlabs.awt.GridLayoutPlus;

public class PlanningCustomizationPanel extends JPanel implements ActionListener
{
	public PlanningCustomizationPanel()
	{
		super();
		createLegendButtonPanel();
		setBorder(BorderFactory.createTitledBorder(EAM.text("Standard Views")));
	}

	protected void createLegendButtonPanel()
	{
		JPanel jPanel = new JPanel(new GridLayoutPlus(3, 2));
		ButtonGroup buttonGroup = new ButtonGroup();
		addRadioButton(jPanel, buttonGroup, EAM.text("Strategic Plan"));
		addRadioButton(jPanel, buttonGroup, EAM.text("Monitoring Plan"));
		addRadioButton(jPanel, buttonGroup, EAM.text("Work Plan"));
		
		add(jPanel);
	}

	private void addRadioButton(JPanel jPanel, ButtonGroup buttonGroup, String buttonName)
	{
		JRadioButton radioButton = new JRadioButton();
		buttonGroup.add(radioButton);
		
		jPanel.add(new JLabel(buttonName));
		jPanel.add(radioButton);
	}

	public void actionPerformed(ActionEvent e)
	{
		
	}
}
