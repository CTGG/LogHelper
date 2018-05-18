package edu.nju.codeInspection;

import com.intellij.codeInspection.InspectionToolProvider;

/**
 * @author max
 */
public class InspectionProvider implements InspectionToolProvider {
    public Class[] getInspectionClasses() {
        return new Class[]{ComparingReferencesInspection.class,ExceptionInspection.class,AssertInspection.class,CriticalOpeInspection.class,JDBCInspection.class,ThreadInspection.class,BranchStatementsInspection.class,SwitchInspection.class};
    }
}
