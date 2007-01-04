/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.util.Hashtable;

public class DefinitionCommonTerms
{
	
	static public void setTerms()
	{
		definitionHeaders.put("Definition:Goals",  "Goals");
		definitions.put("Definition:Goals",  
				"Goal -- A formal statement detailing a desired impact of a project.  " +
				"In conservation projects, it is the desired future status of a target." );
	}
	
	static public String getDefinition(String definitionKey)
	{
		if (definitions.size()==0) 
			setTerms();
		
		String text = (String) definitions.get(definitionKey);
		if (text==null)
			text = "";
		return text;
	}
	
	static public String getDefinitionHeader(String definitionKey)
	{
		if (definitions.size()==0) 
			setTerms();
		
		String text = (String) definitionHeaders.get(definitionKey);
		if (text==null)
			text = "Undefined";
		return "Definition: " + text;
	}
	
	
	static Hashtable definitions = new Hashtable();
	static Hashtable definitionHeaders = new Hashtable();
}
