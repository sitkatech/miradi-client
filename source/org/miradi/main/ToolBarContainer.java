/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.main;

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
	
	public void setToolBar(JToolBar toolBarToUse)
	{
		clear();
		toolBar = toolBarToUse;
		add(toolBar, BorderLayout.CENTER);
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
