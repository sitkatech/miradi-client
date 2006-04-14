/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

public class WizardPanel extends JPanel
{
	public WizardPanel()
	{
		super(new BorderLayout());
	}

	public void setContents(JPanel contents)
	{
		removeAll();

		add(contents, BorderLayout.CENTER);
		allowSplitterToHideUsCompletely();
		validate();
	}
	
	public void next() throws Exception
	{
	}
	
	public void previous() throws Exception
	{
	}

	private void allowSplitterToHideUsCompletely()
	{
		setMinimumSize(new Dimension(0, 0));
	}
	

}

