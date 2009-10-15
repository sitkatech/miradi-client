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
		objectTypes.add(new ObjectContainerSchemaElement(new ProjectResourceObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new OrganizationObjectSchemaElement()));
		objectTypes.add(new ProjectSummaryScopeSchemaElement());
		objectTypes.add(new ProjectSummaryLocationSchemaElement());
		objectTypes.add(new ProjectSummaryPlanningSchemaElement());

		objectTypes.add(new TncProjectDataSchemaElement());
		objectTypes.add(new WwfProjectDataSchemaElement());
		objectTypes.add(new WcsDataSchemaElement());
		objectTypes.add(new RareProjectDataSchemaElement());
		objectTypes.add(new FosProjectDataSchemaElement());
		
		objectTypes.add(new ObjectContainerSchemaElement(new ConceptualModelSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new ResultsChainSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new DiagramFactorSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new DiagramLinkSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new BiodiversityTargetObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new HumanWelfareTargetSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new CauseObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new StrategyObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new ThreatReductionResultsObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new IntermediateResultObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new GroupBoxObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new TextBoxObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new ScopeBoxObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new KeyEcologicalAttributeObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new StressObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new SubTargetObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new GoalObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new ObjectiveSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new IndicatorObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new ActivityObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new MethodObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new TaskObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new ProgressReportObjectSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new ProgressPercentObjectSchemaElement()));
		
//FIXME urgent - wcs - uncomment as each xml element is completed and validated		
//		objectTypes.add(new ObjectContainerSchemaElement(new ResourceAssignmentObjectSchemaElement()));
//		objectTypes.add(new ObjectContainerSchemaElement(new ExpenseAssignmentObjectSchemaElement()));
//		objectTypes.add(new ObjectContainerSchemaElement(new ThreatTargetThreatRatingElement()));
//		objectTypes.add(new ObjectContainerSchemaElement(new SimpleThreatRatingSchemaElement()));
//		objectTypes.add(new ObjectContainerSchemaElement(new StressBasedThreatRatingElement()));
//		objectTypes.add(new ObjectContainerSchemaElement(new MeasurementObjectSchemaElement()));
//		objectTypes.add(new ObjectContainerSchemaElement(new AccountingCodeObjectSchemaElement()));
//		objectTypes.add(new ObjectContainerSchemaElement(new FundingSourceObjectSchemaElement()));
	}
	
	public void output(SchemaWriter writer) throws IOException
	{
		writer.defineAlias(getDotElement(getProjectElementName()), "element " + WcsXmlConstants.PREFIX + getProjectElementName());
		writer.startBlock();
		for(int i = 0; i < objectTypes.size(); ++i)
		{
			ObjectSchemaElement objectElement = objectTypes.get(i);
			writer.printIndented(getDotElement(objectElement.getObjectTypeName()));
			if (objectElement.isContainer())
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
