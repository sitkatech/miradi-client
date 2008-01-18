/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.library;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.views.ActionViewImages;
import org.conservationmeasures.eam.main.EAMToolBar;

public class LibraryToolBar extends EAMToolBar
{
	public LibraryToolBar(Actions actions)
	{
		super(actions, ActionViewImages.class);
	}

}
