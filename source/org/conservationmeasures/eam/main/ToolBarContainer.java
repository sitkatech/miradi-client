/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class ToolBarContainer extends JPanel
{
	public ToolBarContainer()
	{
		super(new BorderLayout());
	}
	
	public void addToolBar(JToolBar toolBarToUse)
	{
		toolBar = toolBarToUse;
		add(toolBar);
	}
	
	public void clear()
	{
		if (toolBar!=null) 
			toolBar.removeAll();
		toolBar = null;
		removeAll();
	}

	JComponent toolBar;
}
