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

package org.miradi.xml.generic;

import java.io.IOException;
import java.util.Vector;

import org.miradi.xml.wcs.WcsXmlConstants;


class ProjectSchemaElement extends SchemaElement
{
	public ProjectSchemaElement()
	{
		objectTypes = new Vector<ObjectSchemaElement>();
		
		objectTypes.add(new ProjectSummarySchemaElement());
		objectTypes.add(new ObjectPoolSchemaElement(new ProjectResourceObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new OrganizationObjectSchemaElement()));
		objectTypes.add(new ProjectSummaryScopeSchemaElement());
		objectTypes.add(new ProjectSummaryLocationSchemaElement());
		objectTypes.add(new ProjectSummaryPlanningSchemaElement());

		objectTypes.add(new TncProjectDataSchemaElement());
		objectTypes.add(new WwfProjectDataSchemaElement());
		objectTypes.add(new WcsDataSchemaElement());
		objectTypes.add(new RareProjectDataSchemaElement());
		objectTypes.add(new FosProjectDataSchemaElement());
		
		objectTypes.add(new ObjectPoolSchemaElement(new ConceptualModelSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new ResultsChainSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new DiagramFactorSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new DiagramLinkSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new BiodiversityTargetObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new HumanWelfareTargetSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new CauseObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new StrategyObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new ThreatReductionResultsObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new IntermediateResultObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new GroupBoxObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new TextBoxObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new ScopeBoxObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new KeyEcologicalAttributeObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new StressObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new SubTargetObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new GoalObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new ObjectiveSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new IndicatorObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new ActivityObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new MethodObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new TaskObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new ProgressReportObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new ProgressPercentObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new MeasurementObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new AccountingCodeObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new FundingSourceObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new ExpenseAssignmentObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new ResourceAssignmentObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new ThreatTargetThreatRatingElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new IucnRedListSpeciesObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new OtherNotableSpeciesObjectSchemaElement()));
		objectTypes.add(new ObjectPoolSchemaElement(new AudienceObjectSchemaElement()));
	}
	
	public void output(SchemaWriter writer) throws IOException
	{
		writer.defineAlias(getDotElement(getProjectElementName()), "element " + WcsXmlConstants.PREFIX + getProjectElementName());
		writer.startBlock();
		for(int i = 0; i < objectTypes.size(); ++i)
		{
			ObjectSchemaElement objectElement = objectTypes.get(i);
			writer.printIndented(getDotElement(objectElement.getObjectTypeName()));
			if (objectElement.isPool())
				writer.print(WcsXmlConstants.OPTIONAL_ELEMENT);
				
			if(i < objectTypes.size() - 1)
				writer.print(" &");

			writer.println();
		}
		writer.endBlock();
		
		for(ObjectSchemaElement objectElement: objectTypes)
		{
			objectElement.output(writer);
		}
		
	}
	
	String getProjectElementName()
	{
		return WcsXmlConstants.CONSERVATION_PROJECT;
	}

	private Vector<ObjectSchemaElement> objectTypes;
}
