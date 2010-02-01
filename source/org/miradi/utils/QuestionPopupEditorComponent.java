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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.martus.swing.UiLabel;
import org.martus.swing.Utilities;
import org.miradi.dialogfields.ChoiceItemListSelectionEvent;
import org.miradi.dialogfields.ControlPanelRadioButtonEditorComponent;
import org.miradi.dialogfields.FieldSaver;
import org.miradi.dialogs.base.UndecoratedModelessDialogWithClose;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTextField;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.PopupEditorIcon;
import org.miradi.layout.OneColumnPanel;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public class QuestionPopupEditorComponent extends OneRowPanel
{
	public QuestionPopupEditorComponent(ChoiceQuestion questionToUse, String translatedPopupButtonTextToUse) throws Exception
	{
		question = questionToUse;
		translatedPopupButtonText = translatedPopupButtonTextToUse;
		
		PanelTitleLabel staticLabel = new PanelTitleLabel(translatedPopupButtonText);
		currentSelectionText = new PanelTextField(10);
		currentSelectionText.setEditable(false);
		popupInvokeButton = new PanelButton(new PopupEditorIcon());
		
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
		if (editorPanel != null)
		{
			editorPanel.removeListSelectionListener(closeDialogAfterSelectionHandler);
			editorPanel.dispose();
			editorPanel = null;
		}
	}

	private void addPopupEditorHandler(Component mouseListener)
	{
		mouseListener.addMouseListener(new PopUpEditorHandler());
	}
	
	public void setText(String code)
	{
		ChoiceItem choice = question.findChoiceByCode(code);
		currentSelectionText.setText(choice.getLabel());
		currentSelectionText.setBackground(choice.getColor());
	}
	
	public String getText()
	{
		String currentLabel = currentSelectionText.getText();
		return getQuestion().findChoiceByLabel(currentLabel).getCode();
	}
	
	protected void addAdditionalDescriptionPanel(OneColumnPanel panel)
	{
	}
	
	protected ControlPanelRadioButtonEditorComponent createPopupEditorPanel()
	{
		return new ControlPanelRadioButtonEditorComponent(getQuestion());
	}
	
	private void invokePopupEditor()
	{
		editorPanel = createPopupEditorPanel();
		editorPanel.addListSelectionListener(closeDialogAfterSelectionHandler);
		selectRating();
		editorDialog = new UndecoratedModelessDialogWithClose(EAM.getMainWindow(), EAM.text("Select"));
		editorDialog.enableCloseWhenFocusLost();
		OneColumnPanel panel = new OneColumnPanel();
		addAdditionalDescriptionPanel(panel);
		editorDialog.add(panel, BorderLayout.BEFORE_FIRST_LINE);
		editorDialog.setMainPanel(editorPanel);
		editorDialog.pack();
		//NOTE: packing twice due to preferences height not being set correctly.
		editorDialog.pack();
		Utilities.centerFrame(editorDialog);
		editorDialog.setVisible(true);	
	}
	
	private void selectRating()
	{
		editorPanel.setText(getText());
	}
	
	protected ChoiceQuestion getQuestion()
	{
		return question;
	}
	
	protected String getTranslatedPopupButtonText()
	{
		return translatedPopupButtonText;
	}

	private class PopUpEditorHandler extends MouseAdapter implements ActionListener 
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			invokePopupEditor();
		}
		
		public void actionPerformed(ActionEvent event)
		{
			invokePopupEditor();	
		}
	}
	
	private class CloseEditorAfterSelectionHandler implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent event)
		{
			String code = ((ChoiceItemListSelectionEvent)event).getCode();
			setText(code);
	
			FieldSaver.savePendingEdits();
			editorDialog.setVisible(false);
			editorDialog.dispose();
		}
	}
	
	private PanelButton popupInvokeButton;
	private PanelTextField currentSelectionText;
	private UndecoratedModelessDialogWithClose editorDialog;
	private ChoiceQuestion question;
	private String translatedPopupButtonText;
	private ControlPanelRadioButtonEditorComponent editorPanel;
	private CloseEditorAfterSelectionHandler closeDialogAfterSelectionHandler;
}
