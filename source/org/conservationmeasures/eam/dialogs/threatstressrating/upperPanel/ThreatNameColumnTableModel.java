/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.martus.util.xml.XmlUtilities;

public class ThreatNameColumnTableModel extends MainThreatTableModel
{
	public ThreatNameColumnTableModel(Project projectToUse)
	{
		super(projectToUse);
	}
	
	public int getColumnCount()
	{
		return 1;
	}
	
	public String getColumnName(int column)
	{
		return EAM.text("Threats");
	}
	
	public String getColumnTag(int column)
	{
		return "";
	}
	
	public Object getValueAt(int row, int column)
	{
		String encodedName = XmlUtilities.getXmlEncoded(getDirectThreat(row).toString());
		return "<HTML>" + encodedName + "</HTML>";
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getDirectThreat(row);
	}
}
