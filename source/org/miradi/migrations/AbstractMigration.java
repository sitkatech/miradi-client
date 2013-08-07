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



abstract public class AbstractMigration
{
	public AbstractMigration(RawProject rawProjectToUse)
	{
		rawProject = rawProjectToUse;
	}
	
	public MigrationResult forwardMigrateIfPossible() throws Exception
	{
		MigrationResult migrationResult = MigrationResult.createUninitializedResult();
		if (canForwardMigrateThisVersion(getRawProject().getCurrentVersionRange()))
		{
			migrationResult = migrateForward();
			updateProjectVersionRange(getPostForwardMigrationVersionRange());
		}
		
		return migrationResult;
	}

	public MigrationResult reverseMigrateIfPossible() throws Exception
	{
		MigrationResult migrationResult = MigrationResult.createUninitializedResult();
		if (canReverseMigrateThisVersion(getRawProject().getCurrentVersionRange()))
		{
			migrationResult = reverseMigrate();
			updateProjectVersionRange(getPostReverseMigrationVersionRange());
		}
		
		return migrationResult;
	}

	private boolean canForwardMigrateThisVersion(VersionRange versionRange) throws Exception
	{
		return getMigratableVersionRange().doesContain(versionRange.getHighVersion());
	}
	
	private boolean canReverseMigrateThisVersion(VersionRange versionRange) throws Exception
	{
		final VersionRange migratableVersionRange = new VersionRange(getToVersion());
		return migratableVersionRange.doesContain(versionRange.getLowVersion());
	}

	protected RawProject getRawProject()
	{
		return rawProject;
	}
	
	private void updateProjectVersionRange(final VersionRange postMigrationVersionRange)
	{
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
	
	protected void removeField(RawObject rawObject, final String fieldTag)
	{
		rawObject.remove(fieldTag);
	}
	
	protected void visitAllObjectsInPool(final RawObjectVisitor visitor) throws Exception
	{
		getRawProject().visitAllObjectsInPool(visitor);
	}
	
	abstract protected int getToVersion();
	
	abstract protected int getFromVersion(); 
	
	abstract protected VersionRange getMigratableVersionRange() throws Exception;
	
	abstract protected MigrationResult migrateForward() throws Exception;
	
	abstract protected MigrationResult reverseMigrate() throws Exception;
	
	private RawProject rawProject;
}
