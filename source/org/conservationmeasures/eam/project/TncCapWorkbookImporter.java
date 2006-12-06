package org.conservationmeasures.eam.project;
/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
import java.io.File;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


public class TncCapWorkbookImporter
{
	public TncCapWorkbookImporter(String fileNameToUse) throws Exception 
	{
		fileName = fileNameToUse;
		workbook = Workbook.getWorkbook(new File(fileName)); 
		if (!isValidVersion()) 
			throw new Exception("Invalid TNC CAP File Version");
	}
	

	private boolean isValidVersion()
	{
		return (getProjectVersion().equals(VER1));
	}
	
	
	public String getProjectVersion()
	{
		return getProjectCell(EXPECTED_WKBK_VERSION_COL);
	}
	
	
	public String getProjectVersionDate()
	{
		return getProjectCell(EXPECTED_WKBK_VERSION_DATE_COL);
	}
	
	
	public String getProjectDownloadDate()
	{
		return getProjectCell(EXPECTED_DOWNLOAD_DATE_COL);
	}
	
	
	public String getProjectLessonsLearned()
	{
		return getProjectCell(EXPECTED_PROJECT_LESSONS_LEARNED_COL);
	}
	
	
	public String getProjectStartDate()
	{
		return getProjectCell(EXPECTED_PROJECT_START_DATE_COL);
	}
	
	
	public String getPlanningTeamComment()
	{
		return getProjectCell(EXPECTED_PLANNING_TEAM_COMMENT_COL);
	}
	
	
	public String getProjectDataEffectiveDate()
	{
		return getProjectCell(EXPECTED_DATA_EFFECTIVE_DATE_COL);
	}
	
	
	public String getProjectSize()
	{
		return getProjectCell(EXPECTED_PROJECT_AREA_SIZE_HECTARES_COL);
	}
	
	
	public String getProjectVision()
	{
		return getProjectCell(EXPECTED_PROJECT_GOAL_COMMENT_COL);
	}
	
	
	public String getProjectScopeFull()
	{
		return getProjectCell(EXPECTED_PROJECT_DESCR_COMMENT_COL);
	}
	

	private String getProjectCell(int column)
	{
		Sheet sheet = workbook.getSheet(PROJECT_SHEET_NUMBER);
		Cell cell = sheet.getCell(column, EXPECTED_ROW);
		if (cell==null) 
			return "";
		return cell.getContents();
	}
	
	private String fileName;
	private Workbook workbook;
	
	final static String VER1 = "CAP_v5a.xls";
	
	final static int PROJECT_SHEET_NUMBER = 0;
	final static int EXPECTED_ROW = 6;
	

	final static int EXPECTED_PROJECT_START_DATE_COL = 7;
	final static int EXPECTED_DATA_EFFECTIVE_DATE_COL = 8;
	final static int EXPECTED_PROJECT_AREA_SIZE_HECTARES_COL = 11;
	final static int EXPECTED_PROJECT_DESCR_COMMENT_COL = 12;
	final static int EXPECTED_PROJECT_GOAL_COMMENT_COL = 13;
	final static int EXPECTED_PLANNING_TEAM_COMMENT_COL = 14;
	final static int EXPECTED_PROJECT_LESSONS_LEARNED_COL = 15;
	final static int EXPECTED_DOWNLOAD_DATE_COL = 33;
	final static int EXPECTED_WKBK_VERSION_COL = 28;
	final static int EXPECTED_WKBK_VERSION_DATE_COL = 29;
}
