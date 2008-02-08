/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.reports;

public class Report
{
	public Report(String reportNameToUse, String fileNameToUse)
	{
		reportName = reportNameToUse;
		fileName = fileNameToUse;
	}
	
	public String getFileName()
	{
		return fileName;
	}

	@Override
	public String toString()
	{
		return reportName;
	}
	
	private String fileName;
	private String reportName;
}
