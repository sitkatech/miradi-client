/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.mpfMigrations;

import org.martus.util.UnicodeReader;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.AbstractProjectLoader;

public class RawProjectLoader extends AbstractProjectLoader
{
	public static RawProject loadProject(final UnicodeReader reader) throws Exception
	{
		final RawProjectLoader projectLoader = new RawProjectLoader(reader);
		projectLoader.load();
		
		return projectLoader.getTypeToRawPoolMap();
	}
	
	private RawProjectLoader(UnicodeReader readerToUse) throws Exception
	{
		super(readerToUse);
	}
	
	@Override
	protected void prepareToLoad() throws Exception
	{
		rawProject = new RawProject();
	}
	
	@Override
	protected void createObject(ORef ref) throws Exception
	{
		RawPool pool = getRawPool(ref);
		pool.put(ref, new RawObject());
	}

	@Override
	protected void updateObjectWithData(ORef ref, String tag, String value)	throws Exception
	{
		RawPool pool = getRawPool(ref);
		RawObject rawObject = pool.get(ref);
		rawObject.put(tag, value);
	}
	
	private RawPool getRawPool(ORef ref)
	{
		if (!rawProject.containType(ref.getObjectType()))
			getTypeToRawPoolMap().putTypeToNewPoolEntry(ref.getObjectType(), new RawPool());
		
		return getTypeToRawPoolMap().getRawPoolForType(ref.getObjectType());
	}
	
	private RawProject getTypeToRawPoolMap()
	{
		return rawProject;
	}

	private RawProject rawProject;
}
