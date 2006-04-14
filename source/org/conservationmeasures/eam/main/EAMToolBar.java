/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.Actions;

public class EAMToolBar extends JToolBar
{
	public EAMToolBar(Actions actions, Class currentViewActionClass)
	{
		this(actions, currentViewActionClass, new JComponent[0]);
	}
	
	public EAMToolBar(Actions actions, Class currentViewActionClass, JComponent[] customButtons)
	{
		setFloatable(false);
		add(ViewSwitcher.create(actions, currentViewActionClass));
		for(int i = 0; i < customButtons.length; ++i)
			add(customButtons[i]);
		
		add(Box.createHorizontalGlue());

		add(new ProcessButton());
		add(new ExamplesButton());
		add(new WorkshopButton());
	}
	
}


abstract class CommandButton extends JButton implements ActionListener
{
	public CommandButton(String text)
	{
		super(text);
		addActionListener(this);
	}

}

abstract class HotButton extends CommandButton
{
	public HotButton(String text, Color color)
	{
		super(text);
		setBackground(color);
	}
	
	public void actionPerformed(ActionEvent arg0)
	{
		EAM.okDialog("Function Not Available", new String[] {"This function is not yet available"});
	}

	public Dimension getMaximumSize()
	{
		return super.getPreferredSize();
	}

}

class ProcessButton extends HotButton
{
	public ProcessButton()
	{
		super("More Info", Color.GREEN);
	}

}

class ExamplesButton extends HotButton
{
	public ExamplesButton()
	{
		super("Examples", Color.YELLOW);
	}

}

class WorkshopButton extends HotButton
{
	public WorkshopButton()
	{
		super("Workshop", Color.CYAN);
	}

}

