/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.base;

import java.awt.BorderLayout;
import java.awt.LayoutManager2;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;

abstract public class ModelessDialogPanel extends DisposablePanelWithDescription
{
	public ModelessDialogPanel()
	{
		this(new BorderLayout());
	}
	
	public ModelessDialogPanel(LayoutManager2 layoutToUse)
	{
		super(layoutToUse);
	}
	
	public void objectWasSelected(BaseId selectedId)
	{
		
	}
	
	public void selectObject(BaseObject objectToSelect)
	{
		EAM.logWarning("selectObject not handled by " + getClass().getName());
	}
	
	public Class getJumpActionClass()
	{
		return null;
	}

	abstract public BaseObject getObject();

}
