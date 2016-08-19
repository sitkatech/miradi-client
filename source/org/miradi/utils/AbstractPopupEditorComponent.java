/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.utils;

import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.tablerenderers.ExpandingReadonlyTableCellEditorOrRendererFactory;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static org.miradi.main.Miradi.isWindows;

abstract public class AbstractPopupEditorComponent extends PopupEditorComponent
{
	public AbstractPopupEditorComponent(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
		setBackground(AppPreferences.getDataPanelBackgroundColor());
		
		createComponents();
		add(currentSelectionText, BorderLayout.CENTER);
		add(popupInvokeButton, BorderLayout.AFTER_LINE_ENDS);
		addListeners();
	}
	
	public void setStopEditingListener(ExpandingReadonlyTableCellEditorOrRendererFactory listener)
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
		currentSelectionText = new EditableHtmlPane(getMainWindow());
		currentSelectionText.setEditable(false);

		// MRD-6036 - force margin to 0 on windows
		if (isWindows())
			currentSelectionText.setMargin(new Insets(0, 0, 0, 0));

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
		currentSelectionText.setEditable(isEnabled);
	}
	
	protected MainWindow getMainWindow()
	{
		return mainWindow;
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
				EAM.alertUserOfNonFatalException(e);
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
				EAM.alertUserOfNonFatalException(e);
			}
		}
	}
	
	public void setEditable(boolean b)
	{
		currentSelectionText.setEditable(b);
	}

	abstract protected void invokePopupEditor() throws Exception;

	private MainWindow mainWindow;
	private PanelButton popupInvokeButton;
	private PopUpEditorHandler popupEditHandler;
	private EditableHtmlPane currentSelectionText;
	protected ExpandingReadonlyTableCellEditorOrRendererFactory stopEditingListener;
}
