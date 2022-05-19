/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.actions.*;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.RemovedFeatureLabel;
import org.miradi.utils.ToolBarButton;

import javax.swing.*;
import java.awt.*;

public class MiradiProjectToolBar extends MiradiToolBar
{
	public MiradiProjectToolBar(Actions actions, Class currentViewActionClass)
	{
		this(actions, currentViewActionClass, new JComponent[0][0]);
	}
	
	public MiradiProjectToolBar(Actions actions, Class currentViewActionClass, JComponent[][] customButtons)
	{
		setFloatable(false);
		add(ViewSwitcher.create(actions, currentViewActionClass));
		addSeparator();
		add(new ToolBarButton(actions, ActionUndo.class));
		add(new ToolBarButton(actions, ActionRedo.class));
		toggleSpellCheckButton = new ToolBarButton(actions, ActionToggleSpellChecker.class);
		add(toggleSpellCheckButton);

		for(int segment = 0; segment < customButtons.length; ++segment)
		{
			addSeparator();
			for(int button = 0; button < customButtons[segment].length; ++button)
				add(customButtons[segment][button]);
		}

		addSeparator();
		removedFeatureLabel = new RemovedFeatureLabel();
		removedFeatureLabel.updateVisibility();
		add(removedFeatureLabel);

		add(Box.createHorizontalGlue());

		add(new MiradiShareButton(actions.get(ActionLinkToMiradiShare.class)));

		updateButtonStates();
	}

	@Override
	public void updateButtonStates()
	{
		super.updateButtonStates();
		boolean isSpellCheckerActive = getMainWindow().isSpellCheckerActive();
		toggleSpellCheckButton.setSelected(isSpellCheckerActive);

		removedFeatureLabel.updateVisibility();
	}
	
	private MainWindow getMainWindow()
	{
		// TODO: Shouldn't use static here
		return EAM.getMainWindow();
	}

	private ToolBarButton toggleSpellCheckButton;
	private RemovedFeatureLabel removedFeatureLabel;
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

	@Override
	public Dimension getMaximumSize()
	{
		return super.getPreferredSize();
	}
}

class MiradiShareButton extends HelpButton
{
	public MiradiShareButton(Action action)
	{
		super(action, AppPreferences.getWizardBackgroundColor());
	}
}

