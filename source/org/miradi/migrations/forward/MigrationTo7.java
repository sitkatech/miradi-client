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

package org.miradi.migrations.forward;

import java.util.Vector;

import org.miradi.migrations.AbstractMigration;
import org.miradi.migrations.AbstractMigrationVisitor;
import org.miradi.migrations.RawObject;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.VersionRange;
import org.miradi.objecthelpers.ObjectType;

public class MigrationTo7 extends AbstractMigration
{
	public MigrationTo7(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}

	@Override
	public void migrateForward() throws Exception
	{
	}

	@Override
	public VersionRange getMigratableVersionRange() throws Exception
	{
		return new VersionRange(VERSION_LOW, VERSION_HIGH);
	}
	
	@Override
	protected void doPostMigrationCleanup() throws Exception
	{
		getRawProject().deletePoolWithData(ObjectType.MIRADI_SHARE_PROJECT_DATA);
		getRawProject().deletePoolWithData(ObjectType.MIRADI_SHARE_TAXONOMY);
		getRawProject().deletePoolWithData(ObjectType.TAXONOMY_ASSOCIATION);

		super.doPostMigrationCleanup();
	}
	
	@Override
	public Vector<AbstractMigrationVisitor> createRawObjectReverseMigrationVisitors()
	{
		Vector<AbstractMigrationVisitor> visitors = super.createRawObjectReverseMigrationVisitors();
		Vector<Integer> typesWithTaxonomyClassifications = getTypesWithTaxonomyClassifications();
		for(Integer typeWithTaxonomy : typesWithTaxonomyClassifications)
		{
			visitors.add(new RemoveTaxonomyClassificationFieldVisitor(typeWithTaxonomy));	
		}
		
		return visitors;
	}
	
	private Vector<Integer> getTypesWithTaxonomyClassifications()
	{
		Vector<Integer> typesWithTaxonomyClassifications = new Vector<Integer>();
		typesWithTaxonomyClassifications.add(ObjectType.TARGET);
		typesWithTaxonomyClassifications.add(ObjectType.HUMAN_WELFARE_TARGET);
		typesWithTaxonomyClassifications.add(ObjectType.CAUSE);
		typesWithTaxonomyClassifications.add(ObjectType.OBJECTIVE);
		typesWithTaxonomyClassifications.add(ObjectType.GOAL);
		typesWithTaxonomyClassifications.add(ObjectType.INDICATOR);
		typesWithTaxonomyClassifications.add(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE);
		typesWithTaxonomyClassifications.add(ObjectType.MIRADI_SHARE_PROJECT_DATA);
		typesWithTaxonomyClassifications.add(ObjectType.RESULTS_CHAIN_DIAGRAM);
		typesWithTaxonomyClassifications.add(ObjectType.STRATEGY);
		typesWithTaxonomyClassifications.add(ObjectType.STRESS);
		typesWithTaxonomyClassifications.add(ObjectType.TASK);
		typesWithTaxonomyClassifications.add(ObjectType.THREAT_REDUCTION_RESULT);
		
		return typesWithTaxonomyClassifications;
	}
	
	@Override
	protected int getToVersion()
	{
		return 7;
	}
	
	@Override
	protected int getFromVersion() 
	{
		return 6;
	}
	
	private class RemoveTaxonomyClassificationFieldVisitor extends AbstractMigrationVisitor
	{
		public RemoveTaxonomyClassificationFieldVisitor(int typeToUse)
		{
			type = typeToUse;
		}

		public int getTypeToVisit()
		{
			return type;
		}

		@Override
		public void internalVisit(RawObject rawObject) throws Exception 
		{
			rawObject.remove(TAG_TAXONOMY_CLASSIFICATION_CONTAINER);
		}
		
		private int type;
	}
	
	private static final int VERSION_LOW = 6;
	private static final int VERSION_HIGH = 6;
	
	private static final String TAG_TAXONOMY_CLASSIFICATION_CONTAINER = "TaxonomyClassificationContainer";
}
