/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.fieldComponents;

import javax.swing.Icon;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.UiLabel;

public class PanelTitleLabel extends UiLabel
{
	public PanelTitleLabel()
	{
		super();
	}
	
	public PanelTitleLabel(String text)
	{
		super(text);
		setFont(getMainWindow().getUserDataPanelFont());
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
	
	public MainWindow getMainWindow()
	{
		return EAM.mainWindow;
	}
}