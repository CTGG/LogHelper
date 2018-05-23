package edu.nju.codeInspection;

import com.intellij.codeInspection.InspectionToolProvider;

/**
 * @author max
 */
public class InspectionProvider implements InspectionToolProvider {
    public Class[] getInspectionClasses() {
        return new Class[]{TotalInspection.class};
    }
}
