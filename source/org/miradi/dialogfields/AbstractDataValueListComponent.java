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
package org.miradi.dialogfields;

import java.util.Vector;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

abstract public class AbstractDataValueListComponent extends AbstractQuestionEditorComponent
{
	public AbstractDataValueListComponent(ChoiceQuestion questionToUse,	int columnCount, ListSelectionListener listener)
	{
		super(questionToUse, columnCount);
		
		listSelectionListeners = new Vector();
		addListSelectionListener(listener);
	}
	
	public void valueChanged(ChoiceItem choiceItem, boolean isSelected) throws Exception
	{
		if (!shouldSkipNotification())
		{
			for(ListSelectionListener listSelectionListener : listSelectionListeners)
			{
				ListSelectionEvent event = new ListSelectionEvent(choiceItem.getCode(),0,0, false);
				listSelectionListener.valueChanged(event);
			}
		}
	}
	
	public void addListSelectionListener(ListSelectionListener listSelectionListenerToAdd)
	{
		if (!listSelectionListeners.contains(listSelectionListenerToAdd))
			listSelectionListeners.add(listSelectionListenerToAdd);
	}
	
	public void removeListSelectionListener(ListSelectionListener listSelectionListenerToAdd)
	{
		listSelectionListeners.remove(listSelectionListenerToAdd);
	}

	private boolean shouldSkipNotification()
	{
		return skipNotification;
	}
	
	protected void enableSkipNotification()
	{
		skipNotification = true;
	}
	
	protected void disableSkipNotification()
	{
		skipNotification = false;
	}
	
	abstract public String getText();
	
	abstract public void setText(String codesToUse);
	
	private boolean skipNotification;
	private Vector<ListSelectionListener> listSelectionListeners;
}
