/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.DelimitedFileLoader;

public class TaxonomyLoader
{
	public static TaxonomyItem[] load(String resourceFileName) throws Exception
	{

		InputStream is = EAM.class.getResourceAsStream(resourceFileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		Vector fileVector = DelimitedFileLoader.getDelimitedContents(reader);
		reader.close();

		Vector taxonomyItems = processVector(fileVector);
		return (TaxonomyItem[]) taxonomyItems.toArray(new TaxonomyItem[0]);
	}

	public static TaxonomyItem[] load(BufferedReader reader) throws Exception
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
				String taxonomyLevel2text = ++level1Index + "   "+ level1Descriptor;
				taxonomyItems.add(new TaxonomyItem(code, taxonomyLevel2text));
			}
			
			++level2Index;
			String taxonomyLevel1Text = "    " + level1Index + "."
					+ level2Index + "    " + level2Descriptor;
			taxonomyItems.add(new TaxonomyItem(code, taxonomyLevel1Text));

			prevLevel1Code = getLevel1Code(code);
		}
		return taxonomyItems;
	}

	private static String getLevel1Code(String code)
	{
		return code.substring(0, code.indexOf("."));
	}

}
