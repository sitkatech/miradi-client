package org.miradi.actions;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionExportMiradiShareFile extends MainWindowAction
{
    public ActionExportMiradiShareFile(MainWindow mainWindowToUse)
    {
        super(mainWindowToUse, getLabel());
    }

    public static String getLabel()
    {
        return EAM.text("Action|Export MiradiShare File");
    }

    @Override
    public String getToolTipText()
    {
        return EAM.text("TT|Export entire project as an XML file");
    }
}
