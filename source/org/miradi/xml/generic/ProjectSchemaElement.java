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


class ProjectSchemaElement extends SchemaElement
{
	public ProjectSchemaElement()
	{
		objectTypes = new Vector<ObjectSchemaElement>();
		
		objectTypes.add(new ProjectSummarySchemaElement());
		// ProjectResource
		// OtherOrganization
		objectTypes.add(new ProjectSummaryScopeSchemaElement());
		objectTypes.add(new ProjectSummaryLocationSchemaElement());
		objectTypes.add(new ProjectSummaryPlanningSchemaElement());

		// TNC
		// WWF
		objectTypes.add(new WcsDataSchemaElement());
		// RARE
		// FOS
		

		objectTypes.add(new ObjectContainerSchemaElement(new ConceptualModelSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new ResultsChainSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new DiagramFactorSchemaElement()));
		objectTypes.add(new ObjectContainerSchemaElement(new DiagramLinkSchemaElement()));
				
		objectTypes.add(new ObjectContainerSchemaElement(new BiodiversityTargetObjectSchemaElement()));
		// HWT
		objectTypes.add(new ObjectContainerSchemaElement(new CauseObjectSchemaElement()));
		// Strategy
		// TRR
		// IR
		// Group Box
		// Text Box
		// Scope Box
		
		// KEA
		// Stress
		// Subtarget
		// Goal
		// Objective
		objectTypes.add(new ObjectContainerSchemaElement(new IndicatorObjectSchemaElement()));
		
		// ThreatRating
		
		// Activity
		// Method
		// Task
		
		// ResourceAssignment
		// ExpenseAssignment
		
		// ProgressReport
		// ProgressPercent
		// Measurement
		
		// AccountingCode
		// FundingSource
		
		// ReportTemplate
		// PlanningCustomization
		// TaggedObjectSet
	}
	
	public void output(SchemaWriter writer) throws IOException
	{
		writer.defineAlias(getDotElement(getProjectElementName()), "element miradi:" + getProjectElementName());
		writer.startBlock();
		for(int i = 0; i < objectTypes.size(); ++i)
		{
			ObjectSchemaElement objectElement = objectTypes.get(i);
			writer.printIndented(getDotElement(objectElement.getObjectTypeName()));
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
		return "ConservationProject";
	}

	private Vector<ObjectSchemaElement> objectTypes;
}
