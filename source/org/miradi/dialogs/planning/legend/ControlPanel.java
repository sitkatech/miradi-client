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
package org.miradi.dialogs.planning.legend;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.LineBorder;

import org.martus.swing.UiLabel;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;

import com.jhlabs.awt.BasicGridLayout;

public class ControlPanel extends DisposablePanel
{
	public ControlPanel()
	{
		super(new BasicGridLayout(2, 1));
		setBackground(AppPreferences.getControlPanelBackgroundColor());

		add(createTitleBar(EAM.text("Control Bar")));
	}

	public UiLabel createTitleBar(String text)
	{
		UiLabel title = new PanelTitleLabel(text);
		title.setBorder(new LineBorder(Color.BLACK, 2));
		title.setHorizontalAlignment(UiLabel.CENTER);
		title.setBackground(getBackground());
		title.setMinimumSize(new Dimension(0,0));
		
		return title;
	}
	
}
