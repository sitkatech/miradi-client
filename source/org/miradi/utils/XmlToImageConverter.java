/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
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
