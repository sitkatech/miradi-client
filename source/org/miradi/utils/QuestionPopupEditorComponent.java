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

package org.miradi.utils;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.martus.swing.UiLabel;
import org.martus.swing.Utilities;
import org.miradi.dialogfields.ControlPanelRadioButtonEditorComponent;
import org.miradi.dialogs.base.UndecoratedModelessDialogWithClose;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTextField;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public class QuestionPopupEditorComponent extends OneRowPanel
{
	public QuestionPopupEditorComponent(ListSelectionListener selectionHandlerToUse, ChoiceQuestion questionToUse, String translatedPopupButtonText) throws Exception
	{
		selectionHandler = selectionHandlerToUse;
		question = questionToUse;
		
		createEditorPanel();
		PanelTitleLabel staticLabel = new PanelTitleLabel(translatedPopupButtonText);
		currentSelectionText = new PanelTextField(10);
		currentSelectionText.setEditable(false);
		popupInvokeButton = new PanelButton("...");
		
		OneRowPanel panel = new OneRowPanel();
		panel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		panel.setMargins(2);
		panel.setGaps(2);
		panel.add(staticLabel);
		panel.add(new UiLabel(" "));
		panel.add(currentSelectionText);
		panel.add(popupInvokeButton);
		
		add(panel);
		
		addPopupEditorHandler(staticLabel);
		addPopupEditorHandler(currentSelectionText);
		addPopupEditorHandler(panel);
		popupInvokeButton.addActionListener(new PopUpEditorHandler());
		closeDialogAfterSelectionHandler = new CloseEditorAfterSelectionHandler();
	}
	
	public void dispose()
	{
		editorPanel.removeListSelectionListener(closeDialogAfterSelectionHandler);
		editorPanel.dispose();
		editorPanel = null;
	}

	private void createEditorPanel()
	{
		editorPanel = new ControlPanelRadioButtonEditorComponent(getQuestion(), selectionHandler);
		editorPanel.addListSelectionListener(closeDialogAfterSelectionHandler);
	}
	
	private void addPopupEditorHandler(Component mouseListener)
	{
		mouseListener.addMouseListener(new PopUpEditorHandler());
	}
	
	public void setText(String text)
	{
		editorPanel.setText(text);
		ChoiceItem choice = question.findChoiceByCode(text);
		currentSelectionText.setText(choice.getLabel());
		currentSelectionText.setBackground(choice.getColor());
	}
	
	public String getText()
	{
		return editorPanel.getText();
	}

	private class PopUpEditorHandler extends MouseAdapter implements ActionListener 
	{
		public PopUpEditorHandler()
		{
			selectRating();
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			invokePopupEditor();
		}
		
		public void actionPerformed(ActionEvent event)
		{
			invokePopupEditor();	
		}

		private void invokePopupEditor()
		{
			createEditorPanel();
			editorDialog = new UndecoratedModelessDialogWithClose(EAM.getMainWindow(), EAM.text("Select"));
			editorDialog.enableCloseWhenFocusLost();
			editorDialog.setMainPanel(editorPanel);
			editorDialog.pack();
			//NOTE: packing twice due to preferences height not being set correctly.
			editorDialog.pack();
			Utilities.centerFrame(editorDialog);
			editorDialog.setVisible(true);	
		}
		
		private void selectRating()
		{
			try
			{
				CodeList codeList = new CodeList();
				codeList.add(getText());
				String codeListAsString = codeList.toString();
				editorPanel.setText(codeListAsString);
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}
	}
	
	private class CloseEditorAfterSelectionHandler implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent event)
		{
			editorDialog.setVisible(false);
			editorDialog.dispose();
		}
	}
	
	private ChoiceQuestion getQuestion()
	{
		return question;
	}

	private PanelButton popupInvokeButton;
	private PanelTextField currentSelectionText;
	private UndecoratedModelessDialogWithClose editorDialog;
	private ChoiceQuestion question;
	private ControlPanelRadioButtonEditorComponent editorPanel;
	private ListSelectionListener selectionHandler;
	private CloseEditorAfterSelectionHandler closeDialogAfterSelectionHandler; 
}
