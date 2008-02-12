/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.library;

import org.miradi.actions.Actions;
import org.miradi.actions.views.ActionViewImages;
import org.miradi.main.EAMToolBar;

public class LibraryToolBar extends EAMToolBar
{
	public LibraryToolBar(Actions actions)
	{
		super(actions, ActionViewImages.class);
	}

}
