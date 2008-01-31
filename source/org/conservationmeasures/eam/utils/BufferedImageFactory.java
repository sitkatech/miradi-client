/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;


public  class BufferedImageFactory
{
	public static BufferedImage getImage(JComponent swingComponent,  int inset) 
	{
		Rectangle2D bounds = swingComponent.getBounds();
		if (bounds == null) 
			return null;
		
		toScreen(bounds);
		int width = (int) bounds.getWidth() + 2 * inset;
		int height = (int) bounds.getHeight() + 2 * inset;
		
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
		graphics.translate((int) (-bounds.getX() + inset), 
				(int) (-bounds.getY() + inset));
		swingComponent.print(graphics);
		graphics.dispose();
		return image;

	}
	
	private static void toScreen(Rectangle2D rect) 
	{
		rect.setFrame(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	public static BufferedImage createImageFromComponent(JComponent component)
	{
		JFrame frame = new JFrame();
		frame.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		frame.add(component);
		frame.pack();
		BufferedImage image = getImage(component,5);
		return image;
	}
	
}
