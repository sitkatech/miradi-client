/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.DelimitedFileLoader;

public class TaxonomyLoader
{
	public static TaxonomyItem[] load(String resourceFileName) throws Exception
	{
		if (tablePreLoad.contains(resourceFileName))
			return (TaxonomyItem[])tablePreLoad.get(resourceFileName);
		
		InputStream is = EAM.class.getResourceAsStream(resourceFileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		TaxonomyItem[] table = getTaxomonies(reader);
		tablePreLoad.put(resourceFileName, table);
		reader.close();
		return table;
	}

	public static TaxonomyItem[] load(BufferedReader reader) throws Exception
	{
		return getTaxomonies(reader);
	}
	
	private static TaxonomyItem[] getTaxomonies(BufferedReader reader) throws IOException
	{
		Vector fileVector = DelimitedFileLoader.getDelimitedContents(reader);
		Vector taxonomyItems = processVector(fileVector);
		return (TaxonomyItem[]) taxonomyItems.toArray(new TaxonomyItem[0]);
	}
	
	private static Vector processVector(Vector fileVector)
	{
		Vector taxonomyItems = new Vector();
		taxonomyItems.add(new TaxonomyItem("", EAM
				.text("--Select a classification--")));

		String prevLevel1Code = "";
		int level1Index = 0;
		int level2Index = 0;
		int firstNonHeaderRow = 1;
		for(int vectorIndex = firstNonHeaderRow; vectorIndex < fileVector
				.size(); ++vectorIndex)
		{
			Vector row = (Vector) fileVector.get(vectorIndex);
			String code = (String) row.get(0);
			String level1Descriptor = (String) row.get(1);
			String level2Descriptor = (String) row.get(2);

			if(!getLevel1Code(code).equals(prevLevel1Code))
			{
				level2Index = 0;
				String taxonomyLevelText = ++level1Index + "   "+ level1Descriptor;
				taxonomyItems.add(new TaxonomyItem(getLevel1Code(code), taxonomyLevelText));
			}
			
			++level2Index;
			String taxonomyLevel2Text = "    " + level1Index + "."
					+ level2Index + "    " + level2Descriptor;
			taxonomyItems.add(new TaxonomyItem(code, taxonomyLevel2Text));

			prevLevel1Code = getLevel1Code(code);
		}
		return taxonomyItems;
	}

	private static String getLevel1Code(String code)
	{
		return code.substring(0, code.indexOf("."));
	}
	
	private static Hashtable tablePreLoad = new Hashtable();
	public final static String STRATEGY_TAXONOMIES_FILE = "StrategyTaxonomies.txt";
	public final static String THREAT_TAXONOMIES_FILE = "ThreatTaxonomies.txt";

}
