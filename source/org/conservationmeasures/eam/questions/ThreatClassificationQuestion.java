/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;
import java.util.Vector;

import org.conservationmeasures.eam.dialogfields.ChoiceItem;
import org.conservationmeasures.eam.dialogfields.ChoiceQuestion;
import org.conservationmeasures.eam.objecthelpers.TaxonomyItem;
import org.conservationmeasures.eam.objecthelpers.TaxonomyLoader;

public class ThreatClassificationQuestion extends ChoiceQuestion
{
	public ThreatClassificationQuestion(String tag)
	{
		super(tag, "Taxonomy Classifications", getImpactChoices());
	}
	
	static ChoiceItem[] getImpactChoices()
	{
		
		try 
		{
			Vector chocies = new Vector();
			TaxonomyItem[] taxonomyItems = TaxonomyLoader.load(taxonomyFile);

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

	private static String taxonomyFile = TaxonomyLoader.THREAT_TAXONOMIES_FILE;
}
