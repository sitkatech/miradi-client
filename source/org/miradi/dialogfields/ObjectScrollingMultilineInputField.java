/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.utils.PopupEditorButton;
import org.miradi.utils.Translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ObjectScrollingMultilineInputField extends ObjectMultilineInputField
{
	public ObjectScrollingMultilineInputField(MainWindow mainWindow, ORef refToUse, String tagToUse, int columnsToUse) throws Exception
	{
		super(mainWindow, refToUse, tagToUse, INITIAL_MULTI_LINE_TEXT_AREA_ROW_COUNT, columnsToUse);
	}

	private void createTextFieldWithPopupButtonPanel()
	{
		editButton = new PopupEditorButton();
		editButton.addActionListener(new PopupButtonHandler());

		OneColumnPanel panelToPreventVerticalStretching = new OneColumnPanel();
		panelToPreventVerticalStretching.setBackground(AppPreferences.getDataPanelBackgroundColor());
		panelToPreventVerticalStretching.add(editButton);

		scrollPane = new MiradiScrollPane(getTextField());
		scrollPane.setVerticalScrollBarPolicy(scrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		mainPanel = new MiradiPanel(new BorderLayout());
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(panelToPreventVerticalStretching, BorderLayout.AFTER_LINE_ENDS);
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
	public void updateEditableState(boolean isEditable)
	{
		editButton.setEnabled(isEditable);
		getTextField().setEnabled(isEditable);
		
		Color fg = EAM.READONLY_FOREGROUND_COLOR;
		Color bg = EAM.READONLY_BACKGROUND_COLOR;
		if(isEditable)
		{
			fg = EAM.EDITABLE_FOREGROUND_COLOR;
			bg = EAM.EDITABLE_BACKGROUND_COLOR;
		}

		getTextField().setForeground(fg);
		if(shouldSetBackground())
			getTextField().setBackground(bg);

		if(!isEditable)
			getTextField().setDisabledTextColor(EAM.READONLY_FOREGROUND_COLOR);
	}

	@Override
	protected void addFocusListener()
	{
		super.addFocusListener();
		getTextField().addFocusListener(this);
	}
	
	@Override
	public void focusLost(FocusEvent e)
	{
		super.focusLost(e);
		if(e.isTemporary())
			return;
		
		// NOTE: Shef returns a value different from what is being shown,
		// so whenever we lose focus, put the display back in sync.
		setText(getText());
	}
	
	protected void setTextFromPopup(String textFromPopupEditor)
	{
		setText(textFromPopupEditor);
	}

	private class PopupButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				String fieldLabel = Translation.fieldLabel(getObjectType(), getTag());
				dialog = new PopupTextEditorDialog(getMainWindow(), fieldLabel, getTextField().getText());
				dialog.addWindowListener(new WindowCloseSaveHandler());
				Utilities.centerDlg(dialog);
				dialog.setVisible(true);
			}
			catch(Exception e)
			{
				EAM.alertUserOfNonFatalException(e);
			}
		}

		private class WindowCloseSaveHandler extends WindowAdapter
		{
			@Override
			public void windowDeactivated(WindowEvent e)
			{
				setTextFromPopup(dialog.getText());
				forceSave();
			}
		}

		private PopupTextEditorDialog dialog;
	}

	private PanelButton editButton;
	private MiradiPanel mainPanel;
	private MiradiScrollPane scrollPane;
	public static final int INITIAL_MULTI_LINE_TEXT_AREA_ROW_COUNT = 4;
}
