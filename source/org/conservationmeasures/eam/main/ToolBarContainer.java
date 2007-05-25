/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class ToolBarContainer extends JPanel
{
	public ToolBarContainer()
	{
		super(new BorderLayout());
	}
	
	public void addToolBar(JComponent compToUse)
	{
		comp = compToUse;
		add(comp);
	}
	
	public void dispose()
	{
		if (comp!=null) 
			comp.removeAll();
		comp = null;
		removeAll();
	}

	JComponent comp;
}
