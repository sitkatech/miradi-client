/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import java.text.Collator;
import java.util.Comparator;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRField;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.questions.KeyEcologicalAttributeTypeQuestion;

public class KeyEcologicalAttrubutesDataSource extends CommonDataSource
{
	public KeyEcologicalAttrubutesDataSource(Target target)
	{
		super(target.getObjectManager().getProject());
		if (target.isViabilityModeTNC())
		{
			ORefList list = new ORefList(KeyEcologicalAttribute.getObjectType(), target.getKeyEcologicalAttributes());
			setObjectList(list);
			sortObjectList(new KeaTypeComparator());
		}
		else
		{
			setObjectList(new ORefList());
		}
	}
	
	public JRDataSource getViabilityIndicatorsDataSource()
	{
		return new ViabilityIndicatorsDataSource((KeyEcologicalAttribute)getCurrentObject());
	}
	
	public Object getFieldValue(JRField field)
	{
		if (field.getName().equals(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE))
		{
			String value = getCurrentObject().getData(field.getName());
			return  translateCode(value);
		}
		return getValue(field, getCurrentObject());
	}

	private String translateCode(String value)
	{
		KeyEcologicalAttributeTypeQuestion question = new KeyEcologicalAttributeTypeQuestion(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE);
		value =  question.findChoiceByCode(value).getLabel();
		return value;
	}
	
	class KeaTypeComparator implements Comparator
	{
		public int compare(Object kea1, Object kea2)
		{
			String type1 =((KeyEcologicalAttribute) kea1).getKeyEcologicalAttributeType();
			String type2 =((KeyEcologicalAttribute) kea2).getKeyEcologicalAttributeType();
			Collator myCollator = Collator.getInstance();
			return myCollator.compare(type1, type2);
		}
	}
} 
