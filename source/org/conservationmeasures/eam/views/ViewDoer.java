/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

abstract public class ViewDoer extends MainWindowDoer
{
	public void setView(UmbrellaView view)
	{
		this.view = view;
	}

	public UmbrellaView getView()
	{
		return view;
	}

	private UmbrellaView view;
}
