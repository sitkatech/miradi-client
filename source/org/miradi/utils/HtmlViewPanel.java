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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPopupMenu;

import org.martus.swing.UiLabel;
import org.martus.swing.Utilities;
import org.miradi.dialogs.base.DialogWithEscapeToClose;
import org.miradi.dialogs.fieldComponents.HtmlFormViewer;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.wizard.MiradiHtmlViewer;

public class HtmlViewPanel implements HtmlFormEventHandler
{
	public HtmlViewPanel(MainWindow mainWindowToUse, String titleToUse, String htmlTextToUse, HtmlFormEventHandler handlerToUse)
	{
		super();
		initVars(mainWindowToUse, titleToUse, handlerToUse, htmlTextToUse);
	}

	public HtmlViewPanel(MainWindow mainWindowToUse, String titleToUse, String htmlTextToUse)
	{
		this(mainWindowToUse, titleToUse, htmlTextToUse, new DummyHandler());
	}
	
	public HtmlViewPanel(MainWindow mainWindowToUse, String title, String text, int width)
	{
		this(mainWindowToUse, title, text);
		forcedWidth = width;
	}

	private void initVars(MainWindow mainWindowToUse, String titleToUse, HtmlFormEventHandler handlerToUse, String text)
	{
		// Choose a "reasonable" width, a bit narrower than the screen
		htmlText = text;
		forcedWidth = getAvailableSize().width - 200;
		viewTitle = titleToUse;
		delegateFormHandler = handlerToUse;
		mainWindow = mainWindowToUse;
	}
	
	protected MainWindow getMainWindow()
	{
		return mainWindow;
	}

	
	public void showAsOkDialog()
	{
		DialogWithEscapeToClose dlg = createDialog();
		dlg.setModal(true);

		showDialog(dlg);
	}
	
	public void showAsModelessOkDialog()
	{
		DialogWithEscapeToClose dlg = createDialog();
		dlg.setModal(false);

		showDialog(dlg);
	}

	private DialogWithEscapeToClose createDialog()
	{
		DialogWithEscapeToClose dialog = new DialogWithEscapeToClose(mainWindow);
		dialog.setTitle(viewTitle);
		return dialog;
	}

	private void showDialog(DialogWithEscapeToClose dlg)
	{
		String body = htmlText;
		if (body == null)
			return;
		HtmlFormViewer bodyComponent =  new MiradiHtmlViewer(mainWindow, this);
		bodyComponent.setText(body);
		bodyComponent.setFont(Font.getFont("Arial"));
		configureBodyComponent(bodyComponent);

		JComponent topSection = createTopPanel();
		JComponent buttonBar = createButtonBar(dlg);
		
		Container contents = dlg.getContentPane();
		contents.setBackground(AppPreferences.getWizardTitleBackground());
		contents.setLayout(new BorderLayout());
		if(topSection != null)
			contents.add(topSection, BorderLayout.BEFORE_FIRST_LINE);
		contents.add(new MiradiScrollPane(bodyComponent), BorderLayout.CENTER);
		contents.add(buttonBar, BorderLayout.AFTER_LAST_LINE);
		
		calculateHeight(dlg, contents, bodyComponent, buttonBar);
		Utilities.centerDlg(dlg);
		close.requestFocus(true);
		dlg.getRootPane().setDefaultButton(close);
		dlg.setVisible(true);
	}


	protected void configureBodyComponent(HtmlFormViewer bodyComponent)
	{
	}

	protected JComponent createTopPanel()
	{
		return null;
	}

	private void calculateHeight(DialogWithEscapeToClose dlg, Container contents, HtmlFormViewer bodyComponent, JComponent buttonBar)
	{
		// Compute dialog size based on that fixed content width
		bodyComponent.setFixedWidth(bodyComponent, forcedWidth);
		Dimension preferredContentSize = contents.getPreferredSize();
		preferredContentSize.height += buttonBar.getPreferredSize().height;
		dlg.getContentPane().setPreferredSize(preferredContentSize);

		// Prevent dialog from being larger than the available screen space
		Dimension candidateDialogSize = dlg.getPreferredSize();
		candidateDialogSize.width = Math.min(candidateDialogSize.width, getAvailableSize().width);
		candidateDialogSize.height = Math.min(candidateDialogSize.height, getAvailableSize().width);
		
		// TODO: If the dialog is too wide and not very tall, retry with a narrower width 

		// Make it so
		dlg.setSize(candidateDialogSize);
	}

	private Dimension getAvailableSize()
	{
		return Utilities.getViewableRectangle().getSize();
	}


	protected JComponent createButtonBar(DialogWithEscapeToClose dlg)
	{
		close = new JButton(new CloseAction(dlg));
		dlg.getRootPane().setDefaultButton(close);

		OneRowPanel buttonBar = new OneRowPanel();
		buttonBar.setMargins(5);
		buttonBar.setAlignmentRight();
		buttonBar.setBackground(AppPreferences.getWizardTitleBackground());
		buttonBar.add(new UiLabel(" "));
		buttonBar.add(close);

		return buttonBar;
	}
	


	protected class CloseAction extends AbstractAction
	{
		public CloseAction(JDialog dialogToClose)
		{
			this(dialogToClose, EAM.text("Button|Close"));
		}
		
		public CloseAction(JDialog dialogToClose, String buttonText)
		{
			super(buttonText);
			dlg = dialogToClose;
		}

		public void actionPerformed(ActionEvent arg0)
		{
			dlg.dispose();
		}
		
		JDialog dlg;
	}
	
	public void setCloseButton(JButton newCloseButton)
	{
		close = newCloseButton;
	}

	public void buttonPressed(String buttonName)
	{
		delegateFormHandler.buttonPressed(buttonName);
	}

	public JPopupMenu getRightClickMenu(String url)
	{
		return delegateFormHandler.getRightClickMenu(url);
	}

	public void linkClicked(String linkDescription)
	{	
		if (mainWindow.mainLinkFunction(linkDescription))
			return;
		
		delegateFormHandler.linkClicked(linkDescription);
	}

	
	public void valueChanged(String widget, String newValue)
	{
		delegateFormHandler.valueChanged(widget, newValue);
	}
	
	public void setComponent(String name, JComponent component)
	{
		delegateFormHandler.setComponent(name, component);
	}
	
	static class DummyHandler implements HtmlFormEventHandler
	{

		public void setComponent(String name, JComponent component)
		{
		}

		public void buttonPressed(String buttonName)
		{
		}

		public JPopupMenu getRightClickMenu(String url)
		{
			return null;
		}

		public void linkClicked(String linkDescription)
		{
		}

		public void valueChanged(String widget, String newValue)
		{
		}
		
	}

	private int forcedWidth;
	private String viewTitle;
	private String htmlText;
	private HtmlFormEventHandler delegateFormHandler;
	private JButton close;
	private MainWindow mainWindow;
}
