/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import javax.swing.Icon;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.Doer;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

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
