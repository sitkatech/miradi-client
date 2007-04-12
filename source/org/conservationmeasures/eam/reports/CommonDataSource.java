/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;
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
	
	public Object getValue(JRField field, BaseObject baseObject)
	{
		if (isLabelFiedl(field.getName()))
			return translateToLabel(baseObject.getType(), field.getName());
		return baseObject.getData(field.getName());
	}
	
	public boolean isLabelFiedl(String name)
	{
		return (name.startsWith(LABEL_PREFIX));
	}
	
	public String translateToLabel(int objectType, String name)
	{
		return EAM.fieldLabel(objectType, name.substring(LABEL_PREFIX.length()));
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

	private static String LABEL_PREFIX = "Label:";
	
	private int currentRow;
	private int rowCount;
	public Project project;
}
