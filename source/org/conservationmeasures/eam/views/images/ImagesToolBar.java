/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.images;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.views.ActionViewImages;
import org.conservationmeasures.eam.main.EAMToolBar;

public class ImagesToolBar extends EAMToolBar
{
	public ImagesToolBar(Actions actions)
	{
		super(actions, ActionViewImages.class);
	}

}
