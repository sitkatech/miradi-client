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
		return getProjectCell(EXPECTED_WWKBK_VERSION_DATE_COL);
	}
	
	
	public String getProjectDownloadDate()
	{
		return getProjectCell(EXPECTED_DOWNLOAD_DATE_COL);
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
	
	final static int PROJECT_SHEET_NUMBER = 0;
	final static int EXPECTED_ROW = 6;
	
	final static int EXPECTED_WKBK_VERSION_COL = 28;
	
	final static int EXPECTED_WWKBK_VERSION_DATE_COL = 29;
	
	final static int EXPECTED_DOWNLOAD_DATE_COL = 23;
	
	final static String VER1 = "CAP_v5a.xls";
}
