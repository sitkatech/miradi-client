/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
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
