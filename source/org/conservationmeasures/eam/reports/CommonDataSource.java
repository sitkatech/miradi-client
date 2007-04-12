/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.conservationmeasures.eam.project.Project;

abstract public class CommonDataSource implements JRDataSource
{
	public CommonDataSource(Project projectToUse)
	{
		setRowCount(1);
		setCurrentRow(-1);
		project = projectToUse;
	}
	
	abstract public Object getFieldValue(JRField field);
	
	public boolean next() throws JRException 
	{
		currentRow = currentRow + 1;
		return (currentRow < rowCount);
	}
	
	public int setCurrentRow(int row)
	{
		return currentRow = row;
	}

	public int getCurrentRow()
	{
		return currentRow;
	}

	public int setRowCount(int rows)
	{
		return rowCount = rows;
	}

	private int currentRow;
	private int rowCount;
	public Project project;
}
