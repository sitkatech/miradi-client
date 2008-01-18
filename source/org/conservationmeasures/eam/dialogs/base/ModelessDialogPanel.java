/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import java.awt.BorderLayout;
import java.awt.LayoutManager2;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;

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
		EAM.logDebug("selectObject not handled by " + getClass().getName());
	}
	
	public Class getJumpActionClass()
	{
		return null;
	}

	abstract public BaseObject getObject();

}
