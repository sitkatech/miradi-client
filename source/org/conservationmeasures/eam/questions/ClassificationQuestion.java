/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;
import java.util.Vector;

import org.conservationmeasures.eam.objecthelpers.TaxonomyItem;
import org.conservationmeasures.eam.objecthelpers.TaxonomyLoader;

public abstract class ClassificationQuestion extends ChoiceQuestion
{
	public ClassificationQuestion(String tag)
	{
		super(tag, "Taxonomy Classifications", null);
		setChoiceItems(getImpactChoices());
	}
	
	public abstract String getTaxonomyFile();
	
	private ChoiceItem[] getImpactChoices()
	{
		try 
		{
			Vector chocies = new Vector();
			TaxonomyItem[] taxonomyItems = TaxonomyLoader.load(getTaxonomyFile());

			for (int i=0; i<taxonomyItems.length; ++i)
			{

				ChoiceItem choice = new ChoiceItem(
						taxonomyItems[i].getTaxonomyCode(), 
						taxonomyItems[i].getTaxonomyDescription());
				choice.setSelectable(taxonomyItems[i].isLeaf());
				chocies.add(choice);
			}
			
			return (ChoiceItem[])chocies.toArray(new ChoiceItem[0]);
		}
		catch (Exception e)
		{
			ChoiceItem[] choices = {new ChoiceItem("", "error processing classifications", Color.WHITE), };
			return choices;
		}
	}
}
