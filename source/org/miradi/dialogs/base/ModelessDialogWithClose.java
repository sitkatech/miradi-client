/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.base; 

import org.miradi.main.MainWindow;

public class ModelessDialogWithClose extends AbstractDialogWithClose
{
	public ModelessDialogWithClose(MainWindow parent, DisposablePanel panel, String headingText)
	{
		super(parent, panel, headingText);
		setModal(false);
	}
	
	@Override
	protected Class getJumpAction()
	{
		return getMainPanel().getJumpActionClass();
	}
	
	protected DisposablePanel getMainPanel()
	{
		return (DisposablePanel)getWrappedPanel();
	}
}
