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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.martus.swing.Utilities;
import org.miradi.dialogfields.RadioButtonEditorComponent;
import org.miradi.dialogs.base.UndecoratedModelessDialogWithClose;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.RatingIcon;
import org.miradi.layout.OneRowPanel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public class QuestionPopupEditorComponent extends OneRowPanel
{
	public QuestionPopupEditorComponent(ListSelectionListener selectionHandler, ChoiceQuestion questionToUse, String translatedPopupButtonText) throws Exception
	{
		question = questionToUse;
		editorPanel = new RadioButtonEditorComponent(getQuestion(), selectionHandler);
		editorPanel.addListSelectionListener(new CloseEditorAfterSelectionHandler());
		
		popupInvokeButton = new PanelButton("");
		popupInvokeButton.addActionListener(new PopUpEditorHandler());
		
		addEditComponent(popupInvokeButton, translatedPopupButtonText);
	}
	
	public void setText(String text)
	{
		CodeList codeList = new CodeList();
		codeList.add(text);
		editorPanel.setText(codeList.toString());
		ChoiceItem choice = question.findChoiceByCode(text);
		popupInvokeButton.setText(choice.getLabel());
		popupInvokeButton.setIcon(new RatingIcon(choice));
	}
	
	public String getText()
	{
		return editorPanel.getText();
	}

	private void addEditComponent(JComponent component, String translatedText)
	{
		TwoColumnPanel panel = new TwoColumnPanel();
		panel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		panel.setBorder(BorderFactory.createEtchedBorder());
		PanelTitleLabel label = new PanelTitleLabel(translatedText);
		panel.add(label);
		panel.add(component);
		add(panel);
	}
	
	private class PopUpEditorHandler implements ActionListener
	{
		public PopUpEditorHandler()
		{
			selectRating();
		}

		public void actionPerformed(ActionEvent event)
		{
			editorDialog = new UndecoratedModelessDialogWithClose(EAM.getMainWindow(), EAM.text("Select")); 
			editorDialog.add(editorPanel);
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
		}
	}
	
	private ChoiceQuestion getQuestion()
	{
		return question;
	}

	private PanelButton popupInvokeButton;
	private UndecoratedModelessDialogWithClose editorDialog;
	private ChoiceQuestion question;
	private RadioButtonEditorComponent editorPanel;
}
