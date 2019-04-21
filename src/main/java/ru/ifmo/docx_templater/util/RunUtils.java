package ru.ifmo.docx_templater.util;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;

public class RunUtils {

    /**
     * Copies style from one run to another
     * @param from run to copy from
     * @param to run to copy to
     */
    public static void copyStyle(XWPFRun from, XWPFRun to) {
        CTRPr rPr = from.getCTR().getRPr();
        if (rPr != null) {
            CTString rStyle = rPr.getRStyle();
            if (rStyle != null) {
                String styleId = rStyle.getVal();
                if (styleId != null) {
                    to.setStyle(styleId);
                    return;
                }
            }
        }
        CTRPr ctrPr = to.getCTR().isSetRPr() ? to.getCTR().getRPr() : to.getCTR().addNewRPr();
        ctrPr.set(from.getCTR().getRPr());
    }

}
