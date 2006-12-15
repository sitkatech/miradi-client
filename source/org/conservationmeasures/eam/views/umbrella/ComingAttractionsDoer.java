/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.Doer;

public class ComingAttractionsDoer extends Doer
{
	public ComingAttractionsDoer()
	{
	}
	
	public boolean isAvailable()
	{
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		new HtmlViewPanel("Coming Attactions", UmbrellaView.class, "ComingAttractions.html").showOkDialog();
	}
	
	MainWindow mainWindow;
}

