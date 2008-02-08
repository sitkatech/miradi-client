/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.layout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class OneRowPanel extends JPanel
{
	public OneRowPanel()
	{
		super(new OneRowGridLayout());
	}
	
	public void setMargins(int margin)
	{
		getGridLayout().setMargins(margin);
	}
	
	public void setAlignmentRight()
	{
		add(new JLabel(" "), 0);
		getGridLayout().setColWeight(0, 1);
	}

	public OneRowGridLayout getGridLayout()
	{
		return (OneRowGridLayout)getLayout();
	}
}
