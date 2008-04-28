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
package org.miradi.xml.conpro;

import java.util.HashMap;
import java.util.Set;

import org.miradi.questions.ProgressReportStatusQuestion;

public class ConProMiradiCodeMapHelper
{
	public ConProMiradiCodeMapHelper()
	{
		createMiradiToConproCodeMaps();
	}
	
	private void createMiradiToConproCodeMaps()
	{
		progressStatusMap = new HashMap<String, String>();
		progressStatusMap.put(ProgressReportStatusQuestion.PLANNED_CODE, CONPRO_STATUS_PLANNED_VALUE);
		progressStatusMap.put(ProgressReportStatusQuestion.ON_TRACK_CODE, CONPRO_STATUS_ON_TRACK_VALUE);
		
		rankingMap = new HashMap<String, String>();
		rankingMap.put("1", "Poor");
		rankingMap.put("2", "Fair"); 
		rankingMap.put("3", "Good");
		rankingMap.put("4", "Very Good"); 
	}
	
	public static String getSafeXmlCode(HashMap<String, String> map, String code)
	{
		String value = map.get(code);
		if (value == null)
			return "";
		
		return value.toString();
	}
	
	public HashMap<String, String> getConProToMiradiRankingMap()
	{
		return reverseMap(rankingMap);
	}
	
	public static HashMap<String, String> reverseMap(HashMap<String, String> map)
	{
		HashMap reversedMap = new HashMap<String, String>();
		Set<String> keys = map.keySet();
		for(String key : keys)
		{
			String value = map.get(key);
			reversedMap.put(value, key);
		}
		
		return reversedMap;
	}
	
	public HashMap<String, String> getMiradiToConProProgressStatusMap()
	{
		return progressStatusMap;
	}
	
	public HashMap<String, String> getMiradiToConProRankingMap()
	{
		return rankingMap;
	}
	
	private HashMap<String, String> progressStatusMap;
	private HashMap<String, String> rankingMap;
	
	private static final String CONPRO_STATUS_PLANNED_VALUE = "Planned";
	private static final String CONPRO_STATUS_ON_TRACK_VALUE = "On Track";
}
