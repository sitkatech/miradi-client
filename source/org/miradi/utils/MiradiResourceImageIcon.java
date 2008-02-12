/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.miradi.utils;

import javax.swing.ImageIcon;

public class MiradiResourceImageIcon extends ImageIcon
{
	public MiradiResourceImageIcon(String fileName)
	{
		super(MiradiResourceImageIcon.class.getClassLoader().getResource(fileName));
	}
}
