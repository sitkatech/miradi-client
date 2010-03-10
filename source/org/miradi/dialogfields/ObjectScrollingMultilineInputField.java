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
package org.miradi.dialogfields;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.icons.PopupEditorIcon;
import org.miradi.ids.BaseId;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.MiradiScrollPane;

public class ObjectScrollingMultilineInputField extends ObjectMultilineInputField
{
	public ObjectScrollingMultilineInputField(MainWindow mainWindow, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, int columnsToUse)
	{
		super(mainWindow, objectTypeToUse, objectIdToUse, tagToUse, INITIAL_MULTI_LINE_TEXT_AREA_ROW_COUNT, columnsToUse);
	}

	private void createTextFieldWithPopupButtonPanel()
	{
		editButton = new PanelButton(new PopupEditorIcon());
		editButton.addActionListener(new PopupButtonHandler());

		OneColumnPanel buttonPanel = new OneColumnPanel();
		buttonPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		buttonPanel.add(editButton);

		scrollPane = new MiradiScrollPane(getTextField());
		scrollPane.setVerticalScrollBarPolicy(scrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		mainPanel = new MiradiPanel(new BorderLayout());
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.AFTER_LINE_ENDS);
		setDefaultFieldBorder();
	}

	@Override
	public JComponent getComponent()
	{
		if (mainPanel == null)
			createTextFieldWithPopupButtonPanel();
		
		return mainPanel;
	}

	@Override
	protected void addFocusListener()
	{
		super.addFocusListener();
		getTextField().addFocusListener(this);
	}

	private class PopupButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			ModalDialogWithClose dialog = new ModalDialogWithClose(getMainWindow(), EAM.text("Edit..."));
			dialog.addWindowListener(new WindowCloseSaveHandler());
			popupTextField = new MiradiTextPane(getMainWindow(), 100, 20);
			new TextAreaRightClickMouseHandler(getMainWindow().getActions(), popupTextField);
			popupTextField.setText(getTextField().getText());
			dialog.add(new MiradiScrollPane(popupTextField));
			Utilities.centerDlg(dialog);
			dialog.setVisible(true);
		}
	}
	
	private class WindowCloseSaveHandler extends WindowAdapter
	{
		@Override
		public void windowDeactivated(WindowEvent e)
		{
			setText(popupTextField.getText());
			saveIfNeeded();
		}
	}

	private MiradiTextPane popupTextField; 
	private PanelButton editButton;
	private MiradiPanel mainPanel;
	private MiradiScrollPane scrollPane;
	public static final int INITIAL_MULTI_LINE_TEXT_AREA_ROW_COUNT = 4;
}
