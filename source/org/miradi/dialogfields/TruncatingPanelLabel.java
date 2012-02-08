/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogfields;

import java.awt.Container;
import java.awt.Dimension;

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;

/*
 * NOTE: This class is a label that will display ...
 * at the end if it would have been wider than the current 
 * width of the parent container
 */
public class TruncatingPanelLabel extends PanelTitleLabel
{

	@Override
	public Dimension getMaximumSize()
	{
		Dimension superMaximumSize = super.getMaximumSize();

		Container parent = getParent();
		if(parent != null)
		{
			superMaximumSize.width = Math.min(superMaximumSize.width, parent.getWidth());
			superMaximumSize.height = Math.min(superMaximumSize.height, parent.getHeight());
		}

		return superMaximumSize;
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		Dimension superPreferredSize = super.getPreferredSize();
		Dimension maxSize = getMaximumSize();
	
		superPreferredSize.width = Math.min(superPreferredSize.width, maxSize.width);
		superPreferredSize.height = Math.min(superPreferredSize.height, maxSize.height);
		
		return superPreferredSize;
	}
}
