/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.Image;

import org.martus.swing.ResourceImageIcon;
import org.w3c.dom.Document;

public class XmlToImageConverter
{
	public static Image convert(Document xml)
	{
		return new ResourceImageIcon("/images/appIcon.png").getImage();
	}
}
