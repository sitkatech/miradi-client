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

import java.awt.Font;

import javax.swing.JLabel;

import org.miradi.layout.OneRowPanel;
import org.miradi.utils.MiradiResourceImageIcon;

public class MiradiLogoPanel extends OneRowPanel
{
	public MiradiLogoPanel()
	{
		setBackground(AppPreferences.getWizardTitleBackground());
		String tagline = EAM.text("Adaptive Management Software for Conservation Projects");
		MiradiResourceImageIcon logoIcon = new MiradiResourceImageIcon("images/MiradiLogoName.png");
		JLabel headerComponent = new JLabel(tagline, logoIcon, JLabel.TRAILING);
		Font font = new Font("sansserif", Font.BOLD, 16);
		headerComponent.setFont(font);
		add(headerComponent);

	}
}
