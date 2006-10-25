/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class MouseAdapterDoubleClickDelegator extends MouseAdapter
{
	
	public MouseAdapterDoubleClickDelegator(Class delegateActionIn, MainWindow mainWindow) 
	{
		super();
		delegateAction = mainWindow.getActions().get(delegateActionIn);
	}
	
    public void mouseClicked(MouseEvent e)
    {
		if(e.getClickCount() == 2)
		{
			try
			{
				delegateAction.doAction();
			}
			catch(CommandFailedException ex)
			{
				EAM.logException(ex);
				EAM.errorDialog("Action failed on mouseClicked:" + ex);
			}
		}
	}
    
    EAMAction delegateAction;
}
