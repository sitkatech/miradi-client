/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainStatusBar extends JPanel
{
	public MainStatusBar()
	{
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS) );
		add(new JLabel(EAM.text("Status|Ready")));
	}

}
