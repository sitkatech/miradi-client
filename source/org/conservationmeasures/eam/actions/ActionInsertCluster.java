/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.ClusterIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertCluster extends LocationAction
{
	public ActionInsertCluster(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), new ClusterIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Factor Group");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Create a group to contain factors");
	}

}
