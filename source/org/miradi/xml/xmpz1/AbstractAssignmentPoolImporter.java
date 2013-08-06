/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz1;

import org.martus.util.MultiCalendar;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Assignment;
import org.miradi.schemas.BudgetCategoryOneSchema;
import org.miradi.schemas.BudgetCategoryTwoSchema;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.DoubleUtilities;
import org.miradi.xml.wcs.Xmpz1XmlConstants;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

abstract public class AbstractAssignmentPoolImporter extends AbstractBaseObjectPoolImporter
{
	public AbstractAssignmentPoolImporter(Xmpz1XmlImporter importerToUse, String poolNameToUse, int objectTypeToImportToUse)
	{
		super(importerToUse, poolNameToUse, objectTypeToImportToUse);
	}

	@Override
	protected void importFields(Node node, ORef destinationRef)	throws Exception
	{
		super.importFields(node, destinationRef);
		
		importOptionalRef(node, destinationRef, Assignment.TAG_CATEGORY_ONE_REF, Xmpz1XmlConstants.BUDGET_CATEGORY_ONE, BudgetCategoryOneSchema.getObjectType());
		importOptionalRef(node, destinationRef, Assignment.TAG_CATEGORY_TWO_REF, Xmpz1XmlConstants.BUDGET_CATEGORY_TWO, BudgetCategoryTwoSchema.getObjectType());
		importDateUnitEffortList(node, destinationRef);
	}

	private void importDateUnitEffortList(Node node, ORef destinationRef) throws Exception
	{
		Node dateUnitEffortsNode = getImporter().getNamedChildNode(node, getPoolName() + Assignment.TAG_DATEUNIT_EFFORTS);
		if (dateUnitEffortsNode == null)
			return;
		
		NodeList dateUnitEffortNodes = getImporter().getNodes(dateUnitEffortsNode, new String[]{getDateUnitsElementName(), });
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		for (int index = 0; index < dateUnitEffortNodes.getLength(); ++index)
		{
			Node dateUnitEffortNode = dateUnitEffortNodes.item(index);
			Node dateUnitNode = getImporter().getNamedChildNode(dateUnitEffortNode, getDateUnitElementName());
			DateUnit dateUnit = extractDateUnit(dateUnitNode);
			
			Node quantityNode = getImporter().getNamedChildNode(dateUnitEffortNode, getQuantatityElementName());
			String quantityAsString = quantityNode.getTextContent();
			DateUnitEffort dateUnitEffort = new DateUnitEffort(dateUnit, DoubleUtilities.toDoubleFromDataFormat(quantityAsString));
			dateUnitEffortList.add(dateUnitEffort);
		}
		
		getImporter().setData(destinationRef, Assignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toString());
	}

	private DateUnit extractDateUnit(Node dateUnitNode) throws Exception
	{
		Node timeSpanNode = getImporter().getNamedChildNode(dateUnitNode, getFullProjectTimespanElementName());
		if (timeSpanNode != null)
		{
			return new DateUnit();
		}
		
		Node yearNode = getImporter().getNamedChildNode(dateUnitNode, getYearElementName());
		if (yearNode != null)
		{
			int startYear = getAttributeAsInt(yearNode, Xmpz1XmlConstants.START_YEAR);
			int startMonth = getAttributeAsInt(yearNode, Xmpz1XmlConstants.START_MONTH);

			return DateUnit.createFiscalYear(startYear, startMonth);
		}
		
		Node quarterNode = getImporter().getNamedChildNode(dateUnitNode, getQuarterElementName());
		if (quarterNode != null)
		{
			int year = getAttributeAsInt(quarterNode, Xmpz1XmlConstants.YEAR);
			int startMonth = getAttributeAsInt(quarterNode, Xmpz1XmlConstants.START_MONTH);
			
			return DateUnit.createQuarterDateUnit(year, (startMonth-1)/3 + 1);
		}
			
		Node monthNode = getImporter().getNamedChildNode(dateUnitNode, getMonthElementName());
		if (monthNode != null)
		{
			int year = getAttributeAsInt(monthNode, Xmpz1XmlConstants.YEAR);
			int month = getAttributeAsInt(monthNode, Xmpz1XmlConstants.MONTH);
			MultiCalendar date = MultiCalendar.createFromGregorianYearMonthDay(year, month, 1);
			
			return DateUnit.createMonthDateUnit(date.toIsoDateString());
		}
		
		Node dayNode = getImporter().getNamedChildNode(dateUnitNode, getDayElementName());
		if (dayNode != null)
		{
			return DateUnit.createDayDateUnit(getImporter().getAttributeValue(dayNode, DATE));
		}
		
		throw new RuntimeException("Could not find a matching Date unit for element content.");
	}

	private int getAttributeAsInt(Node node, String attributeName)
	{
		String asString = getImporter().getAttributeValue(node, attributeName);
		return Integer.parseInt(asString);
	}
	
	abstract protected String getDateUnitsElementName();

	abstract protected String getDateUnitElementName();
	
	abstract protected String getDayElementName();
	
	abstract protected String getMonthElementName();
	
	abstract protected String getQuarterElementName();
	
	abstract protected String getYearElementName();
	
	abstract protected String getFullProjectTimespanElementName();
	
	abstract protected String getQuantatityElementName();
}
