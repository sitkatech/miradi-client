/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.util.Vector;

import org.conservationmeasures.eam.objecthelpers.TwoLevelEntry;
import org.conservationmeasures.eam.objecthelpers.TwoLevelFileLoader;

public class TwoLevelQuestion extends ChoiceQuestion
{
	public TwoLevelQuestion(String tagToUse, String labelToUse,	String fileNameToUse)
	{
		super(tagToUse, labelToUse, getTwoLevelChoices(fileNameToUse));
	}
	
	private static ChoiceItem[] getTwoLevelChoices(String fileName)
	{
		try 
		{
			Vector chocies = new Vector();
			TwoLevelEntry[] twoLevelEntry = new TwoLevelFileLoader().load(fileName);

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
			throw new RuntimeException("error processing two level entry inside:" + fileName);
		}
	}
}
