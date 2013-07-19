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

package org.miradi.migrations;

import java.util.Vector;


abstract public class AbstractForwardMigration
{
	public AbstractForwardMigration(RawProject rawProjectToUse)
	{
		rawProject = rawProjectToUse;
	}

	public boolean canMigrateThisVersion(VersionRange versionRange) throws Exception
	{
		return getMigratableVersionRange().doesContainHigh(versionRange.getHighVersion());
	}

	protected RawProject getRawProject()
	{
		return rawProject;
	}
	
	public Vector<RawObjectVisitor> createRawObjectVisitors()
	{
		return new Vector<RawObjectVisitor>();
	}
	
	public Vector<RawObjectVisitor> createRawObjectReverseMigrationVisitors()
	{
		return new Vector<RawObjectVisitor>();
	}
	
	public void possiblyMigrateForward() throws Exception
	{
		if (canMigrateThisVersion(getRawProject().getCurrentVersionRange()))
		{
			getRawProject().visitAllObjectsInPool(getTypeToMigrate(), createRawObjectVisitors());
			
			final VersionRange incrementedByOne = getMigratableVersionRange().incrementByOne();
			getRawProject().setCurrentVersionRange(incrementedByOne);
		}
	}
	
	public void possibleMigrateReverse() throws Exception
	{
		if (canMigrateThisVersion(getRawProject().getCurrentVersionRange()))
		{
			getRawProject().visitAllObjectsInPool(getTypeToMigrate(), createRawObjectReverseMigrationVisitors());
			
			final VersionRange incrementedByOne = getMigratableVersionRange().decrementByOne();
			getRawProject().setCurrentVersionRange(incrementedByOne);
		}
	}
	
	abstract public VersionRange getMigratableVersionRange() throws Exception;

	abstract public int getTypeToMigrate();
	
	private RawProject rawProject;
}
