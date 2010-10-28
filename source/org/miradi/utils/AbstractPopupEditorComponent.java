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
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTextArea;
import org.miradi.dialogs.tablerenderers.MultiLineEditableObjectTableCellEditorOrRendererFactory;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;

abstract public class AbstractPopupEditorComponent extends PopupEditorComponent
{
	public AbstractPopupEditorComponent()
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

	private void addListeners()
	{
		popupEditHandler = new PopUpEditorHandler();
		popupInvokeButton.addActionListener(popupEditHandler);
	}
	
	@Override
	public void setBackground(Color bg)
	{
		super.setBackground(bg);
		if (currentSelectionText != null)
			currentSelectionText.setBackground(bg);
	}
	
	@Override
	public void setForeground(Color fg)
	{
		super.setForeground(fg);
		if (currentSelectionText != null)
			currentSelectionText.setForeground(fg);
	}
	
	private void createComponents()
	{
		currentSelectionText = new PanelTextArea();
		currentSelectionText.setEditable(false);
		currentSelectionText.setLineWrap(true);
		currentSelectionText.setWrapStyleWord(true);

		popupInvokeButton = new PopupEditorButton();
	}
	
	public void dispose()
	{
		popupInvokeButton.removeActionListener(popupEditHandler);
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
	
	public void setInvokeButtonEnabled(boolean isEnabled)
	{
		popupInvokeButton.setEnabled(isEnabled);
	}
	
	private class PopUpEditorHandler extends MouseAdapter implements ActionListener 
	{
		@Override
		public void mouseReleased(MouseEvent event)
		{
			try
			{
				invokePopupEditor();
			}
			catch(Exception e)
			{
				EAM.unexpectedErrorDialog(e);
			}
		}
		
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				invokePopupEditor();
			}
			catch(Exception e)
			{
				EAM.unexpectedErrorDialog(e);
			}
		}
	}
	
	abstract protected void invokePopupEditor() throws Exception;

	private PanelButton popupInvokeButton;
	private PopUpEditorHandler popupEditHandler;
	private PanelTextArea currentSelectionText;
	protected MultiLineEditableObjectTableCellEditorOrRendererFactory stopEditingListener;
}
