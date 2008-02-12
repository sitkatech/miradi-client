/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.miradi.actions.EAMAction;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;

public class MouseAdapterDoubleClickDelegator extends MouseAdapter
{
	
	public MouseAdapterDoubleClickDelegator(EAMAction delegateActionIn) 
	{
		super();
		delegateAction = delegateActionIn;
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
