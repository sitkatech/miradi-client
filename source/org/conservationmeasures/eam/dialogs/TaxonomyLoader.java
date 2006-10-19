/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.io.BufferedReader;
import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.DelimitedFileLoader;

public class TaxonomyLoader
{

	public static TaxonomyItem[] load(BufferedReader reader)
			throws Exception
	{
		Vector fileVector = DelimitedFileLoader
		.getDelimitedContents(reader);
		
		Vector taxonomyItems = processVector(fileVector);
		return (TaxonomyItem[]) taxonomyItems.toArray(new TaxonomyItem[0]);
	}

	public static TaxonomyItem[] load(String taxonomyClassification)
			throws Exception
	{

		Vector fileVector = DelimitedFileLoader
		.getDelimitedContents(taxonomyClassification);

		Vector taxonomyItems = processVector(fileVector);
		return (TaxonomyItem[]) taxonomyItems.toArray(new TaxonomyItem[0]);
	}


	private static Vector processVector(Vector fileVector)
	{
		Vector taxonomyItems = new Vector();
		taxonomyItems.add(new TaxonomyItem("combo box default text", EAM
				.text("--Select a classification--")));

		String prevLevel1Code = "";
		int level1Index = 0;
		int level2Index = 0;
		int fileVectorSize = fileVector.size();

		for(int vectorIndex = 1; fileVectorSize > vectorIndex; vectorIndex++)
		{
			Vector row = (Vector) fileVector.get(vectorIndex);
			String code = (String) row.get(0);
			String level1Descriptor = (String) row.get(1);
			String level2Descriptor = (String) row.get(2);

			if(getLevel1Code(code).equals(prevLevel1Code))
			{
				taxonomyItems.add(new TaxonomyItem(code, "    " + level1Index
						+ "." + ++level2Index + "    " + level2Descriptor));
			}
			else
			{
				level2Index = 1;
				taxonomyItems.add(new TaxonomyItem(code, ++level1Index + "   "
						+ level1Descriptor));
				taxonomyItems.add(new TaxonomyItem(code, "    " + level1Index
						+ "." + level2Index + "    " + level2Descriptor));
			}

			prevLevel1Code = getLevel1Code(code);
		}
		return taxonomyItems;
	}

	private static String getLevel1Code(String code)
	{
		return code.substring(0, 3);
	}

}
