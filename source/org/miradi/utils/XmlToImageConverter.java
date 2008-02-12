/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import java.awt.Image;

import javax.swing.ImageIcon;


public class XmlToImageConverter
{
	public static Image convert(String filePath)
	{
		return new ImageIcon(filePath).getImage();
	}
}
