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

import org.miradi.objects.AccountingCode;
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
		
		createNewStyle(NORMAL_STYLE_TAG,                    FS_20_RTF_ID," \\sbasedon222\\snext0{\\*\\keycode \\shift\\ctrl n} ", "Normal");             
		createNewStyle(COMMENT_STYLE_TAG, 					S_15_RTF_ID, " \\additive \\b\\i\\fs28 \\sbasedon10 \\styrsid10564856 ", "Long Text");		
		createNewStyle(ConceptualModelDiagram.OBJECT_NAME, 	S_16_RTF_ID, " \\additive \\b\\i\\fs28 \\sbasedon10 \\styrsid10564856 ", "Conceptual Model Diagram");
		createNewStyle(ResultsChainDiagram.OBJECT_NAME,    	S_17_RTF_ID, " \\additive \\b\\i\\fs28 \\sbasedon10 \\styrsid10564856 ", "Results Chain Diagram");
		createNewStyle(Target.OBJECT_NAME,                 	S_18_RTF_ID, " \\additive \\b\\i\\fs28 \\sbasedon10 \\styrsid10564856 ", "Target");
		createNewStyle(Cause.OBJECT_NAME_THREAT,           	S_19_RTF_ID, " \\additive \\b\\i\\fs28 \\sbasedon10 \\styrsid10564856 ", "Direct Threat");
		createNewStyle(AccountingCode.OBJECT_NAME,  	    S_20_RTF_ID, " \\additive \\b\\i\\fs28 \\sbasedon10 \\styrsid10564856 ", "Accounting Code");
	}

	private void createNewStyle(String objectName, String rtfStyleId, String rtfFormatingCommand, String styleName)
	{
		RtfStyle rtfStyle = new RtfStyle(objectName, rtfStyleId, rtfStyleId + rtfFormatingCommand, styleName);
		getStyleMap().put(objectName, rtfStyle);
	}

	public void exportRtfStyleTable(RtfWriter writer) throws Exception
	{	
		writer.newLine();
		writer.startBlock();
		writer.writeln("\\stylesheet ");
		Set<String> keys = getStyleMap().keySet();
		for(String key : keys)
		{
			writer.startBlock();
			RtfStyle rtfStyle = getStyleMap().get(key);
			writer.write(rtfStyle.getRtfFormatingCommand() + rtfStyle.getStyleName() + ";");
			writer.endBlock();
		}
		
		writer.endBlock();
	}
	
	public String createStyleTag(BaseObject baseObject)
	{
		return baseObject.getTypeName();
	}
	
	private HashMap<String, RtfStyle> getStyleMap()
	{
		return styleMap;
	} 
	
	public static String createTag(BaseObject baseObjectForRow)
	{
		return baseObjectForRow.getTypeName();
	}
	
	//FIXME this is a a temporary method
	public String getStyleFormatingCommand(String styleTag)
	{
		RtfStyle rtfStyle = getStyleMap().get(styleTag);
		if (rtfStyle != null)
			return "{" + rtfStyle.getRtfFormatingCommand() + "}";
		
		return "";
	}
			
	private HashMap<String, RtfStyle> styleMap;
	
	public static final String COMMENT_STYLE_TAG = "CommentStyle";
	public static final String NORMAL_STYLE_TAG = "NormalStyle";
	public static final String S_15_RTF_ID = "\\cs15";
	public static final String S_16_RTF_ID = "\\cs16";
	public static final String S_17_RTF_ID = "\\cs17";
	public static final String S_18_RTF_ID = "\\cs18";
	public static final String S_19_RTF_ID = "\\cs19";
	public static final String S_20_RTF_ID = "\\cs20";
	public static final String FS_20_RTF_ID = "\\fs20";
}
