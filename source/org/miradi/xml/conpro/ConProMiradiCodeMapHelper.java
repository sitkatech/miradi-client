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

import org.miradi.questions.KeyEcologicalAttributeTypeQuestion;
import org.miradi.questions.ProgressReportStatusQuestion;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.TncProjectSharingQuestion;
import org.miradi.questions.ViabilityModeQuestion;
import org.miradi.utils.ConproMiradiHabitatCodeMap;

public class ConProMiradiCodeMapHelper
{
	public ConProMiradiCodeMapHelper() throws Exception
	{
		createMiradiToConproCodeMaps();
	}
	
	private void createMiradiToConproCodeMaps() throws Exception
	{
		progressStatusMap = new HashMap<String, String>();
		progressStatusMap.put(ProgressReportStatusQuestion.PLANNED_CODE, CONPRO_STATUS_PLANNED_VALUE);
		progressStatusMap.put(ProgressReportStatusQuestion.MAJOR_ISSUES_CODE, CONPRO_STATUS_MAJOR_ISSUES_VALUE);
		progressStatusMap.put(ProgressReportStatusQuestion.MINOR_ISSUES_CODE, CONPRO_STATUS_MINOR_ISSUES_VALUE);
		progressStatusMap.put(ProgressReportStatusQuestion.ON_TRACK_CODE, CONPRO_STATUS_ON_TRACK_VALUE);
		progressStatusMap.put(ProgressReportStatusQuestion.COMPLETED_CODE, CONPRO_STATUS_COMPLETED_VALUE);
		progressStatusMap.put(ProgressReportStatusQuestion.ABANDONED_CODE, CONPRO_STATUS_ABANDONED_VALUE);
		
		rankingMap = new HashMap<String, String>();
		rankingMap.put("1", "Poor");
		rankingMap.put("2", "Fair"); 
		rankingMap.put("3", "Good");
		rankingMap.put("4", "Very Good");
		
		ratingMap = new HashMap<String, String>();
		ratingMap.put("1", "Low");
		ratingMap.put("2", "Medium"); 
		ratingMap.put("3", "High");
		ratingMap.put("4", "Very High"); 
	
		habitatCodeMap = new ConproMiradiHabitatCodeMap().loadMap();
		
		keaTypeMap = new HashMap<String, String>();
		keaTypeMap.put(KeyEcologicalAttributeTypeQuestion.SIZE, "Size");
		keaTypeMap.put(KeyEcologicalAttributeTypeQuestion.CONDITION, "Condition"); 
		keaTypeMap.put(KeyEcologicalAttributeTypeQuestion.LANDSCAPE, "Landscape Context");
		
		statusConfidenceMap = new HashMap<String, String>();
		statusConfidenceMap.put(StatusConfidenceQuestion.ROUGH_GUESS_CODE, "Rough Guess");
		statusConfidenceMap.put(StatusConfidenceQuestion.EXPERT_KNOWLEDGE_CODE, "Expert Knowledge"); 
		statusConfidenceMap.put(StatusConfidenceQuestion.RAPID_ASSESSMENT_CODE, "Rapid Assessment");
		statusConfidenceMap.put(StatusConfidenceQuestion.INTENSIVE_ASSESSMENT_CODE, "Intensive Assessment");
		
		viabilityModeMap = new HashMap<String, String>();
		viabilityModeMap.put(ViabilityModeQuestion.SIMPLE_MODE_CODE, CONPRO_TARGET_SIMPLE_MODE_VALUE);
		viabilityModeMap.put(ViabilityModeQuestion.TNC_STYLE_CODE, "kea");
		
		tncProjectSharingMap = new HashMap<String, String>();
		tncProjectSharingMap.put(TncProjectSharingQuestion.NO_CODE, "false");
		tncProjectSharingMap.put(TncProjectSharingQuestion.YES_CODE, "true");
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
	
	public HashMap<String, String> getConProToMiradiHabitiatCodeMap()
	{
		return reverseMap(habitatCodeMap);
	}
	
	public HashMap<String, String> getConProToMiradiRatingMap()
	{
		return reverseMap(ratingMap);
	}
	
	public HashMap<String, String> getConProToMiradiKeaTypeMap()
	{
		return reverseMap(keaTypeMap);
	}
	
	public HashMap<String, String> getConProToMiradiStatusConfidenceMap()
	{
		return reverseMap(statusConfidenceMap);
	}
	
	public HashMap<String, String> getConProToMiradiProgressStatusMap()
	{
		return reverseMap(progressStatusMap);
	}

	public HashMap<String, String> getConProToMiradiViabilityModeMap()
	{
		return reverseMap(viabilityModeMap);
	}
	
	public HashMap<String, String> getConProToMiradiTncProjectSharingMap()
	{
		return reverseMap(tncProjectSharingMap);
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
	
	public HashMap<String, String> getMiradiToConProHabitatCodeMap()
	{
		return habitatCodeMap;
	}
	
	public HashMap<String, String> getMiradiToConProRatingMap()
	{
		return ratingMap;
	}
	
	public HashMap<String, String> getMiradiToConProKeaTypeMap()
	{
		return keaTypeMap;
	}
	
	public HashMap<String, String> getMiradiToConProStatusConfidenceMap()
	{
		return statusConfidenceMap;
	}
	
	public HashMap<String, String> getMiradiToConProViabilityModeMap()
	{
		return viabilityModeMap;
	}
	
	public HashMap<String, String> getMiradiToConProTncProjectSharingMap()
	{
		return tncProjectSharingMap;
	}
	
	private HashMap<String, String> progressStatusMap;
	private HashMap<String, String> rankingMap;
	private HashMap<String, String> habitatCodeMap;
	private HashMap<String, String> ratingMap;
	private HashMap<String, String> keaTypeMap;
	private HashMap<String, String> statusConfidenceMap;
	private HashMap<String, String> viabilityModeMap;
	private HashMap<String, String> tncProjectSharingMap;
	
	private static final String CONPRO_STATUS_PLANNED_VALUE = "Planned";
	private static final String CONPRO_STATUS_MAJOR_ISSUES_VALUE = "Major Issues";
	private static final String CONPRO_STATUS_MINOR_ISSUES_VALUE = "Minor Issues";
	private static final String CONPRO_STATUS_ON_TRACK_VALUE = "On Track";
	private static final String CONPRO_STATUS_COMPLETED_VALUE = "Completed";
	private static final String CONPRO_STATUS_ABANDONED_VALUE = "Abandoned";
	
	public static final String CONPRO_TARGET_SIMPLE_MODE_VALUE = "simple";
}
