/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

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
import javax.swing.JDialog;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.dialogs.EAMDialog;
import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.HtmlViewer;
import org.martus.swing.HyperlinkHandler;
import org.martus.swing.Utilities;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.BrowserLauncherRunner;

public class HtmlViewPanel implements HyperlinkHandler
{

	public HtmlViewPanel(String titleToUse, Class viewClassToUse, String htmlFileNameToUse)
	{
		super();
		viewTitle = titleToUse;
		viewClass = viewClassToUse;
		htmlFileName = htmlFileNameToUse;
	}

	void showOkDialog()
	{
		String title = EAM.text("Title|" + viewTitle);
		EAMDialog dlg = new EAMDialog(EAM.mainWindow, title);
		dlg.setModal(true);

		String body = loadHtml();
		if (body == null)
			return;
		HtmlViewer bodyComponent =  new HtmlViewer(body, this);
		bodyComponent.setFont(Font.getFont("Arial"));
		dlg.setBackground(bodyComponent.getBackground());

		Container contents = dlg.getContentPane();
		contents.setLayout(new BorderLayout());
		contents.add(new JScrollPane(bodyComponent), BorderLayout.CENTER);
		contents.add(createButtonBar(dlg), BorderLayout.AFTER_LAST_LINE);
		
		calculateHeight(dlg, contents, bodyComponent);
		Utilities.centerDlg(dlg);
		dlg.setVisible(true);
	}


	private void calculateHeight(EAMDialog dlg, Container contents, HtmlViewer bodyComponent)
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
		JButton close = new JButton(new CloseAction(dlg));
		close.requestFocus(true);
		dlg.getRootPane().setDefaultButton(close);
		Box buttonBar = Box.createHorizontalBox();
		Component[] components = new Component[] {Box.createHorizontalGlue(), close};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
	}
	
	private String loadHtml()
	{
		try
		{
			return EAM.loadResourceFile(viewClass, htmlFileName);
		}
		catch (Exception e)
		{
			EAM.errorDialog("ERROR: Feature file not found: " + viewClass + "/" + htmlFileName );
			return null;
		}
	}


	
	static class CloseAction extends AbstractAction
	{
		public CloseAction(JDialog dialogToClose)
		{
			super("CLOSE");
			dlg = dialogToClose;
		}
		
		public void actionPerformed(ActionEvent arg0)
		{
			dlg.dispose();
		}
		
		JDialog dlg;
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
		if (!linkDescription.startsWith(HTTP_PROTOCOL) && 
			!linkDescription.startsWith(MAIL_PROTOCOL))
			return;
			
        try 
        {
            BrowserLauncherRunner runner = new BrowserLauncherRunner(
            		new BrowserLauncher(null),
                    "",
                    linkDescription,
                    null);
            new Thread(runner).start();
        }
        catch (Exception e) 
        {
        	EAM.logException(e);
        }
	}

	public void valueChanged(String widget, String newValue)
	{
	}
	
	private static String HTTP_PROTOCOL = "http";
	private static String MAIL_PROTOCOL = "mailto:";
	private String viewTitle;
	private Class viewClass;
	private String htmlFileName;

}
