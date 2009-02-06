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

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

abstract public class AbstractDataValueListComponent extends AbstractListComponent
{
	public AbstractDataValueListComponent(ChoiceQuestion questionToUse,	int columnCount, ListSelectionListener listener)
	{
		super(questionToUse, columnCount);
		
		listSelectionListener = listener;
	}
	
	public void valueChanged(ChoiceItem choiceItem, boolean isSelected) throws Exception
	{
		if (!skipNotice)
		{
			ListSelectionEvent event = new ListSelectionEvent("DUMMY EVENT",0,0, false);
			listSelectionListener.valueChanged(event);
		}
	}
	
	abstract public String getText();
	
	abstract public void setText(String codesToUse);
	
	protected boolean skipNotice;
	protected ListSelectionListener listSelectionListener;
}
