/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MouseContextMenuAdapter extends MouseAdapter
{
	public MouseContextMenuAdapter(ComponentWithContextMenu owner)
	{
		ownerComponent = owner;
	}
	
	public void mousePressed(MouseEvent e)
	{
		if(e.isPopupTrigger())
			ownerComponent.showContextMenu(e);
		else
			super.mousePressed(e);
	}

	ComponentWithContextMenu ownerComponent;

}