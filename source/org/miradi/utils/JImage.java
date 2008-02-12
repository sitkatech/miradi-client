/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
/**
 * 
 */
package org.miradi.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class JImage extends JComponent
{
	public JImage(BufferedImage imageToUse)
	{
		image = imageToUse;
	}
	public void paint(Graphics g) 
	{
		g.drawImage(image, 0, 0, null);
	}
	BufferedImage image;
}