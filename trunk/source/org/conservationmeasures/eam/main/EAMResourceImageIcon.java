/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import javax.swing.ImageIcon;

public class EAMResourceImageIcon extends ImageIcon
{
	public EAMResourceImageIcon(String fileName)
	{
		super(EAM.loadResourceImageFile(fileName));
	}
}
