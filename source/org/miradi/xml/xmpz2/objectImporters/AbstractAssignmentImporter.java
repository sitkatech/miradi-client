/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.xml.xmpz2.objectImporters;

import org.martus.util.MultiCalendar;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Assignment;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.DoubleUtilities;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

abstract public class AbstractAssignmentImporter extends BaseObjectImporter
{
	public AbstractAssignmentImporter(Xmpz2XmlImporter importerToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(importerToUse, baseObjectSchemaToUse);
	}
	
	@Override
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		super.importFields(baseObjectNode, refToUse);
		
		importDateUnitEffortList(baseObjectNode, refToUse);
	}
	
	@Override
	protected boolean isCustomImportField(String tag)
	{
		if (tag.equals(Assignment.TAG_DATEUNIT_DETAILS))
			return true;
		
		return super.isCustomImportField(tag);
	}
	
	private void importDateUnitEffortList(Node node, ORef destinationRef) throws Exception
	{
		Node dateUnitEffortsNode = getImporter().getNamedChildNode(node, getXmpz2ElementName() + Assignment.TAG_DATEUNIT_DETAILS);
		if (dateUnitEffortsNode == null)
			return;
		
		NodeList dateUnitEffortNodes = getImporter().getNodes(dateUnitEffortsNode, new String[]{getDateUnitsElementName(), });
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		for (int index = 0; index < dateUnitEffortNodes.getLength(); ++index)
		{
			Node dateUnitEffortNode = dateUnitEffortNodes.item(index);
			Node dateUnitNode = getImporter().getNamedChildNode(dateUnitEffortNode, getDateUnitElementName());
			DateUnit dateUnit = extractDateUnit(dateUnitNode);

			double quantity = 0.0;
			if (supportsQuantityElement())
			{
				Node quantityNode = getImporter().getNamedChildNode(dateUnitEffortNode, getQuantityElementName());
				String quantityAsString = quantityNode.getTextContent();
				quantity = DoubleUtilities.toDoubleFromDataFormat(quantityAsString);
			}

			DateUnitEffort dateUnitEffort = new DateUnitEffort(dateUnit, quantity);
			dateUnitEffortList.add(dateUnitEffort);
		}
		
		getImporter().setData(destinationRef, Assignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toString());
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
			int startYear = getAttributeAsInt(yearNode, START_YEAR);
			int startMonth = getAttributeAsInt(yearNode, START_MONTH);

			return DateUnit.createFiscalYear(startYear, startMonth);
		}
		
		Node quarterNode = getImporter().getNamedChildNode(dateUnitNode, getQuarterElementName());
		if (quarterNode != null)
		{
			int year = getAttributeAsInt(quarterNode, YEAR);
			int startMonth = getAttributeAsInt(quarterNode, START_MONTH);
			
			return DateUnit.createQuarterDateUnit(year, (startMonth-1)/3 + 1);
		}
			
		Node monthNode = getImporter().getNamedChildNode(dateUnitNode, getMonthElementName());
		if (monthNode != null)
		{
			int year = getAttributeAsInt(monthNode, YEAR);
			int month = getAttributeAsInt(monthNode, MONTH);
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

	abstract protected String getQuantityElementName();

	protected boolean supportsQuantityElement()
	{
		return true;
	}
}
