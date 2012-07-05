/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.views.umbrella;

import java.io.File;
import java.util.HashSet;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TncProjectData;
import org.miradi.schemas.TncProjectDataSchema;
import org.miradi.utils.HtmlUtilities;
import org.miradi.utils.MiradiFileSaveChooser;
import org.miradi.utils.MpfFileFilter;
import org.miradi.utils.MpfToMpzConverter;
import org.miradi.utils.MpzFileChooser;
import org.miradi.utils.ProgressInterface;
import org.miradi.utils.Translation;

public class ExportMpzDoer extends AbstractFileSaverDoer
{
	@Override
	protected MiradiFileSaveChooser createFileChooser()
	{
		return new MpzFileChooser(getMainWindow());
	}

	@Override
	protected boolean doWork(File destinationFile, ProgressInterface progressInterface) throws Exception
	{
		MpfToMpzConverter converter = new MpfToMpzConverter(getProject());
		File currentMpfFile = new File(EAM.getHomeDirectory(), getProject().getFilename() + MpfFileFilter.EXTENSION);
		converter.convert(currentMpfFile, destinationFile, getProject().getFilename());
		
		return true;
	}

	@Override
	protected String getProgressTitle()
	{
		return EAM.text("Export MPZ");
	}
	
	@Override
	protected void displayUserInfoDialog()
	{
		HashSet<String> hasNonBackwardCompatipalFieldsWithValues = hasNonBackwardCompatipalFieldsWithValues();
		if (hasNonBackwardCompatipalFieldsWithValues.size() == 0)
			return;
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(EAM.text("These fields contain data, however the data will not appear in version 3.3.2:"));
		for(String fieldName : hasNonBackwardCompatipalFieldsWithValues)
		{
			stringBuffer.append(HtmlUtilities.BR_TAG);
			stringBuffer.append("- ");
			stringBuffer.append(fieldName);
		}
		
		EAM.displayHtmlWarningDialog(stringBuffer.toString());	
	}

	private HashSet<String> hasNonBackwardCompatipalFieldsWithValues()
	{
		HashSet<String> fieldNamesWithValues = new HashSet<String>();
		ORef tncProjectDataRef = getProject().getSingletonObjectRef(TncProjectDataSchema.getObjectType());
		BaseObject baseObject = BaseObject.find(getProject(), tncProjectDataRef);
		addInPlaceFieldNamesWithValues(fieldNamesWithValues, baseObject, TncProjectData.TAG_MAKING_THE_CASE);
		addInPlaceFieldNamesWithValues(fieldNamesWithValues, baseObject, TncProjectData.TAG_RISKS);
		addInPlaceFieldNamesWithValues(fieldNamesWithValues, baseObject, TncProjectData.TAG_CAPACITY_AND_FUNDING);
		
		return fieldNamesWithValues;
	}

	private void addInPlaceFieldNamesWithValues(HashSet<String> fieldNamesWithValues, BaseObject baseObject, final String tag)
	{
		if (baseObject.getData(tag).length() > 0)
			fieldNamesWithValues.add(Translation.fieldLabel(baseObject.getType(), tag));
	}
}
