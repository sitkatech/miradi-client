/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import javax.swing.JPanel;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.umbrella.About;

abstract public class UmbrellaView extends JPanel
{
	abstract public String cardName();
	
	public void doAbout() throws CommandFailedException
	{
		About.doAbout();
	}
	
}
