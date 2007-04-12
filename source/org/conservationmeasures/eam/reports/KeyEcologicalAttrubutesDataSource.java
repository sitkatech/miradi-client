/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.KeyEcologicalAttributeTypeQuestion;

public class KeyEcologicalAttrubutesDataSource implements JRDataSource
{
	public KeyEcologicalAttrubutesDataSource(Target target)
	{
		project = target.getObjectManager().getProject();
		if (target.isViabilityModeTNC())
		{
			ORefList list = new ORefList(KeyEcologicalAttribute.getObjectType(), target.getKeyEcologicalAttributes());
			count = list.size();
			keas = loadSortedKeas(list);
		}
		else
		{
			count = 0;
			keas = new KeyEcologicalAttribute[0];
		}
	}

	private KeyEcologicalAttribute[] loadSortedKeas(ORefList list)
	{
		KeyEcologicalAttribute[] keasToSort = new KeyEcologicalAttribute[list.size()];
		for (int i=0; i<list.size(); ++i)
		{
			keasToSort[i] = (KeyEcologicalAttribute) project.findObject(list.get(i));
		}
		Arrays.sort(keasToSort, new KeaTypeComparator());
		return keasToSort;
	}
	
	public JRDataSource getViabilityIndicatorsDataSource()
	{
		return new ViabilityIndicatorsDataSource(keas[count]);
	}
	
	public Object getFieldValue(JRField field) throws JRException
	{
		return getData(field.getName());
	}

	public boolean next() throws JRException 
	{
		return (--count>=0);
	}

	public String getData(String name)
	{
		String value = keas[count].getData(name);
		if (name.equals(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE))
		{
			value = translateCode(value);
		}
		return value;
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
	
	int count;
	KeyEcologicalAttribute[] keas;
	Project project;
} 
