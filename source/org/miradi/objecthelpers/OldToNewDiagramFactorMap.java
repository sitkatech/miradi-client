/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.objecthelpers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.miradi.objects.DiagramFactor;

public class OldToNewDiagramFactorMap extends HashMap<DiagramFactor, DiagramFactor>
{
	@Override
	public DiagramFactor put(DiagramFactor key, DiagramFactor value)
	{
		if (containsKey(key))
			throw new RuntimeException("Key DiagramFactor already exists in map. DF ref = " + key.getRef());
		
		return super.put(key, value);
	}
	
	@Override
	public void putAll(Map<? extends DiagramFactor, ? extends DiagramFactor> otherMap)
	{
		HashSet<DiagramFactor> keys = new HashSet<DiagramFactor>(keySet());
		HashSet<DiagramFactor> otherKeys = new HashSet<DiagramFactor>(otherMap.keySet());
		keys.retainAll(otherKeys);
		if (keys.size() > 0)
			throw new RuntimeException("Keys exist in both maps.");
		
		super.putAll(otherMap);
	}
}
