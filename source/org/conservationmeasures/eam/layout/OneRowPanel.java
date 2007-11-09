/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.layout;

import javax.swing.JPanel;

public class OneRowPanel extends JPanel
{
	public OneRowPanel()
	{
		super(new OneRowGridLayout());
	}
}
