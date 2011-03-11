/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.questions;

import java.awt.Color;

import org.miradi.dialogs.dashboard.AbstractLongDescriptionProvider;
import org.miradi.dialogs.dashboard.StaticLongDescriptionProvider;

public class ChoiceItemWithLongDescriptionProvider extends ChoiceItemWithAdditionalText
{	
	public ChoiceItemWithLongDescriptionProvider(String codeToUse, String labelToUse, Color colorToUse)
	{
		this(codeToUse, labelToUse, "", colorToUse, new StaticLongDescriptionProvider());
	}
	
	public ChoiceItemWithLongDescriptionProvider(String codeToUse, String labelToUse, String additionalTextToUse, Color colorToUse)
	{
		this(codeToUse, labelToUse, additionalTextToUse, colorToUse, new StaticLongDescriptionProvider());
	}
	
	public ChoiceItemWithLongDescriptionProvider(String codeToUse, String labelToUse)
	{
		this(codeToUse, labelToUse, "", new StaticLongDescriptionProvider());
	}

	public ChoiceItemWithLongDescriptionProvider(String codeToUse, String labelToUse, String additionalTextToUse)
	{
		this(codeToUse, labelToUse, additionalTextToUse, new StaticLongDescriptionProvider());
	}
	
	public ChoiceItemWithLongDescriptionProvider(String codeToUse, String labelToUse, AbstractLongDescriptionProvider providerToUse)
	{
		this(codeToUse, labelToUse, "", providerToUse);
	}
	
	public ChoiceItemWithLongDescriptionProvider(String codeToUse, String labelToUse, String additionalTextToUse, AbstractLongDescriptionProvider providerToUse)
	{
		this(codeToUse, labelToUse, additionalTextToUse, (Color)null, providerToUse);
	}
	
	public ChoiceItemWithLongDescriptionProvider(String codeToUse, String labelToUse, String additionalTextToUse, Color colorToUse, AbstractLongDescriptionProvider providerToUse)
	{
		super(codeToUse, labelToUse, additionalTextToUse, colorToUse);
		
		provider = providerToUse;
	}
	
	@Override
	public AbstractLongDescriptionProvider getLongDescriptionProvider()
	{
		return provider;
	}
	
	private AbstractLongDescriptionProvider provider;
}
