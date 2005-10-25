/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.conservationmeasures.eam.exceptions.CommandFailedException;

public class MainStatusBar extends JPanel
{
	public MainStatusBar()
	{
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS) );
		statusSummary = new JLabel();
		add(statusSummary);
		
	}
	
	public void setStatusReady()
	{
		setStatus(EAM.text("Status|Ready"));
	}
	
	public void setStatusError(CommandFailedException e)
	{
		setStatus(e.getMessage());
	}
	
	public void setStatus(String text)
	{
		statusSummary.setText(text);
	}

	private JLabel statusSummary;
}
