/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;
public class Definition
{
	public Definition(String key, String termToUse, String defintionToUse)
	{
		definition = defintionToUse;
		term = termToUse;
		DefinitionCommonTerms.definitions.put(key, this);
	}
	
	public String term;
	public String definition;
}
