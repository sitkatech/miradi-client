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

package org.miradi.questions;

import java.util.Vector;

import org.miradi.dialogs.dashboard.AbstractLongDescriptionProvider;
import org.miradi.dialogs.dashboard.StaticLongDescriptionProvider;
import org.miradi.main.EAM;

public class ChoiceItemWithChildren extends ChoiceItemWithLongDescriptionProvider
{
	public ChoiceItemWithChildren(String codeToUse, AbstractLongDescriptionProvider providerToUse)
	{
		this(codeToUse, "", "", providerToUse);
	}
	
	public ChoiceItemWithChildren(String codeToUse, String additionalTextToUse, AbstractLongDescriptionProvider providerToUse)
	{
		this(codeToUse, additionalTextToUse, "", providerToUse);
	}
	
	public ChoiceItemWithChildren(String codeToUse, String labelToUse, String descriptionToUse)
	{
		this(codeToUse, labelToUse, EAM.emptyText(), new StaticLongDescriptionProvider(descriptionToUse));
	}
	
	public ChoiceItemWithChildren(String codeToUse, String labelToUse, String additionalTextToUse, AbstractLongDescriptionProvider providerToUse)
	{
		super(codeToUse, labelToUse, additionalTextToUse, providerToUse);
		
		children = new Vector<ChoiceItem>();
		setSelectable(false);
	}

	public void addChild(ChoiceItem childChoiceItem)
	{
		children.add(childChoiceItem);
	}
	
	@Override
	public int getChildrenCount()
	{
		return children.size();
	}
	
	@Override
	public ChoiceItem getChild(int childIndex)
	{
		return children.elementAt(childIndex);
	}

	@Override
	public Vector<ChoiceItem> getChildren()
	{
		return new Vector<ChoiceItem>(children);
	}
	
	private Vector<ChoiceItem> children;
}
