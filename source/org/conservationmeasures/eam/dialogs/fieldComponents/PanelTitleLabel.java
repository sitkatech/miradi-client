/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.fieldComponents;

import javax.swing.Icon;

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.UiLabel;

public class PanelTitleLabel extends UiLabel
{
	public PanelTitleLabel()
	{
		super();
		initialize();
	}
	
	public PanelTitleLabel(String text)
	{
		super(text);
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
		setBackground(AppPreferences.CONTROL_PANEL_BACKGROUND);
	}
	
	//TODO: Richard: should not use static ref here
	public MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}
}