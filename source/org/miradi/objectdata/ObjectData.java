/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.objectdata;

import java.util.HashSet;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.questions.ChoiceQuestion;


abstract public class ObjectData
{
	public ObjectData(String tagToUse)
	{
		this(tagToUse, new HashSet<String>());
	}
	
	public ObjectData(String tagToUse, HashSet<String> dependencyTagsToUse)
	{
		tag = tagToUse;
		dependencyTags = dependencyTagsToUse;
	}
	
	abstract public void set(String newValue) throws Exception;
	abstract public String get();
	abstract public boolean equals(Object rawOther);
	abstract public int hashCode();
	
	final public String toString()
	{
		return get();
	}
	
	final public boolean isEmpty()
	{
		return toString().length() == 0;
	}
	
	public boolean isPseudoField()
	{
		return false;
	}
	
	public boolean isIdListData()
	{
		return false;
	}
	
	public boolean isChoiceItemData()
	{
		return false;
	}
	
	public boolean isCodeListData()
	{
		return false;
	}
	
	public boolean isStringMapData()
	{
		return false;
	}
	
	public ChoiceQuestion getChoiceQuestion()
	{
		return null;
	}
	
	public ORef getRef()
	{
		throw new RuntimeException("Called getRawRef on " + getClass().getSimpleName());
	}
	
	public ORefList getRefList()
	{
		return new ORefList();
	}
	
	public String getTag()
	{
		return tag;
	}
	
	public HashSet<String> getDependencyTags()
	{
		return dependencyTags;
	}
	
	private HashSet<String> dependencyTags;
	private String tag;
}
