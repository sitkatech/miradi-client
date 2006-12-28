/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;

import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.HyperlinkHandler;
import org.martus.swing.UiLabel;

public class HyperlinkLabel extends UiLabel implements MouseListener
{
	
	public HyperlinkLabel(String textToShow, String textToDisplay)
	{
		this(textToShow);
		displayText[0] = textToDisplay;
	}
	
	public HyperlinkLabel(String textToShow, String urlToReturn, HyperlinkHandler handlerToUse)
	{
		this(textToShow);
		url = urlToReturn;
		handler = handlerToUse;
	}
	
	private HyperlinkLabel(String textToShow)
	{
		super("<html><u>" + textToShow + "</u></html>");
		text = textToShow;
		setBackground(Color.WHITE);
		setForeground(Color.BLUE);
		setOpaque(true);
		addMouseListener(this);
	}

	public void mouseClicked(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			if (handler == null)
				EAM.okDialog(EAM.text("Hyperlink Label"), displayText);
			else
				handler.linkClicked(url);
		}
	}

	public void mouseEntered(MouseEvent e)
	{
		setBackground(Color.LIGHT_GRAY);
		invalidate();
	}

	public void mouseExited(MouseEvent e)
	{
		setBackground(Color.WHITE);
		invalidate();
	}

	public void mousePressed(MouseEvent e)
	{
		if(e.isPopupTrigger())
			fireRightClick(e);
	}

	public void mouseReleased(MouseEvent e)
	{
		if(e.isPopupTrigger())
			fireRightClick(e);
	}
	
	void fireRightClick(MouseEvent e)
	{
		if (handler != null)
		{
			JPopupMenu menu = handler.getRightClickMenu(text);
			menu.show(this, e.getX(), e.getY());
		}
	}
	
	String url;
	String text;
	String[] displayText = new String[1];
	HyperlinkHandler handler;
}
