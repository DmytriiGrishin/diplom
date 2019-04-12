package ru.ifmo.docx_templater.util;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;

public class RunUtils {
    public static XWPFRun splitRun(XWPFParagraph paragraph, XWPFRun run, int at) {
        XWPFRun newRun;
        int i = paragraph.getRuns().indexOf(run);
        newRun = paragraph.insertNewRun(i+1);
        String remainingText = run.text().substring(at);
        newRun.setText(remainingText);
        run.setText(run.text().substring(0, at), 0);
        copyStyle(run, newRun);
        return newRun;
    }

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

    public static XWPFRun concatRuns(XWPFRun left, XWPFRun right) {
        left.setText(left.text() + right.text(), 0);
        right.setText("", 0);
        return left;
    }
}
