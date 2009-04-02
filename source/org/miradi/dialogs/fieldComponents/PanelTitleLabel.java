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
package org.miradi.dialogs.fieldComponents;

import javax.swing.Icon;
import javax.swing.JLabel;

import org.martus.swing.UiLabel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class PanelTitleLabel extends UiLabel
{
	public PanelTitleLabel()
	{
		super();
		initialize();
	}
	
	public PanelTitleLabel(String text)
	{
		this(text, null);
	}
	
	public PanelTitleLabel(Icon icon)
	{
		this("", icon);
	}
	
	public PanelTitleLabel(String text, Icon icon)
	{
		super(text, icon, JLabel.LEADING);
		setFont(getMainWindow().getUserDataPanelFont());
		initialize();
	}
	
	public PanelTitleLabel(String text, int horizontalAlignment)
	{
		this(text);
		setHorizontalAlignment(horizontalAlignment);
	}
	
	public PanelTitleLabel(String text, Icon icon, int horizontalAlignment)
	{
		this(text,horizontalAlignment);
		setIcon(icon);
	}
	
	private void initialize()
	{
		setBackground(AppPreferences.getControlPanelBackgroundColor());
	}
	
	//TODO should not use static ref here
	public MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}
}