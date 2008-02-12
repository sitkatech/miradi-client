/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objecthelpers;

import java.util.Vector;

import org.miradi.main.EAM;

public class TaxonomyFileLoader extends TwoLevelFileLoader
{
	public TaxonomyFileLoader(String fileNameToUse)
	{
		super(fileNameToUse);
	}

	protected Vector processVector(Vector fileVector)
	{
		Vector taxonomyItems = new Vector();
		taxonomyItems.add(new TwoLevelEntry("", EAM.text("--Select a classification--")));

		String prevLevel1Code = "";
		int level1Index = 0;
		int level2Index = 0;
		int firstNonHeaderRow = 1;
		for(int i = firstNonHeaderRow; i < fileVector.size(); ++i)
		{
			Vector row = (Vector) fileVector.get(i);
			String code = (String) row.get(0);
			String level1Descriptor = (String) row.get(1);
			String level2Descriptor = (String) row.get(2);

			if(!getLevel1Code(code).equals(prevLevel1Code))
			{
				level2Index = 0;
				String taxonomyLevelText = ++level1Index + "   "+ level1Descriptor;
				taxonomyItems.add(new TwoLevelEntry(getLevel1Code(code), taxonomyLevelText));
			}
			
			++level2Index;
			String taxonomyLevel2Text = "    " + level1Index + "." + level2Index + "    " + level2Descriptor;
			taxonomyItems.add(new TwoLevelEntry(code, taxonomyLevel2Text));

			prevLevel1Code = getLevel1Code(code);
		}
		return taxonomyItems;
	}

	private String getLevel1Code(String code)
	{
		return code.substring(0, code.indexOf("."));
	}
}
