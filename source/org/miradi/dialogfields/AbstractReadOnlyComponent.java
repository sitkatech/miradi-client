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

import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.main.EAM;

import com.jhlabs.awt.BasicGridLayout;

abstract public class AbstractReadOnlyComponent extends MiradiPanel
{
	public AbstractReadOnlyComponent()
	{
		this(SINGLE_COULMN_COUNT);
	}

	public AbstractReadOnlyComponent(int columnCount)
	{
		setLayout(new BasicGridLayout(0, columnCount));
		setBackground(EAM.READONLY_BACKGROUND_COLOR);
		setForeground(EAM.READONLY_FOREGROUND_COLOR);
	}
	
	abstract public String getText();
	
	abstract public void setText(String text);


	private static final int SINGLE_COULMN_COUNT = 1;
}
