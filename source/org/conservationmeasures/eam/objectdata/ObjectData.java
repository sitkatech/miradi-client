/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objectdata;


abstract public class ObjectData
{
	abstract public void set(String newValue) throws Exception;
	abstract public String get();
	abstract public boolean equals(Object rawOther);
	abstract public int hashCode();
	
	final public String toString()
	{
		return get();
	}
}
