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

package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.Dimension;

import javax.swing.Icon;

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.IconManager;

public class AbstractFixedHeightDirectlyAboveTreeTablePanel extends PanelTitleLabel
{
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(0, getIconHeight() + ARBITRARY_MARGIN);
	}

	protected int getIconHeight()
	{
		return Math.max(getExpandIcon().getIconHeight(), getCollapseIcon().getIconHeight());
	}

	protected Icon getExpandIcon()
	{
		return IconManager.getExpandIcon();
	}

	protected Icon getCollapseIcon()
	{
		return IconManager.getCollapseIcon();
	}

	protected static final int ARBITRARY_MARGIN = 2;
}
