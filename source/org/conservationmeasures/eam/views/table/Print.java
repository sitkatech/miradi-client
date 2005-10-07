/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.table;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.PrintDoer;

public class Print extends PrintDoer 
{
	public boolean isAvailable() 
	{
		Project project = getMainWindow().getProject();
		if(!project.isOpen())
			return false;
		TableView view = (TableView)getMainWindow().getCurrentView();
		return view.anythingToPrint();
	}
}
