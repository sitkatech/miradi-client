/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.LayoutManager2;

import javax.swing.JPanel;

public class DisposablePanel extends JPanel
{
	public DisposablePanel()
	{
		super();
	}
	
	public DisposablePanel(LayoutManager2 layoutToUse)
	{
		super(layoutToUse);
	}
	
	public void dispose()
	{
		
	}
}
