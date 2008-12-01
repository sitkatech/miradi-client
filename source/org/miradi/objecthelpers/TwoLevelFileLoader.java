/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.objecthelpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Vector;

import org.martus.util.UnicodeReader;
import org.miradi.main.ResourcesHandler;
import org.miradi.utils.DelimitedFileLoader;
import org.miradi.utils.Translation;

abstract public class TwoLevelFileLoader extends DelimitedFileLoader
{
	public TwoLevelFileLoader(String fileNameToUse)
	{
		fileName = fileNameToUse;
	}
	
	public TwoLevelEntry[] load() throws Exception
	{
		URL english = ResourcesHandler.getEnglishResourceURL("fieldoptions/" + fileName);
		Reader reader = new UnicodeReader(english.openStream());
		try
		{
			TwoLevelEntry[] table = getTaxomonies(reader);
			return table;
		}
		finally
		{
			reader.close();
		}
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
	
	private TwoLevelEntry[] getTaxomonies(Reader reader) throws IOException
	{
		Vector<Vector<String>> fileVector = getDelimitedContents(reader);
		Vector<TwoLevelEntry> taxonomyItems = processVector(fileVector);
		return taxonomyItems.toArray(new TwoLevelEntry[0]);
	}
	
	@Override
	protected String translateLine(String line)
	{
		return Translation.translateTabDelimited("choice|" + getFileName() + "|", line);
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	abstract protected Vector<TwoLevelEntry> processVector(Vector<Vector<String>> fileVector);

	private String fileName;
	public final static String COUNTRIES_FILE = "Countries.tsv";
	public final static String WWF_ECO_REGIONS_FILE = "EcoRegions.tsv";
	public final static String STRATEGY_TAXONOMIES_FILE = "StrategyTaxonomies.tsv";
	public final static String THREAT_TAXONOMIES_FILE = "ThreatTaxonomies.tsv";
	
	public final static String TNC_FRESHWATER_ECO_REGION_FILE = "TncFreshwaterEcoRegions.tsv";
	public final static String TNC_MARINE_ECO_REGION_FILE = "TncMarineEcoRegions.tsv";
	public final static String TNC_OPERATING_UNITS_FILE = "TncOperatingUnits.tsv";
	public final static String TNC_TERRESTRIAL_ECO_REGION_FILE = "TncTerrestrialEcoRegions.tsv";

	public final static String WWF_LINK_TO_GLOBAL_TARGETS = "WwfLinkToGlobalTargets.tsv";
	public final static String WWF_MANAGING_OFFICES_FILE = "WwfManagingOffices.tsv";
	public final static String WWF_REGIONS_FILE = "WwfRegions.tsv";
}
