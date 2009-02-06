/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JToolBar;

import org.miradi.actions.ActionHelpButtonExamples;
import org.miradi.actions.ActionHelpButtonMoreInfo;
import org.miradi.actions.ActionHelpButtonWorkshop;
import org.miradi.actions.ActionRedo;
import org.miradi.actions.ActionUndo;
import org.miradi.actions.Actions;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.utils.ToolBarButton;
import org.miradi.views.umbrella.HelpButtonData;
import org.miradi.views.umbrella.ViewSpecificHelpButtonData;

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

abstract class HelpButton extends PanelButton
{
	public HelpButton(Action action, Color color)
	{
		super(action);
		if(color != null)
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
		super(action, Color.decode("#99CCFF"));
		putClientProperty(HelpButtonData.class, new ViewSpecificHelpButtonData(getMainWindow(), HelpButtonData.MORE_INFO, HelpButtonData.MORE_INFO_HTML));
	}
}

class ExamplesButton extends HelpButton
{
	public ExamplesButton(Action action)
	{
		super(action, Color.decode("#FFFF77"));
		putClientProperty(HelpButtonData.class, new ViewSpecificHelpButtonData(getMainWindow(), HelpButtonData.EXAMPLES, HelpButtonData.EXAMPLES_HTML));
	}
}

class WorkshopButton extends HelpButton
{
	public WorkshopButton(Action action)
	{
		super(action, Color.decode("#77FF77"));
		putClientProperty(HelpButtonData.class, new ViewSpecificHelpButtonData(getMainWindow(), HelpButtonData.WORKSHOP, HelpButtonData.WORKSHOP_HTML));
	}
}

