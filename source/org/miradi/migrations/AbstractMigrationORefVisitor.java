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

import org.miradi.objecthelpers.ORef;

abstract public class AbstractMigrationORefVisitor implements ORefVisitor
{
	public AbstractMigrationORefVisitor()
	{
		migrationResult = MigrationResult.createUninitializedResult();
	}
	
	public final void visit(ORef rawObjectRef) throws Exception
	{
		if (rawObjectRef.getObjectType() != getTypeToVisit())
			throw new Exception("Received incorrect object type for visitor, expecting:" + getTypeToVisit() + " but got type:" + rawObjectRef.getObjectType());
		
		migrationResult.merge(internalVisit(rawObjectRef));
	}
	
	public MigrationResult getMigrationResult()
	{
		return migrationResult;
	}
	
	abstract protected MigrationResult internalVisit(ORef rawObjectRef) throws Exception;

	private MigrationResult migrationResult;
}	
