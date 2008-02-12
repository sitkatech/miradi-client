/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.fieldComponents;

import javax.swing.Icon;

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
		setBackground(AppPreferences.getControlPanelBackgroundColor());
	}
	
	//TODO: Richard: should not use static ref here
	public MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}
}