/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.conservationmeasures.eam.main.EAM;

import com.jhlabs.awt.GridLayoutPlus;

public class PlanningCustomizationPanel extends JPanel
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
		
		JRadioButton strategicViewRadioButton = new JRadioButton();
		JRadioButton monitoringViewRadioButton = new JRadioButton();
		JRadioButton workViewRadioButton = new JRadioButton();
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(strategicViewRadioButton);
		buttonGroup.add(monitoringViewRadioButton);
		buttonGroup.add(workViewRadioButton);
		
		jPanel.add(new JLabel(EAM.text("Strategic Plan")));
		jPanel.add(strategicViewRadioButton);
		
		jPanel.add(new JLabel(EAM.text("Monitoring Plan")));
		jPanel.add(monitoringViewRadioButton);
		
		jPanel.add(new JLabel(EAM.text("Work Plan")));
		jPanel.add(workViewRadioButton);
		
		add(jPanel);
	}
}
