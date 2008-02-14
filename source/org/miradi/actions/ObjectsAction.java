/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import javax.swing.Icon;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.views.Doer;
import org.miradi.views.umbrella.ObjectPicker;

public class ObjectsAction extends ViewAction implements ListSelectionListener
{
	public ObjectsAction(MainWindow mainWindowToUse, String label)
	{
		super(mainWindowToUse, label);
	}
	
	public ObjectsAction(MainWindow mainWindowToUse, String label, String icon)
	{
		super(mainWindowToUse, label, icon);
	}
	
	public ObjectsAction(MainWindow mainWindowToUse, String label, Icon icon)
	{
		super(mainWindowToUse, label, icon);
	}
	
	public boolean isObjectAction()
	{
		return true;
	}
	
	public void setPicker(ObjectPicker newPicker)
	{
		if(newPicker != null && picker != null)
			EAM.logWarning("Multiple pickers for " + getClass().getSimpleName());
		picker = newPicker;
		if(picker != null)
			picker.addSelectionChangeListener(this);
	}

	public Doer getDoer()
	{
		Doer doer = super.getDoer();
		if(doer != null)
			doer.setPicker(picker);
		return doer;
	}

	public void valueChanged(ListSelectionEvent event)
	{
		updateEnabledState();
	}

	ObjectPicker picker;
}
