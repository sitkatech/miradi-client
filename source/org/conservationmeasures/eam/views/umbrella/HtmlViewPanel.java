/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

import org.conservationmeasures.eam.dialogs.EAMDialog;
import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.HtmlViewer;
import org.martus.swing.HyperlinkHandler;
import org.martus.swing.UiScrollPane;
import org.martus.swing.Utilities;

import com.jhlabs.awt.GridLayoutPlus;

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

		
		JButton close = new JButton(new CloseAction(dlg));

		JPanel panel = new JPanel(new GridLayoutPlus(0,1));
		bodyComponent.setFixedWidth(bodyComponent,bodyComponent.getPreferredSize().width);
		
		String columnZeroRowZero = "0, 0";
		panel.add(bodyComponent, columnZeroRowZero);
		String columnZeroRowOneCenteredCentered = "0, 1, c, c";
		panel.add(close, columnZeroRowOneCenteredCentered);
		
		panel.setBorder(new EmptyBorder(18,18,18,18));
		panel.setBackground(bodyComponent.getBackground());
		
		UiScrollPane scrollPane = new UiScrollPane(panel);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(EAM.STANDARD_SCROLL_INCREMENT);
		scrollPane.getVerticalScrollBar().setUnitIncrement(EAM.STANDARD_SCROLL_INCREMENT);
		dlg.getContentPane().add(scrollPane);
		dlg.pack();

		fudgeSize(dlg);
		
		dlg.getRootPane().setDefaultButton(close);
		close.requestFocus(true);
		dlg.setVisible(true);
	}

	// TODO: There really has to be a better way to get a dialog to contain and center its self
	private void fudgeSize(EAMDialog dlg)
	{
		Dimension dimension = dlg.getSize();
		dimension.height = dimension.height - 70;
		dimension.width = dimension.width + 20;
		dlg.setSize(dimension);
		
		Rectangle rectangle = Utilities.getViewableRectangle();
		rectangle.height = rectangle.height - 20;
		
		dlg.setLocation(Utilities.center(dimension, rectangle));
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
