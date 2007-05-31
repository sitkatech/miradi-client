/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.utils;

import javax.swing.ImageIcon;

public class MiradiResourceImageIcon extends ImageIcon
{
	public MiradiResourceImageIcon(String fileName)
	{
		super(MiradiResourceImageIcon.class.getClassLoader().getResource(fileName));
	}
}
