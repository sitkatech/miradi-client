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

import java.util.Set;

import org.miradi.objecthelpers.ORef;

abstract public class AbstractForwardMigration
{
	public AbstractForwardMigration(RawProject rawProjectToUse)
	{
		rawProject = rawProjectToUse;
	}

	public void forwardMigrate() throws Exception
	{
		visitAllObjectsInPool(getTypeToMigrate(), createRawObjectVisitor());
	}
	
	private void visitAllObjectsInPool(int objectType, RawObjectVisitor visitor)
	{
		if (!getRawProject().containsAnyObjectsOfType(objectType))
			return;
		
		RawPool rawPool = getRawProject().getRawPoolForType(objectType);
		Set<ORef> refs = rawPool.keySet();
		for(ORef ref : refs)
		{
			RawObject rawObject = rawPool.get(ref);
			visitor.visit(rawObject);
		}
	}
	
	public boolean canMigrateThisVersion(VersionRange versionRange) throws Exception
	{
		return getMigratableVersionRange().doesContainHigh(versionRange.getHighVersion());
	}

	protected RawProject getRawProject()
	{
		return rawProject;
	}
	
	abstract public VersionRange getMigratableVersionRange() throws Exception;

	abstract protected RawObjectVisitor createRawObjectVisitor();

	abstract protected int getTypeToMigrate();
	
	private RawProject rawProject;
}
