/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objecthelpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.utils.DelimitedFileLoader;

abstract public class TwoLevelFileLoader
{
	public TwoLevelFileLoader(String fileNameToUse)
	{
		fileName = fileNameToUse;
	}
	
	public TwoLevelEntry[] load() throws Exception
	{
		InputStream is = EAM.class.getResourceAsStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		TwoLevelEntry[] table = getTaxomonies(reader);
		reader.close();
		return table;
	}

	public TwoLevelEntry[] load(InputStream is) throws Exception
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		TwoLevelEntry[] table = getTaxomonies(reader);
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
	public final static String STRATEGY_TAXONOMIES_FILE = "StrategyTaxonomies.txt";
	public final static String THREAT_TAXONOMIES_FILE = "ThreatTaxonomies.txt";
	public final static String WWF_MANAGING_OFFICES_FILE = "WwfManagingOffices.txt";
	public final static String WWF_REGIONS_FILE = "WwfRegions.txt";
	public final static String COUNTRIES_FILE = "Countries.txt";
	public final static String WWF_ECO_REGIONS_FILE = "EcoRegions.txt";
	public final static String WWF_LINK_TO_GLOBAL_TARGETS = "WwfLinkToGlobalTargets.txt";
	public final static String TNC_OPERATING_UNITS_FILE = "TncOperatingUnits.txt";
	public final static String TNC_TERRESTRIAL_ECO_REGION_FILE = "TncTerrestrialEcoRegions.txt";
	public final static String TNC_MARINE_ECO_REGION_FILE = "TncMarineEcoRegions.txt";
	public final static String TNC_FRESHWATER_ECO_REGION_FILE = "TncFreshwaterEcoRegions.txt";
	public final static String COLORS_FILE = "colors.txt";
}
