/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
		String tagline = EAM.text("Adaptive Management for Conservation Projects");
		MiradiResourceImageIcon logoIcon = new MiradiResourceImageIcon("images/MiradiLogoName.png");
		JLabel headerComponent = new JLabel(tagline, logoIcon, JLabel.TRAILING);
		Font font = new Font("sansserif", Font.BOLD, 16);
		headerComponent.setFont(font);
		add(headerComponent);

	}
}
