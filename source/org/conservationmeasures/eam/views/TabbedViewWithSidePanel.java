/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.main.MainWindow;

abstract public class TabbedViewWithSidePanel extends TabbedView
{

	public TabbedViewWithSidePanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}
	
	public void hideSidePanel() throws Exception
	{
		sidePanel.setVisible(false);
	}
	
	public void showSidePanel() throws Exception
	{
		sidePanel.setVisible(true);
	}
	
	public void becomeActive() throws Exception
	{
		super.becomeActive();
		add(sidePanel, BorderLayout.AFTER_LINE_ENDS);
	}
	
	public void becomeInactive() throws Exception
	{
		remove(sidePanel);
		super.becomeInactive();
	}
	
	public void setSidePanel(DisposablePanel panel)
	{
		sidePanel = panel;
	}
	
	JPanel sidePanel;
}
