/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.layout;

import javax.swing.JPanel;

public class OneColumnPanel extends JPanel
{
	public OneColumnPanel()
	{
		super(new OneColumnGridLayout());
	}
	
	public void setGaps(int gap)
	{
		getGridLayout().setGaps(gap);
	}
	
	public void setFullWidth()
	{
		getGridLayout().setFullWidth();
	}

	public OneColumnGridLayout getGridLayout()
	{
		return (OneColumnGridLayout)getLayout();
	}
}
