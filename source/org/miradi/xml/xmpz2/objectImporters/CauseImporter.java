/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

import org.miradi.objecthelpers.CodeToCodeMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.questions.ThreatClassificationQuestionV11;
import org.miradi.questions.ThreatClassificationQuestionV20;
import org.miradi.schemas.CauseSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;

public class CauseImporter extends BaseObjectImporter
{
    public CauseImporter(Xmpz2XmlImporter importerToUse)
    {
        super(importerToUse, new CauseSchema());
    }

    @Override
    public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
    {
        super.importFields(baseObjectNode, refToUse);

        importCauseStandardClassifications(baseObjectNode, refToUse);
    }

    @Override
    protected boolean isCustomImportField(String tag)
    {
        if (tag.equals(Cause.TAG_STANDARD_CLASSIFICATION_V11_CODE))
            return true;

        if (tag.equals(Cause.TAG_STANDARD_CLASSIFICATION_V20_CODE))
            return true;

        return super.isCustomImportField(tag);
    }

    private void importCauseStandardClassifications(Node baseObjectNode, ORef refToUse) throws Exception
    {
        CodeToCodeMap causeStandardClassificationCodes = new CodeToCodeMap();

        Node causeStandardClassificationContainerNode = getImporter().getNamedChildNode(baseObjectNode,  CAUSE_STANDARD_CLASSIFICATION_CONTAINER);
        if (causeStandardClassificationContainerNode != null)
        {
            NodeList causeStandardClassificationNodes = getImporter().getNodes(causeStandardClassificationContainerNode, new String[]{CAUSE_STANDARD_CLASSIFICATION});

            for (int index = 0; index < causeStandardClassificationNodes.getLength(); ++index)
            {
                Node causeStandardClassificationNode = causeStandardClassificationNodes.item(index);
                String causeStandardClassificationCode = getImporter().getAttributeValue(causeStandardClassificationNode, CAUSE_STANDARD_CLASSIFICATION_CODE);
                Node codeNode = getImporter().getNamedChildNode(causeStandardClassificationNode, CODE_ELEMENT_NAME);
                if (codeNode != null)
                {
                    String codeValue = codeNode.getTextContent();
                    causeStandardClassificationCodes.putCode(causeStandardClassificationCode, codeValue);
                }
            }
        }

        setStandardClassifications(refToUse, causeStandardClassificationCodes);
    }

    private void setStandardClassifications(ORef refToUse, CodeToCodeMap causeStandardClassificationCodes) throws Exception
    {
        HashMap<String, String> codeListHashMap = causeStandardClassificationCodes.toHashMap();
        for (String causeStandardClassificationCode : codeListHashMap.keySet())
        {
            String code = codeListHashMap.get(causeStandardClassificationCode);
            if (!code.isEmpty())
            {
                if (causeStandardClassificationCode.equals(ThreatClassificationQuestionV11.STANDARD_CLASSIFICATION_CODELIST_KEY))
                    getImporter().setData(refToUse, Cause.TAG_STANDARD_CLASSIFICATION_V11_CODE, code);

                if (causeStandardClassificationCode.equals(ThreatClassificationQuestionV20.STANDARD_CLASSIFICATION_CODELIST_KEY))
                    getImporter().setData(refToUse, Cause.TAG_STANDARD_CLASSIFICATION_V20_CODE, code);
            }
        }
    }
}
