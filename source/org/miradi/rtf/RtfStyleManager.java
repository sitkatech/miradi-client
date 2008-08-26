/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.rtf;

import java.util.HashMap;
import java.util.Set;

import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Target;

public class RtfStyleManager
{
	public RtfStyleManager()
	{
		clear();
	}
	
	private void clear()
	{
		styleMap = new HashMap();
		
		getStyleMap().put(COMMENT_STYLE_TAG, new RtfStyle(COMMENT_STYLE_TAG, CS_15_RTF_ID, "{\\*" + CS_15_RTF_ID + " \\additive \\rtlch\\fcs1 \\af0 \\ltrch\\fcs0 \\b\\i\\fs28 \\sbasedon10 \\styrsid10564856 Long Text;}"));
		
		creatNewStyle(ConceptualModelDiagram.OBJECT_NAME, CS_16_RTF_ID, " \\additive \\rtlch\\fcs1 \\af0 \\ltrch\\fcs0 \\b\\i\\fs28 \\sbasedon10 \\styrsid10564856 Conceptual Model Diagram;}");
		creatNewStyle(ConceptualModelDiagram.OBJECT_NAME, CS_16_RTF_ID, " \\additive \\rtlch\\fcs1 \\af0 \\ltrch\\fcs0 \\b\\i\\fs28 \\sbasedon10 \\styrsid10564856 Conceptual Model Diagram;}");
		creatNewStyle(ResultsChainDiagram.OBJECT_NAME,    CS_17_RTF_ID, " \\additive \\rtlch\\fcs1 \\af0 \\ltrch\\fcs0 \\b\\i\\fs28 \\sbasedon10 \\styrsid10564856 Results Chain Diagram;}");
		creatNewStyle(Target.OBJECT_NAME,                 CS_18_RTF_ID, " \\additive \\rtlch\\fcs1 \\af0 \\ltrch\\fcs0 \\b\\i\\fs28 \\sbasedon10 \\styrsid10564856 Target;}");
		creatNewStyle(Cause.OBJECT_NAME_THREAT,           CS_19_RTF_ID, " \\additive \\rtlch\\fcs1 \\af0 \\ltrch\\fcs0 \\b\\i\\fs28 \\sbasedon10 \\styrsid10564856 Direct Threat;}");
	}

	private void creatNewStyle(String objectName, String rtfStyleId, String rtfFormatingCommand)
	{
		RtfStyle rtfStyle = new RtfStyle(objectName, rtfStyleId, RTF_STYLE_FORMATTING_START_BLOCK + rtfStyleId + rtfFormatingCommand);
		getStyleMap().put(objectName, rtfStyle);
	}

	public void exportStyleTable(RtfWriter writer) throws Exception
	{	
		Set<String> keys = getStyleMap().keySet();
		for(String key : keys)
		{
			RtfStyle rtfStyle = getStyleMap().get(key);
			writer.writeln(rtfStyle.getRtfFormatingCommand());
		}
	}
	
	public String createStyleTag(BaseObject baseObject)
	{
		return baseObject.getTypeName();
	}
	
	private HashMap<String, RtfStyle> getStyleMap()
	{
		return styleMap;
	} 
	
	private HashMap<String, RtfStyle> styleMap;
	
	public static final String RTF_STYLE_FORMATTING_START_BLOCK = "{\\*";
	public static final String COMMENT_STYLE_TAG = "CommentStyle";
	public static final String CS_15_RTF_ID = "\\cs15";
	public static final String CS_16_RTF_ID = "\\cs16";
	public static final String CS_17_RTF_ID = "\\cs17";
	public static final String CS_18_RTF_ID = "\\cs18";
	public static final String CS_19_RTF_ID = "\\cs19";
}
