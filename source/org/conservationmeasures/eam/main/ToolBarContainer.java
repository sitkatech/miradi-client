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

import org.conservationmeasures.eam.wizard.WizardTitlePanel;

public class ToolBarContainer extends JPanel
{
	public ToolBarContainer(WizardTitlePanel wizardTitlePanelToUse)
	{
		super(new BorderLayout());
		wizardTitlePanel = wizardTitlePanelToUse;

	}
	
	public void setToolBar(JToolBar toolBarToUse)
	{
		clear();
		toolBar = toolBarToUse;
		add(toolBar, BorderLayout.CENTER);
		
		add(wizardTitlePanel, BorderLayout.AFTER_LAST_LINE);
	}
	
	public void clear()
	{
		if (toolBar!=null) 
			toolBar.removeAll();
		toolBar = null;
		removeAll();
	}

	JComponent toolBar;
	private WizardTitlePanel wizardTitlePanel;
}
