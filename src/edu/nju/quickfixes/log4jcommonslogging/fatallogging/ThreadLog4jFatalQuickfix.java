package edu.nju.quickfixes.log4jcommonslogging.fatallogging;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import edu.nju.util.loggingutil.LoggingType;
import edu.nju.util.loggingutil.ThreadLoggingUtil;
import edu.nju.util.loggingutil.logginglevel.Log4jLoggingLevel;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by chentiange on 2018/5/10.
 */
public class ThreadLog4jFatalQuickfix implements LocalQuickFix {
    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Logging thread using log4j or commons-logging fatal level";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return getName();
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
        ThreadLoggingUtil.doThreadLogging(project,problemDescriptor, LoggingType.LOG4J, Log4jLoggingLevel.LOG_FATAL,1);
    }
}
