package org.conservationmeasures.eam.project;

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
			throw new Exception("Invalid Tnc Cap File Version");
	}
	

	private boolean isValidVersion()
	{
		Sheet sheet = workbook.getSheet(PROJECT_SHEET_NUMBER);
		Cell cell = sheet.findLabelCell(EXPECTED_WKBK_VERSION_LABEL);
		
		if (cell==null)
			return false;
		
		if (cell.getColumn()!=EXPECTED_WKBK_VERSION_LABEL_COL)
			return false;
		
		if (cell.getRow()!=EXPECTED_LABEL_ROW)
			return false;
		
		String value = sheet.getCell(EXPECTED_WKBK_VERSION_LABEL_COL,EXPECTED_LABEL_ROW+1).getContents();
		if (!value.equals(VER1))
			return false;
		
		return true;
	}
	
	
	public String getProjectVersion()
	{
		return getProjectCell(EXPECTED_WKBK_VERSION_LABEL_COL);
	}
	
	
	public String getProjectVersionDate()
	{
		return getProjectCell(EXPECTED_WWKBK_VERSION_DATE_LABEL_COL);
	}
	
	
	public String getProjectDownloadDate()
	{
		return getProjectCell(EXPECTED_DOWNLOAD_DATE_LABEL_COL);
	}
	
	
	private String getProjectCell(int column)
	{
		Sheet sheet = workbook.getSheet(PROJECT_SHEET_NUMBER);
		return sheet.getCell(column, EXPECTED_LABEL_ROW+1).getContents();
	}
	
	private String fileName;
	private Workbook workbook;
	
	final static int PROJECT_SHEET_NUMBER = 0;
	final static int EXPECTED_LABEL_ROW = 5;
	
	final static String EXPECTED_WKBK_VERSION_LABEL = "WKBK_VERSION";
	final static int EXPECTED_WKBK_VERSION_LABEL_COL = 28;
	
	final static String EXPECTED_WKBK_VERSION_DATE_LABEL = "WKBK_VERSION_DATE";
	final static int EXPECTED_WWKBK_VERSION_DATE_LABEL_COL = 29;
	
	final static String EXPECTED_DOWNLOAD_DATE_LABEL = "WKBK_VERSION";
	final static int EXPECTED_DOWNLOAD_DATE_LABEL_COL = 23;
	
	final static String VER1 = "CAP_v5a.xls";
}
