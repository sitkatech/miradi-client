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
	
	public Vector<RawObjectVisitor> createRawObjectForwardMigrationVisitors()
	{
		return new Vector<RawObjectVisitor>();
	}
	
	public Vector<RawObjectVisitor> createRawObjectReverseMigrationVisitors()
	{
		return new Vector<RawObjectVisitor>();
	}
	
	public void forwardMigrateIfPossible() throws Exception
	{
		final Vector<RawObjectVisitor> rawObjectForwardMigrationVisitors = createRawObjectForwardMigrationVisitors();
		final VersionRange postMigrationVersionRange = getPostForwardMigrationVersionRange();

		if (canMigrateThisVersion(getRawProject().getCurrentVersionRange()))
			migrate(rawObjectForwardMigrationVisitors, postMigrationVersionRange);
	}
	
	public void reverseMigrateIfPossible() throws Exception
	{
		final Vector<RawObjectVisitor> rawObjectReverseMigrationVisitors = createRawObjectReverseMigrationVisitors();
		final VersionRange postMigrationVersionRange = getPostReverseMigrationVersionRange();
		
		if (canReverseMigrateThisVersion(getRawProject().getCurrentVersionRange()))
			migrate(rawObjectReverseMigrationVisitors, postMigrationVersionRange);
	}

	private void migrate(final Vector<RawObjectVisitor> rawObjectVisitors, final VersionRange postMigrationVersionRange) throws Exception
	{
		getRawProject().visitAllObjectsInPool(rawObjectVisitors);
		getRawProject().setCurrentVersionRange(postMigrationVersionRange);
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
	
	private RawProject rawProject;
}
