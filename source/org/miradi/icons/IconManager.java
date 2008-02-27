/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.icons;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class IconManager
{
	public static Image getKeyEcologicalAttributeIcon()
	{
		return convertToImage(new KeyEcologicalAttributeIcon());
	}
	
	public static Image getMeasurementIcon()
	{
		return convertToImage(new MeasurementIcon());
	}
	
	public static Image getGoalIcon()
	{
		return convertToImage(new GoalIcon());
	}
	
	public static Image getIndicatorIcon()
	{
		return convertToImage(new IndicatorIcon());
	}
	
	public static Image getTargetIcon()
	{
		return convertToImage(new TargetIcon());
	}

	private static Image convertToImage(Icon icon)
	{
		final int PADDING = 5;
		BufferedImage img = new BufferedImage(icon.getIconWidth() + PADDING, icon.getIconHeight() + PADDING, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		icon.paintIcon(new JLabel(), g, 2, 2);
		
		return new ImageIcon(img).getImage();
	}
}
