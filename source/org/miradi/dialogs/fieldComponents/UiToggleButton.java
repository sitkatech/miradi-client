/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.fieldComponents;

import java.awt.Dimension;

import javax.swing.JToggleButton;

import org.martus.swing.Utilities;
import org.miradi.actions.MiradiAction;

public class UiToggleButton extends JToggleButton
{
	public UiToggleButton(MiradiAction action)
	{
		super(action);
	}

	/* 
	 * This is copied from UiButton:
	 * This is copied from the horrible hack in JTextArea.
	 * I'm not sure if there is a better way, but somehow we 
	 * need to prevent Arabic letters from being chopped off 
	 * at the top and bottom.
	 * 
	 * Unfortunately, since my system didn't show the problem,
	 * I'm not sure this actually helped. Still, it seems like 
	 * a good idea to have a UiButton class, so it's not all 
	 * wasted, even if this method disappears. kbs.
	 */
	@Override
	public Dimension getPreferredSize()
	{
		return Utilities.addCushionToHeightIfRequired(super.getPreferredSize(), EXTRA_PIXELS);
	}

	/* 
	 * This is copied from UiButton:
	 * Another horible hack to deal with "tall" letters
	 * getMaximumSize() instead of getPreferredSize()
	 * is being called from Box's horizontal and vertical.
	 */
	@Override
	public Dimension getMaximumSize()
	{
		return Utilities.addCushionToHeightIfRequired(super.getMaximumSize(), EXTRA_PIXELS);
	}

	final int EXTRA_PIXELS = 14;
}
