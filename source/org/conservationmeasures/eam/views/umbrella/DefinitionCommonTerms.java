/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.util.Hashtable;

public class DefinitionCommonTerms
{
    static
    {
		definitions = new Hashtable();
		new Definition("Undefined", "Undefined", "Undefined");
		new Definition("Definition:Goal", "Goal", 
			"Goal -- A formal statement detailing a desired impact of a project. " +
			"In conservation projects, it is the desired future status of a target." );
    }
	
    static public Definition getDefintion(String key)
    {
    	Definition def = (Definition)definitions.get(key);
    	if (def==null)
    		return (Definition)definitions.get("Undefined");
    	return def;
    }
    
	static Hashtable definitions = new Hashtable();
}


