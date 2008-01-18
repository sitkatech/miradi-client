/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.layout;

import com.jhlabs.awt.Alignment;
import com.jhlabs.awt.GridLayoutPlus;

public class OneRowGridLayout extends GridLayoutPlus
{
	public OneRowGridLayout()
	{
		super(1, 0, 0, 0, 0, 0);
		setFill(Alignment.FILL_NONE);
		setAlignment(Alignment.NORTH);
	}
}
