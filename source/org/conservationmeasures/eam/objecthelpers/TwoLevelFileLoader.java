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

abstract public class TwoLevelFileLoader
{
	public TwoLevelFileLoader(String fileNameToUse)
	{
		fileName = fileNameToUse;
	}
	
	public TwoLevelEntry[] load() throws Exception
	{
		if (tablePreLoad.contains(fileName))
			return (TwoLevelEntry[])tablePreLoad.get(fileName);
		
		InputStream is = EAM.class.getResourceAsStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		TwoLevelEntry[] table = getTaxomonies(reader);
		tablePreLoad.put(fileName, table);
		reader.close();
		return table;
	}

	public TwoLevelEntry[] load(BufferedReader reader) throws Exception
	{
		return getTaxomonies(reader);
	}
	
	private TwoLevelEntry[] getTaxomonies(BufferedReader reader) throws IOException
	{
		Vector fileVector = DelimitedFileLoader.getDelimitedContents(reader);
		Vector taxonomyItems = processVector(fileVector);
		return (TwoLevelEntry[]) taxonomyItems.toArray(new TwoLevelEntry[0]);
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	abstract protected Vector processVector(Vector fileVector);

	private String fileName;
	private Hashtable tablePreLoad = new Hashtable();
	public final static String STRATEGY_TAXONOMIES_FILE = "StrategyTaxonomies.txt";
	public final static String THREAT_TAXONOMIES_FILE = "ThreatTaxonomies.txt";
	public final static String WWF_MANAGING_OFFICES_FILE = "WwfManagingOffices.txt";
	public final static String WWF_REGIONS_FILE = "WwfRegions.txt";
	public final static String COUNTRIES_FILE = "Countries.txt";
	public final static String WWF_ECO_REGIONS_FILE = "EcoRegions.txt";
	public final static String WWF_LINK_TO_GLOBAL_TARGETS = "WwfLinkToGlobalTargets.txt";
}
