/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.fieldComponents;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class PanelTitledBorder extends TitledBorder
{
	public PanelTitledBorder(String title)
	{
		super(BorderFactory.createLineBorder(Color.BLACK));
		setTitle(title);
		setTitleJustification(LEADING);
		setTitlePosition(TOP);
		Font font = getMainWindow().getUserDataPanelFont();
		font = font.deriveFont(Font.ITALIC);
		setTitleFont(font);
	}
	
	//TODO: Richard: should not use static ref here
	static private MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}
}
