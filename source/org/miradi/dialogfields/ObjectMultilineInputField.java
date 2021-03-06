/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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


import javax.swing.text.JTextComponent;

import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.utils.AbstractHtmlPane;
import org.miradi.utils.EditableHtmlPane;
import org.miradi.utils.HtmlEditorRightClickMouseHandler;

public abstract class ObjectMultilineInputField extends ObjectTextInputField
{
	protected ObjectMultilineInputField(MainWindow mainWindowToUse, ORef refToUse, String tagToUse, int initialVisibleRows, int columnsToUse) throws Exception
	{
		this(mainWindowToUse, refToUse, tagToUse, createTextComponent(mainWindowToUse, initialVisibleRows, columnsToUse));
	}

	public ObjectMultilineInputField(MainWindow mainWindowToUse, ORef refToUse, String tagToUse, JTextComponent createTextComponent) throws Exception
	{
		super(mainWindowToUse, refToUse, tagToUse, createTextComponent);
		
		mainWindow = mainWindowToUse;
	}

	private static JTextComponent createTextComponent(MainWindow mainWindow, int initialVisibleRows, int columnsToUse) throws Exception
	{
		return new EditableHtmlPane(mainWindow, columnsToUse, initialVisibleRows);
	}
	
	@Override
	protected void setSaveListener(DocumentEventHandler saveListenerToUse) throws Exception
	{
		super.setSaveListener(saveListenerToUse);
		
		((AbstractHtmlPane)getTextField()).setSaverListener(saveListenerToUse);
	}

	@Override
	protected void createRightClickMouseHandler()
	{
		new HtmlEditorRightClickMouseHandler(getActions(), getTextField());
	}	
	
	protected MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private MainWindow mainWindow;
}
