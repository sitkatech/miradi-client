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

import org.conservationmeasures.eam.dialogs.EAMDialog;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.VersionConstants;
import org.conservationmeasures.eam.views.Doer;
import org.martus.swing.HtmlViewer;
import org.martus.swing.HyperlinkHandler;
import org.martus.swing.Utilities;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.BrowserLauncherRunner;

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
		String title = EAM.text("Title|About Miradi");
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
	
	static final String aboutText = 
			"<FONT face=Tahoma size=6><STRONG>" +
			"<IMG src=\"images/miradi64.png\" height=64 width=64>Miradi</STRONG>" + 
			"</FONT>" +
			"<FONT size=2>TM</FONT>" +
			"<BR>" +
			"<FONT face=Tahoma size=4>" +
			"<STRONG>Adaptive Management Software for Conservation Projects</STRONG>" +
			"</FONT>&nbsp;" +
			"<FONT size=4>" +
			"<P>(c) Copyright 2005-2007, Wildlife Conservation Society, Bronx, New York on" + 
			"behalf of the Conservation Measures Partnership (\"CMP\") and Beneficent " +
			"Technology, Inc. (\"Benetech\"), Palo Alto, California.</P>" +
			"<p>VERSION " + VersionConstants.VERSION_STRING +"</p>" +
			"<P>NOT FOR REDISTRIBUTION<BR>This pre-release version is intended for evaluation" + 
			"and feedback only. Please send suggestions and other feedback to " +
			"<A href=\"mailto:feedback@miradi.org\">feedback@miradi.org</A>. You are not allowed" + 
			"to redistribute this program without the express written permission of the CMP " +
			"(<A href=\"http://www.conservationmeasures.org\">www.conservationmeasures.org</A>)" +
			"&nbsp;or Benetech (<A href=\"http://www.benetech.org\">www.benetech.org</A>). You can also " +
			"find more information about this software program at: " +
			"<A href=\"http://www.miradi.org\">www.miradi.org</A>.</P>" +
			"<P>This software program is being developed to assist conservation practitioners" + 
			"to go through the adaptive management process outlined in the CMP's <EM>Open " +
			"Standards for the Practice of Conservation</EM>.&nbsp;To obtain a copy of the " +
			"latest version of these standards, go to " +
			"</font>" +
			"<A href=\"www.conservationmeasures.org\">" +
			"<U><FONT color=#0000ff>www.conservationmeasures.org</U></FONT>" + 
			"</A>" +
			".&nbsp;&nbsp; </P>" +
			"<P>&nbsp;</P>";
	
	public void buttonPressed(String buttonName)
	{
	}

	public void linkClicked(String linkDescription)
	{
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

	public JPopupMenu getRightClickMenu(String url)
	{
		return null;
	}

	public void valueChanged(String widget, String newValue)
	{
	}
	
}
