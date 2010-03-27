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

package org.miradi.utils;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextArea;

import org.martus.swing.Utilities;
import org.miradi.dialogfields.PopupTextEditorDialog;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.tablerenderers.MultiLineEditableObjectTableCellEditorOrRendererFactory;
import org.miradi.icons.PopupEditorIcon;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

/**
 * FIXME: Eliminate code duplication with QuestionPopupEditorComponent
 */
public class TextFieldPopupEditorComponent extends PopupEditorComponent
{
	public TextFieldPopupEditorComponent()
	{
		setBackground(AppPreferences.getDataPanelBackgroundColor());
		
		createComponents();
		add(currentSelectionText, BorderLayout.CENTER);
		add(popupInvokeButton, BorderLayout.AFTER_LINE_ENDS);
		addListeners();
	}
	
	public void setStopEditingListener(MultiLineEditableObjectTableCellEditorOrRendererFactory listener)
	{
		stopEditingListener = listener;
	}

	protected void addListeners()
	{
		PopUpEditorHandler popupEditHandler = new PopUpEditorHandler();
		popupInvokeButton.addActionListener(popupEditHandler);
	}
	
	private void createComponents()
	{
		currentSelectionText = new JTextArea();
		currentSelectionText.setEditable(false);
		currentSelectionText.setLineWrap(true);
		currentSelectionText.setWrapStyleWord(true);

		popupInvokeButton = new PanelButton(new PopupEditorIcon());
	}
	
	public void dispose()
	{
	}

	public void setText(String text)
	{
		currentSelectionText.setText(text);
	}
	
	public String getText()
	{
		String currentLabel = currentSelectionText.getText();
		return currentLabel;
	}
	
	protected void invokePopupEditor()
	{
		MainWindow mainWindow = EAM.getMainWindow();
		String title = EAM.text("Edit Text");
		String initialText = getText();
		PopupTextEditorDialog dialog = new PopupTextEditorDialog(mainWindow, title, initialText);
		Utilities.centerFrame(dialog);
		dialog.setVisible(true);
		setText(dialog.getText());
		stopEditingListener.editingWasStoppedByComponent();
	}
	
	private class PopUpEditorHandler extends MouseAdapter implements ActionListener 
	{
		@Override
		public void mouseReleased(MouseEvent e)
		{
			invokePopupEditor();
		}
		
		public void actionPerformed(ActionEvent event)
		{
			invokePopupEditor();	
		}
	}
	
	private PanelButton popupInvokeButton;
	private JTextArea currentSelectionText;
	private MultiLineEditableObjectTableCellEditorOrRendererFactory stopEditingListener;
}
