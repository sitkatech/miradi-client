/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.util.Enumeration;
import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.DelimitedFileLoader;

public class LoadTaxonomies
{
	public static TaxonomyItem[] load(String taxonomyClassification)
			throws Exception
	{

		String prevCode = "       ";
		Vector fileVec = DelimitedFileLoader
				.getDelimitedContents(taxonomyClassification + ".txt");
		TaxonomyItem[] taxonomyItems = new TaxonomyItem[fileVec.size() * 2];
		Enumeration enum = fileVec.elements();
		enum.nextElement(); //skip header
		taxonomyItems[0] = new TaxonomyItem("combo box default text", 
				EAM.text("--Select a classification--"));
		int inc = 1;
		int incrMain = 0;
		int incrSub = 0;
		while(enum.hasMoreElements())
		{

			Vector row = (Vector) enum.nextElement();
			String code = (String) row.get(0);
			String descp_L1 = (String) row.get(1);
			String descp_L2 = (String) row.get(2);

			if(code.substring(0, 3).equals(prevCode))
			{
				taxonomyItems[inc++] = new TaxonomyItem(code, "    " + incrMain
						+ "." + ++incrSub + "    " + descp_L2);
			}
			else
			{
				incrSub = 1;
				taxonomyItems[inc++] = new TaxonomyItem(code, ++incrMain
						+ "   " + descp_L1);
				taxonomyItems[inc++] = new TaxonomyItem(code, "    " + incrMain
						+ "." + incrSub + "    " + descp_L2);
			}

			prevCode = code.substring(0, 3);

		}
		return taxonomyItems;
	}

}
