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


abstract public class AbstractMigration
{
	public AbstractMigration(RawProject rawProjectToUse)
	{
		rawProject = rawProjectToUse;
	}

	public boolean canMigrateThisVersion(VersionRange versionRange) throws Exception
	{
		return getMigratableVersionRange().doesContainHigh(versionRange.getHighVersion());
	}
	
	public boolean canReverseMigrateThisVersion(VersionRange versionRange) throws Exception
	{
		final VersionRange migratableVersionRange = getMigratableVersionRange();
		return versionRange.isEntirelyNewerThan(migratableVersionRange);
	}

	protected RawProject getRawProject()
	{
		return rawProject;
	}
	
	public Vector<AbstractMigrationVisitor> createRawObjectReverseMigrationVisitors()
	{
		return new Vector<AbstractMigrationVisitor>();
	}
	
	public void forwardMigrateIfPossible() throws Exception
	{
		if (canMigrateThisVersion(getRawProject().getCurrentVersionRange()))
		{
			migrateForward();
			final VersionRange postMigrationVersionRange = getPostForwardMigrationVersionRange();
			getRawProject().setCurrentVersionRange(postMigrationVersionRange);
		}
	}

	
	public void reverseMigrateIfPossible() throws Exception
	{
		if (canReverseMigrateThisVersion(getRawProject().getCurrentVersionRange()))
		{
			final Vector<AbstractMigrationVisitor> rawObjectReverseMigrationVisitors = createRawObjectReverseMigrationVisitors();
			getRawProject().visitAllObjectsInPool(rawObjectReverseMigrationVisitors);
			doPostMigrationCleanup();
			final VersionRange postMigrationVersionRange = getPostReverseMigrationVersionRange();
			getRawProject().setCurrentVersionRange(postMigrationVersionRange);
		}
	}
		
	protected void doPostMigrationCleanup() throws Exception
	{
	}

	private VersionRange getPostForwardMigrationVersionRange() throws Exception
	{
		return new VersionRange(getToVersion());
	}

	private VersionRange getPostReverseMigrationVersionRange() throws Exception
	{
		return new VersionRange(getFromVersion());
	}
	
	abstract protected int getToVersion();
	
	abstract protected int getFromVersion(); 
	
	abstract public VersionRange getMigratableVersionRange() throws Exception;
	
	abstract public void migrateForward() throws Exception;
	
	private RawProject rawProject;
}
