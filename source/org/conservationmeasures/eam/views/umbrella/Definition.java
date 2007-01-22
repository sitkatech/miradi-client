/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;
public class Definition
{
	public Definition(String termToUse, String defintionToUse)
	{
		definition = defintionToUse;
		term = termToUse;
	}
	
	public String term;
	public String definition;
}
