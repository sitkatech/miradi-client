/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogfields;

import org.miradi.dialogfields.ObjectMultilineInputField.MiradiTextPane;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.main.MainWindow;
import org.miradi.utils.MiradiScrollPane;

class PopupTextEditorDialog extends ModalDialogWithClose
{
	public PopupTextEditorDialog(MainWindow mainWindow, String title, String initialText)
	{
		super(mainWindow, title);
		final int COLUMN_COUNT = 60;
		final int ROW_COUNT = 10;
		popupTextField = new MiradiTextPane(getMainWindow(), COLUMN_COUNT, ROW_COUNT);
		new TextAreaRightClickMouseHandler(getMainWindow().getActions(), popupTextField);
		popupTextField.setText(initialText);
		add(new MiradiScrollPane(popupTextField));
	}
	
	public String getText()
	{
		return popupTextField.getText();
	}

	private MiradiTextPane popupTextField; 
}