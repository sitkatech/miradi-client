/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WizardPanel extends JPanel
{
	public WizardPanel()
	{
		super(new BorderLayout());
	}

	protected JComponent createHotButtons()
	{
		JComponent hotButtons = Box.createVerticalBox();
		hotButtons.add(new ProcessButton());
		hotButtons.add(new JLabel(" "));
		hotButtons.add(new ExamplesButton());
		hotButtons.add(new JLabel(" "));
		hotButtons.add(new WorkshopButton());
		hotButtons.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
		return hotButtons;
	}

	public void setContents(JPanel contents)
	{
		removeAll();

		JComponent hotButtons = createHotButtons();

		add(contents, BorderLayout.CENTER);
		add(hotButtons, BorderLayout.AFTER_LINE_ENDS);
		allowSplitterToHideUsCompletely();
		validate();
	}

	private void allowSplitterToHideUsCompletely()
	{
		setMinimumSize(new Dimension(0, 0));
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
		super("");
		setText("<html><table cellpadding='0' cellspacing='0'><tr><td align='center' valign='center'>" + 
				text + 
				"</td></tr></table></html>");
		setBackground(color);
	}
}

class ProcessButton extends HotButton
{
	public ProcessButton()
	{
		super("Get More<br></br>Information", Color.GREEN);
	}

	public void actionPerformed(ActionEvent arg0)
	{
	}
}

class ExamplesButton extends HotButton
{
	public ExamplesButton()
	{
		super("Examples", Color.YELLOW);
	}

	public void actionPerformed(ActionEvent arg0)
	{
	}
}

class WorkshopButton extends HotButton
{
	public WorkshopButton()
	{
		super("Workshop<br></br>Hints", Color.CYAN);
	}

	public void actionPerformed(ActionEvent arg0)
	{
	}
}

