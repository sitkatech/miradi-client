/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import info.clearthought.layout.TableLayout;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import net.sf.wraplog.NoneLogger;

import org.conservationmeasures.eam.dialogs.EAMDialog;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.VersionConstants;
import org.conservationmeasures.eam.views.Doer;
import org.martus.swing.HtmlViewer;
import org.martus.swing.HyperlinkHandler;
import org.martus.swing.Utilities;

import edu.stanford.ejalbert.BrowserLauncher;

public class AboutDoer extends Doer  implements HyperlinkHandler
{
	public AboutDoer()
	{
	}
	
	public boolean isAvailable()
	{
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		String title = EAM.text("Title|About e-Adaptive Management");
		showOkDialog(title, aboutText);
	}
	
	void showOkDialog(String title, String body)
	{
		EAMDialog dlg = new EAMDialog(EAM.mainWindow, title);
		dlg.setModal(true);
		
		HtmlViewer bodyComponent =  new HtmlViewer(body, this);
		bodyComponent.setFont(Font.getFont("Arial"));
		bodyComponent.setSize(new Dimension(750, Short.MAX_VALUE));
		
		JButton ok = new JButton(new OkAction(dlg));

		// Seems like there should be an easier way to enforce a fixed width
		// and allow an HTML text component to become as high as necessary.
		// I couldn't find any sane way using any built-in LayoutManager,
		// nor with any jhlabs LayoutManager. 2006-11-18 kbs
		double[] rowSizes = {bodyComponent.getWidth()};
		double[] columnSizes = {TableLayout.PREFERRED, TableLayout.PREFERRED};
		double gridSizes[][] = { rowSizes, columnSizes };
		JPanel panel = new JPanel(new TableLayout(gridSizes));
		String columnZeroRowZero = "0, 0";
		panel.add(bodyComponent, columnZeroRowZero);
		String columnZeroRowOneCenteredCentered = "0, 1, c, c";
		panel.add(ok, columnZeroRowOneCenteredCentered);
		
		panel.setBorder(new EmptyBorder(18,18,18,18));
		panel.setBackground(bodyComponent.getBackground());
		
		dlg.getContentPane().add(new JScrollPane(panel));
		dlg.pack();
		dlg.setLocation(Utilities.center(dlg.getSize(), Utilities.getViewableRectangle()));
		
		dlg.getRootPane().setDefaultButton(ok);
		ok.requestFocus(true);
		dlg.setVisible(true);
		
	}

	static class OkAction extends AbstractAction
	{
		public OkAction(JDialog dialogToClose)
		{
			super("OK");
			dlg = dialogToClose;
		}
		
		public void actionPerformed(ActionEvent arg0)
		{
			dlg.dispose();
		}
		
		JDialog dlg;
	}

	public static final String mainAboutText = 
		"<p>" +
		"Copyright 2005-2006, The Conservation Measures Partnership (CMP, at www.conservationmeasures.org) " +
		"and Beneficent Technology, Inc. (Benetech, at www.benetech.org)" +
		"</p>" +

		"<p align='left'>" +
		"This software program is being developed to assist conservation practitioners " +
		"to go through the adaptive management process outlined in the CMP's Open Standards " +
		"for the Practice of Conservation. To obtain a copy of the latest version of these standards, " +
		"go to www.conservationmeasures.org. " +
		"You can also find more information about this software program " +
		"at:" +
		"</p>" +
		"<p align='center'>" +
		"<a href=\"http://www.conservationmeasures.org/CMP/Site_Page.cfm?PageID=22\">CMP Site</a>" +
		"</p>" +
		"<p align='left'>" +
		"If you have questions or suggestions, " +
		"please contact Nick Salafsky at Nick@FOSonline.org or at 1-301-263-2784. " +
		"</p>" +
		"<p>NOT FOR RELEASE OR REDISTRIBUTION</p>" +
		"<b>VERSION " + VersionConstants.VERSION_STRING +
		"</p>";

	public static final String licenseText = 
		"<p align='left'>"  +
		"This pre-release version is intended for evaluation and feedback only. " +
		"Please send suggestions and other feedback to e-AM@conservationmeasures.org. " +
		"You are not allowed to redistribute this program without the express written " +
		"permission of The CMP or Benetech." + 
		"</p>" ;

	static final String aboutText = 
		"<html><table><tr><td align='center' valign='top'>" +
		"<h1>e-Adaptive Management Pre-Release Demo</h1>" +
		"<font face='Arial'>" +
		mainAboutText + 
		licenseText + 
		"</font>" + 
		"</td></tr>" +
		"</table></html>";

	public void buttonPressed(String buttonName)
	{
	}

	public void linkClicked(String linkDescription)
	{
		try
		{
			BrowserLauncher browseLauncher = new BrowserLauncher(new NoneLogger());
			browseLauncher.openURLinBrowser(linkDescription);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	public JPopupMenu getRightClickMenu(String url)
	{
		return null;
	}

	public void valueChanged(String widget, String newValue)
	{
	}
	
}
