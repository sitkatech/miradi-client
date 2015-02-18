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

package org.miradi.dialogs.dashboard;

import java.awt.Color;
import java.util.Vector;

import javax.swing.JComponent;

import org.miradi.dialogs.base.AbstractOpenStandardsQuestionPanel;

public class DashboardSelectableRow extends SelectableRow
{
	public DashboardSelectableRow(Vector<JComponent> selectableComponentsToUse, AbstractLongDescriptionProvider descriptionProviderToUse)
	{
		super(selectableComponentsToUse, descriptionProviderToUse);
	}

	@Override
	protected Color getSelectedForegroundColor()
	{
		return Color.BLACK;
	}

	@Override
	protected Color getSelectedBackgroundColor()
	{
		return AbstractOpenStandardsQuestionPanel.DASHBOARD_BACKGROUND_COLOR;
	}

}
