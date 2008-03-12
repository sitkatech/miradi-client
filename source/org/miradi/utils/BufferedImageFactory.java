/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public  class BufferedImageFactory
{
	public static BufferedImage getImage(JComponent swingComponent,  int inset) 
	{
		Rectangle2D bounds = new Rectangle(swingComponent.getBounds());
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
	
	public static BufferedImage createImageFromTable(JTable table)
	{
		JScrollPane scrollerToShowHeaders = new JScrollPane(table);
		scrollerToShowHeaders.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollerToShowHeaders.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollerToShowHeaders.getViewport().setPreferredSize(table.getPreferredSize());
		return createImageFromComponent(scrollerToShowHeaders);
	}

	public static BufferedImage createImageFromComponent(JComponent component)
	{
		// NOTE: When we add this component to our temporary frame, it 
		// it automatically removed from its original parent. We need to 
		// put it back where we found it when we are finished
		Container parent = component.getParent();
		int oldPosition = findComponentInParent(component);
		try
		{
			JFrame frame = new JFrame();
			frame.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
			frame.add(component);
			frame.pack();
			BufferedImage image = getImage(component,5);
			return image;
		}
		finally
		{
			if(parent != null)
			{
				parent.add(component, oldPosition);
			}
		}
	}

	private static int findComponentInParent(JComponent component)
	{
		Container parent = component.getParent();
		if(parent == null)
			return -1;
		
		for(int i = 0; i < parent.getComponentCount(); ++i)
		{
			if(parent.getComponent(i) == component)
				return i;
		}
		
		return -1;
	}
	
}
