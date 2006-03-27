/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.images;

import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionViewImages;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.ViewSwitcher;

public class ImagesToolBar extends JToolBar
{
	public ImagesToolBar(Actions actions)
	{
		setFloatable(false);

		add(ViewSwitcher.create(actions, ActionViewImages.class));
	}

}
