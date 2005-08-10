/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.FlowLayout;

import javax.swing.JPanel;

public class ToolBarContainer extends JPanel
{
	public ToolBarContainer()
	{
		FlowLayout flow = new FlowLayout();
		flow.setAlignment(FlowLayout.LEADING);
		setLayout(flow);
	}

}
