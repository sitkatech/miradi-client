/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.VersionConstants;

public class ActionAbout extends ProjectAction
{
	public ActionAbout(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|About...");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		String title = EAM.text("Title|About e-Adaptive Management");
		String[] body = 
		{
			EAM.text(productName),
			EAM.text(copyright),
			EAM.text(version),
			EAM.text(description),
			EAM.text(contact),
		};
		EAM.okDialog(title, body);
		
	}

	public boolean shouldBeEnabled()
	{
		return true;
	}
	
	static final String productName = "e-Adaptive Management -- NOT FOR RELEASE";
	static final String copyright = "Copyright 2005, The Conservation Measures Partnership and " + 
		"Beneficent Technology, Inc. (Benetech, at www.benetech.org)\n";
	static final String version = "Version " + VersionConstants.VERSION_STRING +  
		" (This pre-release version is intended for evaluation and feedback only)";
	static final String description = "This software program is being developed " +
		"by the Conservation Measures Partnership (CMP) to assist conservation " +
		"practitioners to go through the adaptive management process outlined in the " +
		"CMP's Open Standards for the Practice of Conservation.";
	static final String contact = "If you have questions or suggestions, " +
		"please contact Nick Salafsky at Nick@FOSonline.org or at 1-301-263-2784.";
}
