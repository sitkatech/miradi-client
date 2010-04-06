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

import javax.swing.JTextArea;

import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.tablerenderers.MultiLineEditableObjectTableCellEditorOrRendererFactory;
import org.miradi.main.AppPreferences;

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
		PopUpEditorHandler popupEditHandler = new PopUpEditorHandler();
		popupInvokeButton.addActionListener(popupEditHandler);
	}
	
	@Override
	public void setBackground(Color bg)
	{
		super.setBackground(bg);
		if (currentSelectionText != null)
			currentSelectionText.setBackground(bg);
	}
	
	private void createComponents()
	{
		currentSelectionText = new JTextArea();
		currentSelectionText.setEditable(false);
		currentSelectionText.setLineWrap(true);
		currentSelectionText.setWrapStyleWord(true);

		popupInvokeButton = new PopupEditorButton();
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
	
	abstract protected void invokePopupEditor();

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
	protected MultiLineEditableObjectTableCellEditorOrRendererFactory stopEditingListener;
}
