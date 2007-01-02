/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionHelpButtonExamples;
import org.conservationmeasures.eam.actions.ActionHelpButtonMoreInfo;
import org.conservationmeasures.eam.actions.ActionHelpButtonWorkshop;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.utils.ToolBarButton;
import org.conservationmeasures.eam.views.umbrella.HelpButtonData;
import org.martus.swing.UiButton;

public class EAMToolBar extends JToolBar
{
	public EAMToolBar(Actions actions, Class currentViewActionClass)
	{
		this(actions, currentViewActionClass, new JComponent[0][0]);
	}
	
	public EAMToolBar(Actions actions, Class currentViewActionClass, JComponent[][] customButtons)
	{
		setFloatable(false);
		add(ViewSwitcher.create(actions, currentViewActionClass));
		addSeparator();
		add(new ToolBarButton(actions, ActionUndo.class));
		add(new ToolBarButton(actions, ActionRedo.class));

		for(int segment = 0; segment < customButtons.length; ++segment)
		{
			addSeparator();
			for(int button = 0; button < customButtons[segment].length; ++button)
				add(customButtons[segment][button]);
		}
		
		add(Box.createHorizontalGlue());

		add(new MoreInfoButton(actions.get(ActionHelpButtonMoreInfo.class)));
		add(new ExamplesButton(actions.get(ActionHelpButtonExamples.class)));
		add(new WorkshopButton(actions.get(ActionHelpButtonWorkshop.class)));
	}
	
}

abstract class HelpButton extends UiButton
{
	public HelpButton(Action action, Color color)
	{
		super(action);
		setBackground(color);
		setFocusable(false);
	}

	public Dimension getMaximumSize()
	{
		return super.getPreferredSize();
	}
}

class MoreInfoButton extends HelpButton
{
	public MoreInfoButton(Action action)
	{
		super(action, Color.GREEN);
		putClientProperty(HelpButtonData.class, new HelpButtonData(HelpButtonData.MORE_INFO, HelpButtonData.MORE_INFO_HTML));
	}
}

class ExamplesButton extends HelpButton
{
	public ExamplesButton(Action action)
	{
		super(action, Color.YELLOW);
		putClientProperty(HelpButtonData.class, new HelpButtonData(HelpButtonData.EXAMPLES, HelpButtonData.EXAMPLES_HTML));
	}
}

class WorkshopButton extends HelpButton
{
	public WorkshopButton(Action action)
	{
		super(action, Color.CYAN);
		putClientProperty(HelpButtonData.class, new HelpButtonData(HelpButtonData.WORKSHOP, HelpButtonData.WORKSHOP_HTML));
	}
}

