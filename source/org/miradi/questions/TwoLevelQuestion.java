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
import org.miradi.utils.XmlUtilities2;

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
			TwoLevelEntry[] twoLevelEntries = twoLevelFileLoader.load();

			for (int i = 0; i < twoLevelEntries.length; ++i)
			{
				final TwoLevelEntry twoLevelEntry = twoLevelEntries[i];
				String code = twoLevelEntry.getEntryCode();
				String label = getSafeEncodedValue(twoLevelEntry.getEntryLabel());
				String description  = getSafeEncodedValue(twoLevelEntry.getDescription());
				ChoiceItem choice = createChoiceItem(code, label, description, twoLevelEntry.getLongDescription());
				choice.setSelectable(twoLevelEntry.isSelectable());
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

	protected String getSafeEncodedValue(final String value)
	{
		if (XmlUtilities2.hasEncoded(value))
			throw new RuntimeException("Value in TwoLevelQuestion is already encoded, value= "+ value);
		
		return XmlUtilities2.getXmlEncoded(value);
	}

	protected ChoiceItem createChoiceItem(String code, String label, String description, String longDescription) throws Exception
	{
		return new ChoiceItem(code, label, description);
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
