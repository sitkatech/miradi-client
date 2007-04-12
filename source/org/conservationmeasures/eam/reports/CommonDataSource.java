/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

abstract public class CommonDataSource implements JRDataSource
{
	public CommonDataSource()
	{
		rowCount=1;
	}
	
	abstract public Object getFieldValue(JRField field);
	
	public boolean next() throws JRException 
	{
		return (--rowCount>=0);
	}
	
	public int rowCount;
}
