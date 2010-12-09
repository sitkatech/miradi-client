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

import org.miradi.main.EAM;

abstract public class DynamicChoiceWithRootChoiceItem extends DynamicChoiceQuestion
{
	@Override
	public ChoiceItem[] getChoices()
	{
		try
		{
			return new ChoiceItem[]{ createHeaderChoiceItem(), };
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
			
			return new ChoiceItem[0];
		}
	}
	
	public ChoiceItem getHeaderChoiceItem() throws Exception
	{
		return createHeaderChoiceItem();
	}
	
	@Override
	public boolean hasAdditionalText()
	{
		return true;
	}
	
	@Override
	public boolean hasLongDescriptionProvider()
	{
		return true;
	}

	protected abstract ChoiceItemWithChildren createHeaderChoiceItem() throws Exception;
}
