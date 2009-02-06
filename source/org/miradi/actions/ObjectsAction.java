/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
		if(picker == newPicker)
			return;
		
		if(picker != null && newPicker != null)
			EAM.logWarning("Multiple pickers for " + getClass().getSimpleName());
		
		if(picker != null)
			picker.removeSelectionChangeListener(this);

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
	
	public ObjectPicker getPicker()
	{
		return picker;
	}

	ObjectPicker picker;
}
