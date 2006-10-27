/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import com.jhlabs.awt.BasicGridLayout;

abstract public class FieldEditingPanel extends JPanel
{
	
	abstract public String getPanelDescriptionText();
	
	public FieldEditingPanel()
	{
		super(new BasicGridLayout(0, 2));
	}

	public Component createFieldPanel(Component component)
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(component, BorderLayout.BEFORE_LINE_BEGINS);
		return panel;
	}

	public void addFieldComponent(Component component)
	{
		add(createFieldPanel(component));
	}
}
