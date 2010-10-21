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
package org.miradi.questions;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.TwoLevelEntry;
import org.miradi.objecthelpers.TwoLevelFileLoader;

public class TwoLevelQuestion extends DynamicChoiceQuestion
{
	public TwoLevelQuestion(TwoLevelFileLoader twoLevelFileLoaderToUse)
	{
		super();
		
		twoLevelFileLoader = twoLevelFileLoaderToUse;
		choices = new Vector<ChoiceItem>(Arrays.asList(loadChoices()));
		twoLevelFileLoader = null;
	}
	
	private ChoiceItem[] loadChoices()
	{
		try 
		{
			Vector<ChoiceItem> chocies = new Vector<ChoiceItem>();
			TwoLevelEntry[] twoLevelEntry = twoLevelFileLoader.load();

			for (int i = 0; i < twoLevelEntry.length; ++i)
			{
				String code = twoLevelEntry[i].getEntryCode();
				String value = twoLevelEntry[i].getEntryDescription();
				ChoiceItem choice = new ChoiceItem(code, value);
				choice.setSelectable(twoLevelEntry[i].isLeaf());
				chocies.add(choice);
			}
			
			return chocies.toArray(new ChoiceItem[0]);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new RuntimeException("error processing two level entry inside:" + twoLevelFileLoader.getFileName());
		}
	}
	
	public void sortChoices()
	{
		Collections.sort(choices);
	}
	
	@Override
	public ChoiceItem[] getChoices()
	{
		return choices.toArray(new ChoiceItem[0]);
	}
	
	protected Vector<ChoiceItem> getRawChoices()
	{
		return choices;
	}
	
	private Vector<ChoiceItem> choices;
	private TwoLevelFileLoader twoLevelFileLoader;
}
