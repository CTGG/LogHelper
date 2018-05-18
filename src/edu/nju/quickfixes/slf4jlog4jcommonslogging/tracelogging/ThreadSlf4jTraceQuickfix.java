package edu.nju.quickfixes.slf4jlog4jcommonslogging.tracelogging;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import edu.nju.util.loggingutil.LoggingType;
import edu.nju.util.loggingutil.logginglevel.Slf4JLoggingLevel;
import edu.nju.util.loggingutil.ThreadLoggingUtil;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by chentiange on 2018/5/9.
 */
public class ThreadSlf4jTraceQuickfix implements LocalQuickFix {
    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Logging thread using slf4j or log4j or commons-logging trace level";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return getName();
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
        ThreadLoggingUtil.doThreadLogging(project,problemDescriptor, LoggingType.SLF4J, Slf4JLoggingLevel.LOG_TRACE,2);
    }
}
