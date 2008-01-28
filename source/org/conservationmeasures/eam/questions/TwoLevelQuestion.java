/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.objecthelpers.TwoLevelEntry;
import org.conservationmeasures.eam.objecthelpers.TwoLevelFileLoader;

public class TwoLevelQuestion extends DynamicChoiceQuestion
{
	public TwoLevelQuestion(String tagToUse, String labelToUse,	TwoLevelFileLoader twoLevelFileLoaderToUse)
	{
		super(tagToUse, labelToUse);
		
		twoLevelFileLoader = twoLevelFileLoaderToUse;
		choices = loadChoices();
		twoLevelFileLoader = null;
	}
	
	private ChoiceItem[] loadChoices()
	{
		try 
		{
			Vector chocies = new Vector();
			TwoLevelEntry[] twoLevelEntry = twoLevelFileLoader.load();

			for (int i = 0; i < twoLevelEntry.length; ++i)
			{
				ChoiceItem choice = new ChoiceItem(twoLevelEntry[i].getEntryCode(), twoLevelEntry[i].getEntryDescription());
				choice.setSelectable(twoLevelEntry[i].isLeaf());
				chocies.add(choice);
			}
			
			return (ChoiceItem[])chocies.toArray(new ChoiceItem[0]);
		}
		catch (Exception e)
		{
			throw new RuntimeException("error processing two level entry inside:" + twoLevelFileLoader.getFileName());
		}
	}
	
	public void sortChoices()
	{
		Arrays.sort(getChoices());
	}
	
	public ChoiceItem[] getChoices()
	{
		return choices;
	}
	
	private ChoiceItem[] choices;
	private TwoLevelFileLoader twoLevelFileLoader;
}
