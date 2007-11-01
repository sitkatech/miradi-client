/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.layout;

import com.jhlabs.awt.Alignment;
import com.jhlabs.awt.GridLayoutPlus;

public class OneColumnGridLayout extends GridLayoutPlus
{
	public OneColumnGridLayout()
	{
		super(0, 1, 0, 0, 0, 0);
		setFill(Alignment.FILL_NONE);
		setAlignment(Alignment.NORTHWEST);
	}
}
