/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.images;

import org.conservationmeasures.eam.actions.ActionViewImages;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAMToolBar;

public class ImagesToolBar extends EAMToolBar
{
	public ImagesToolBar(Actions actions)
	{
		super(actions, ActionViewImages.class);
	}

}
