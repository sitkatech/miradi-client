/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.martus.swing.UiButton;

public class MouseAdapterDoubleClickDelegator extends MouseAdapter
{
	
	public MouseAdapterDoubleClickDelegator(UiButton delegateActionIn) 
	{
		super();
		delegateAction = delegateActionIn;
	}
	
    public void mouseClicked(MouseEvent e){
	      if (e.getClickCount() == 2){
	    	  delegateAction.doClick();
	         }
    }
    
    UiButton delegateAction;
}
