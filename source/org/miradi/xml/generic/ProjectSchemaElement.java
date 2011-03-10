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

import org.miradi.xml.wcs.XmpzXmlConstants;


class ProjectSchemaElement extends SchemaElement
{
	public ProjectSchemaElement()
	{
		schemaElements= new Vector<ObjectSchemaElement>();
		
		schemaElements.add(new ProjectSummarySchemaElement());
		schemaElements.add(new ProjectSummaryScopeSchemaElement());
		schemaElements.add(new ProjectSummaryLocationSchemaElement());
		schemaElements.add(new ProjectSummaryPlanningSchemaElement());
		schemaElements.add(new ObjectPoolSchemaElement(new ProjectResourceObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new OrganizationObjectSchemaElement()));
		schemaElements.add(new TncProjectDataSchemaElement());
		schemaElements.add(new WwfProjectDataSchemaElement());
		schemaElements.add(new WcsDataSchemaElement());
		schemaElements.add(new RareProjectDataSchemaElement());
		schemaElements.add(new FosProjectDataSchemaElement());
		schemaElements.add(new ObjectPoolSchemaElement(new ConceptualModelSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new ResultsChainSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new DiagramFactorSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new DiagramLinkSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new BiodiversityTargetObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new HumanWelfareTargetSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new CauseObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new StrategyObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new ThreatReductionResultsObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new IntermediateResultObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new GroupBoxObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new TextBoxObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new ScopeBoxObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new KeyEcologicalAttributeObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new StressObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new SubTargetObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new GoalObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new ObjectiveSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new IndicatorObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new TaskObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new ProgressReportObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new ProgressPercentObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new MeasurementObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new AccountingCodeObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new FundingSourceObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new BudgetCategoryOneObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new BudgetCategoryTwoObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new ExpenseAssignmentObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new ResourceAssignmentObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new ThreatTargetThreatRatingElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new IucnRedListSpeciesObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new OtherNotableSpeciesObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new AudienceObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new ObjectTreeTableConfigurationSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new DashboardObjectSchemaElement()));
		schemaElements.add(new ObjectPoolSchemaElement(new TaggedObjectSetSchemaElement()));
	}
	
	@Override
	public void output(SchemaWriter writer) throws IOException
	{
		writer.defineAlias(getDotElement(getProjectElementName()), "element " + XmpzXmlConstants.PREFIX + getProjectElementName());
		writer.startBlock();
		for(int i = 0; i < schemaElements.size(); ++i)
		{
			ObjectSchemaElement objectElement = schemaElements.get(i);
			writer.printIndented(getDotElement(objectElement.getObjectTypeName()));
			if (objectElement.isPool())
				writer.print(XmpzXmlConstants.OPTIONAL_ELEMENT);
				
			writer.println(" &");
		}
		writer.println("  element " + XmpzXmlConstants.PREFIX + DELETED_ORPHANS_ELEMENT_NAME +  "{ text }?");
		writer.endBlock();
		
		for(ObjectSchemaElement objectElement: schemaElements)
		{
			objectElement.output(writer);
		}
		
	}
	
	String getProjectElementName()
	{
		return XmpzXmlConstants.CONSERVATION_PROJECT;
	}

	private Vector<ObjectSchemaElement> schemaElements;
}
