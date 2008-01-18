/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
		setStatus(STATUS_READY);
	}
	
	public void setStatusAllLayersVisible()
	{
		setStatusReady();
	}

	public void setStatusHiddenLayers()
	{
		setStatus(STATUS_READY + EAM.text("Status|: Some Factors Hidden"));
	}

	public void setStatusError(CommandFailedException e)
	{
		setStatus(e.getMessage());
	}
	
	public void setStatus(String text)
	{
		statusSummary.setText(text);
	}

	private final String STATUS_READY = EAM.text("Status|Ready");
	private JLabel statusSummary;
}
