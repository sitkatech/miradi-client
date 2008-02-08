/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPopupMenu;

import org.conservationmeasures.eam.dialogs.base.EAMDialog;
import org.conservationmeasures.eam.dialogs.fieldComponents.HtmlFormViewer;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.wizard.MiradiHtmlViewer;
import org.martus.swing.Utilities;

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
	
	public HtmlViewPanel(MainWindow mainWindowToUse, String titleToUse, Class classToUse, String htmlFileNameToUse)
	{
		this(mainWindowToUse, titleToUse,  classToUse,  htmlFileNameToUse, new DummyHandler());
	}

	public HtmlViewPanel(MainWindow mainWindowToUse, String titleToUse, Class classToUse, String htmlFileNameToUse, HtmlFormEventHandler handlerToUse)
	{
		this(mainWindowToUse, titleToUse, loadResourceFile(classToUse, htmlFileNameToUse), handlerToUse);
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
		closeButtonText = EAM.text("Close");
	}

	
	public void showAsOkDialog()
	{
		String title = EAM.text("Title|" + viewTitle);
		EAMDialog dlg = new EAMDialog(mainWindow, title);
		dlg.setModal(true);

		String body = htmlText;
		if (body == null)
			return;
		HtmlFormViewer bodyComponent =  new MiradiHtmlViewer(mainWindow, this);
		bodyComponent.setText(body);
		bodyComponent.setFont(Font.getFont("Arial"));
		dlg.getContentPane().setBackground(AppPreferences.WIZARD_TITLE_BACKGROUND);

		JComponent buttonBar = createButtonBar(dlg);
		
		Container contents = dlg.getContentPane();
		contents.setLayout(new BorderLayout());
		contents.add(new FastScrollPane(bodyComponent), BorderLayout.CENTER);
		contents.add(buttonBar, BorderLayout.AFTER_LAST_LINE);
		
		calculateHeight(dlg, contents, bodyComponent, buttonBar);
		Utilities.centerDlg(dlg);
		close.requestFocus(true);
		dlg.setVisible(true);
	}


	private void calculateHeight(EAMDialog dlg, Container contents, HtmlFormViewer bodyComponent, JComponent buttonBar)
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


	private Box createButtonBar(EAMDialog dlg)
	{
		close = new JButton(new CloseAction(dlg));
		dlg.getRootPane().setDefaultButton(close);
		Box buttonBar = Box.createHorizontalBox();
		Component[] components = new Component[] {Box.createHorizontalGlue(), close, Box.createHorizontalStrut(10)};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
	}
	

	public static String loadResourceFile(Class resourceClass, String htmlFileName)
	{
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

	private int forcedWidth;
	private String viewTitle;
	private String htmlText;
	private HtmlFormEventHandler delegateFormHandler;
	private JButton close;
	private MainWindow mainWindow;
	private String closeButtonText;

}
