/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.conservationmeasures.eam.dialogs.ModelessDialogWithClose;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.VersionConstants;
import org.conservationmeasures.eam.views.Doer;
import org.martus.swing.Utilities;

public class About extends Doer
{
	public About()
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
		ModelessDialogWithClose dlg = new ModelessDialogWithClose(EAM.mainWindow, title);
		dlg.setModal(true);
		
		Box textBox = Box.createHorizontalBox();
		textBox.add(Box.createHorizontalGlue());
		JLabel bodyComponent = new JLabel(body);
		textBox.add(bodyComponent);
		bodyComponent.setFont(Font.getFont("Arial"));
		textBox.add(Box.createHorizontalGlue());
		
		JButton ok = new JButton(new OkAction(dlg));
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(ok);
		buttonBox.add(Box.createHorizontalGlue());
		
		Box box = Box.createVerticalBox();
		box.add(Box.createVerticalGlue());
		box.add(textBox);
		box.add(buttonBox);
		box.add(Box.createVerticalGlue());
		JPanel panel = (JPanel)dlg.getContentPane();
		panel.add(box);
		dlg.setSize(new Dimension(800, 550));
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
		"<p></p>" + 
		"<p>" +
		"This software program is being developed to assist conservation practitioners " +
		"to go through the adaptive management process outlined in the CMP's Open Standards " +
		"for the Practice of Conservation. To obtain a copy of the latest version of these standards, " +
		"go to www.conservationmeasures.org. " +
		"You can also find more information about this software program " +
		"at http://www.conservationmeasures.org/CMP/Site_Page.cfm?PageID=22. " +
		"If you have questions or suggestions, " +
		"please contact Nick Salafsky at Nick@FOSonline.org or at 1-301-263-2784. " +
		"</p>" +
		"<p></p>" +
		"<p>NOT FOR RELEASE OR REDISTRIBUTION</p>" +
		"<p></p>" +
		"<b>VERSION " + VersionConstants.VERSION_STRING +
		"</p>";

	public static final String licenseText = 
		"This pre-release version is intended for evaluation and feedback only. " +
		"Please send suggestions and other feedback to e-AM@conservationmeasures.org. " +
		"You are not allowed to redistribute this program without the express written " +
		"permission of The CMP or Benetech.";

	static final String aboutText = 
		"<html><table><tr><td align='center' valign='top'>" +
		"<h1>e-Adaptive Management Pre-Release Demo</h1>" +
		"<font face='Arial'>" +
		mainAboutText + 
		"<p></p>" +
		"<p>" +
		licenseText + 
		"</p>" +
		"<p></p>" + 
		"<p></p>" + 
		"</font>" + 
		"</td></tr></table></html>";
	
}
