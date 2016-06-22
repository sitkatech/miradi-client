/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.fieldComponents;

import javax.swing.*;
import java.awt.*;

import static org.miradi.main.Miradi.isWindows;

public class QuestionEditorPanelTitleLabel extends PanelTitleLabel
{
	public QuestionEditorPanelTitleLabel()
	{
		super();
	}

	public QuestionEditorPanelTitleLabel(Icon icon)
	{
		this("", icon);
	}

	private QuestionEditorPanelTitleLabel(String text, Icon icon)
	{
		super(text, icon, JLabel.LEADING);
	}

	public Dimension getPreferredSize()
	{
		return padWidthOnWindows(super.getPreferredSize());
	}

	public Dimension getMaximumSize()
	{
		return padWidthOnWindows(super.getMaximumSize());
	}

	private Dimension padWidthOnWindows(Dimension size)
	{
		if (isWindows())
			size.setSize(size.getWidth() + EXTRA_PIXELS, size.getHeight());
		return size;
	}

	final int EXTRA_PIXELS = 5;
}