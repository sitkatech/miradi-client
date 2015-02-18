/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.utils;

import javax.swing.event.EventListenerList;

import org.miradi.dialogs.fieldComponents.ChoiceItemWithXmlTextRendererComboBox;
import org.miradi.questions.ChoiceItem;

public class UiComboBoxWithSaneActionFiring extends ChoiceItemWithXmlTextRendererComboBox
{
	public UiComboBoxWithSaneActionFiring(ChoiceItem[] choices)
	{
		super(choices);
	}

	public void setSelectedItemWithoutFiring(Object anObject)
	{
		EventListenerList listeners = listenerList;
		try
		{
			listenerList = new EventListenerList();
			setSelectedItem(anObject);
		}
		finally
		{
			listenerList = listeners;
		}
	}
}
