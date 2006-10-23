/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;


public  class BufferedImageFactory
{

	public static BufferedImage getImage(JComponent swingComponent, Color bg, int inset) {
		Rectangle2D bounds = swingComponent.getBounds();
		if (bounds != null) {
			toScreen(bounds);
			BufferedImage img = new BufferedImage((int) bounds.getWidth() + 2
					* inset, (int) bounds.getHeight() + 2 * inset,
					(bg != null) ? BufferedImage.TYPE_INT_RGB
							: BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics = img.createGraphics();
			if (bg != null) {
				graphics.setColor(bg);
				graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
			} else {
				graphics.setComposite(AlphaComposite.getInstance(
						AlphaComposite.CLEAR, 0.0f));
				graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
				graphics.setComposite(AlphaComposite.SrcOver);
			}
			graphics.translate((int) (-bounds.getX() + inset), (int) (-bounds
					.getY() + inset));
			swingComponent.print(graphics);
			graphics.dispose();
			return img;
		}
		return null;
	}
	
	private static Rectangle2D toScreen(Rectangle2D rect) {
		if (rect == null)
			return null;
		rect.setFrame(rect.getX() * 1.0, rect.getY() * 1.0, rect.getWidth()
				* 1.0, rect.getHeight() * 1.0);
		return rect;
	}
	
}
