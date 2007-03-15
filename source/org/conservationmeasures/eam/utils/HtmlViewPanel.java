/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.dialogs.EAMDialog;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.Utilities;

public class HtmlViewPanel implements HtmlFormEventHandler
{

	// FIXME: These constructors are a mess...might want to be multiple classes?
	public HtmlViewPanel(MainWindow mainWindowToUse, String titleToUse, String htmlTextToUse)
	{
		this(mainWindowToUse, titleToUse, htmlTextToUse, new DummyHandler());
	}
	
	public HtmlViewPanel(MainWindow mainWindowToUse, String titleToUse, String htmlTextToUse, HtmlFormEventHandler handlerToUse)
	{
		super();
		htmlText = htmlTextToUse;
		initVars(mainWindowToUse, titleToUse, handlerToUse);
	}


	public HtmlViewPanel(MainWindow mainWindowToUse, String titleToUse, Class classToUse, String htmlFileNameToUse)
	{
		this(mainWindowToUse, titleToUse,  classToUse,  htmlFileNameToUse, new DummyHandler());
	}

	
	public HtmlViewPanel(MainWindow mainWindowToUse, String titleToUse, Class classToUse, String htmlFileNameToUse, HtmlFormEventHandler handlerToUse)
	{
		super();
		resourceClass = classToUse;
		htmlFileName = htmlFileNameToUse;
		initVars(mainWindowToUse, titleToUse, handlerToUse);
	}
	
	private void initVars(MainWindow mainWindowToUse, String titleToUse, HtmlFormEventHandler handlerToUse)
	{
		viewTitle = titleToUse;
		delegateFormHandler = handlerToUse;
		mainWindow = mainWindowToUse;
		closeButtonText = EAM.text("Close");
	}

	
	public void showOkDialog()
	{
		String title = EAM.text("Title|" + viewTitle);
		EAMDialog dlg = new EAMDialog(mainWindow, title);
		dlg.setModal(true);

		String body = loadHtml();
		if (body == null)
			return;
		HtmlFormViewer bodyComponent =  new HtmlFormViewer(body, this);
		bodyComponent.setFont(Font.getFont("Arial"));
		dlg.setBackground(bodyComponent.getBackground());

		Container contents = dlg.getContentPane();
		contents.setLayout(new BorderLayout());
		contents.add(new JScrollPane(bodyComponent), BorderLayout.CENTER);
		contents.add(createButtonBar(dlg), BorderLayout.AFTER_LAST_LINE);
		
		calculateHeight(dlg, contents, bodyComponent);
		Utilities.centerDlg(dlg);
		close.requestFocus(true);
		dlg.setVisible(true);
	}


	private void calculateHeight(EAMDialog dlg, Container contents, HtmlFormViewer bodyComponent)
	{
		final int buttonBarHeight = 40;
		final int forcedWidth = 900;
		
		bodyComponent.setFixedWidth(bodyComponent, forcedWidth);
		Rectangle rectangle = Utilities.getViewableRectangle();
		Dimension dim = contents.getPreferredSize();
		
		if (rectangle.height > dim.height)
			rectangle.height = dim.height + buttonBarHeight;
//FIXME: method needs to be re thought 
		dlg.setSize(forcedWidth, rectangle.height);
	}


	private Box createButtonBar(EAMDialog dlg)
	{
		close = new JButton(new CloseAction(dlg));
		dlg.getRootPane().setDefaultButton(close);
		Box buttonBar = Box.createHorizontalBox();
		Component[] components = new Component[] {Box.createHorizontalGlue(), close, Box.createHorizontalStrut(10)};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
	}
	

	
	private String loadHtml()
	{
		if (htmlText!=null)
			return htmlText;
		
		try
		{
			return EAM.loadResourceFile(resourceClass, htmlFileName);
		}
		catch (Exception e)
		{
			EAM.errorDialog("ERROR: Feature file not found: " + resourceClass + "/" + htmlFileName );
			return null;
		}
	}


	
	class CloseAction extends AbstractAction
	{
		public CloseAction(JDialog dialogToClose)
		{
			super(getCloseButtonText());
			dlg = dialogToClose;
		}

		public void actionPerformed(ActionEvent arg0)
		{
			dlg.dispose();
		}
		
		JDialog dlg;
	}

	String getCloseButtonText()
	{
		return closeButtonText;
	}
	
	public void setCloseButtonText(String text)
	{
		closeButtonText = text;
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

	private String viewTitle;
	private Class resourceClass;
	private String htmlText;
	private String htmlFileName;
	private HtmlFormEventHandler delegateFormHandler;
	private JButton close;
	private MainWindow mainWindow;
	private String closeButtonText;

}
