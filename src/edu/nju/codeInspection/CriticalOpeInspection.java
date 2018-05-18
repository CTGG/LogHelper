package edu.nju.codeInspection;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.util.PsiUtil;
import edu.nju.util.loggingutil.LoggingUtil;
import edu.nju.quickfixes.javalogging.configlogging.CriticalOpeJavaConfigQuickfix;
import edu.nju.quickfixes.javalogging.finelogging.CriticalOpeJavaFineQuickfix;
import edu.nju.quickfixes.javalogging.finerllogging.CriticalOpeJavaFinerQuickfix;
import edu.nju.quickfixes.javalogging.finestlogging.CriticalOpeJavaFinestQuickfix;
import edu.nju.quickfixes.javalogging.infologging.CriticalOpeJavaInfoQuickfix;
import edu.nju.quickfixes.javalogging.severelogging.CriticalOpeJavaSevereQuickfix;
import edu.nju.quickfixes.javalogging.warnlogging.CriticalOpeJavaWarningQuickfix;
import edu.nju.quickfixes.log4jcommonslogging.fatallogging.CriticalOpeLog4jFatalQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.debuglogging.CriticalOpeSlf4jDebugQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.errorlogging.CriticalOpeSlf4jErrorQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.infologging.CriticalOpeSlf4jInfoQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.tracelogging.CriticalOpeSlf4jTraceQuickfix;
import edu.nju.quickfixes.slf4jlog4jcommonslogging.warnlogging.CriticalOpeSlf4jWarnQuickfix;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;


/**
 * Created by chentiange on 2018/5/7.
 */
public class CriticalOpeInspection extends BaseJavaLocalInspectionTool {

//    private final String fileNameForClass = "E:\\workspace\\LogHelper\\resources\\criticalClass";
//    private final String fileNameForMethod = "E:\\workspace\\LogHelper\\resources\\criticalMethod";
    final String fileNameForMethod = "/Users/chentiange/Downloads/LogHelper/resources/criticalMethod";
    final String fileNameForClass = "/Users/chentiange/Downloads/LogHelper/resources/criticalClass";

    private final LocalQuickFix criticalOpeJavaInfoQuickfix = new CriticalOpeJavaInfoQuickfix();
    private final LocalQuickFix criticalOpeJavaConfigQuickfix = new CriticalOpeJavaConfigQuickfix();
    private final LocalQuickFix criticalOpeJavaFineQuickfix = new CriticalOpeJavaFineQuickfix();
    private final LocalQuickFix criticalOpeJavaFinerQuickfix = new CriticalOpeJavaFinerQuickfix();
    private final LocalQuickFix criticalOpeJavaFinestQuickfix = new CriticalOpeJavaFinestQuickfix();
    private final LocalQuickFix criticalOpeJavaSevereQuickfix = new CriticalOpeJavaSevereQuickfix();
    private final LocalQuickFix criticalOpeJavaWarningQuickfix = new CriticalOpeJavaWarningQuickfix();
    private final LocalQuickFix criticalOpeLog4jFatalQuickfix = new CriticalOpeLog4jFatalQuickfix();
    private final LocalQuickFix criticalOpeSlf4jDebugQuickfix = new CriticalOpeSlf4jDebugQuickfix();
    private final LocalQuickFix criticalOpeSlf4jErrorQuickfix = new CriticalOpeSlf4jErrorQuickfix();
    private final LocalQuickFix criticalOpeSlf4jInfoQuickfix = new CriticalOpeSlf4jInfoQuickfix();
    private final LocalQuickFix criticalOpeSlf4jTraceQuickfix = new CriticalOpeSlf4jTraceQuickfix();
    private final LocalQuickFix criticalOpeSlf4jWarnQuickfix = new CriticalOpeSlf4jWarnQuickfix();

    private static final String DESCRIPTION_TEMPLATE = "inspection.CriticalOpe.descriptor";

    @NotNull
    public String getDisplayName() {
        return "Critical operation should be logged";
    }

    @NotNull
    public String getGroupDisplayName() {
        return GroupNames.LOGGING_GROUP_NAME;
    }

    //对应的html
    @NotNull
    public String getShortName() {
        return "CriticalOperationLogging";
    }



    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            @Override
            public void visitReferenceExpression(PsiReferenceExpression psiReferenceExpression) {
            }


            @Override
            public void visitClass(PsiClass aClass) {
                super.visitClass(aClass);
                if (!(aClass.getParent() instanceof PsiJavaFile)) {
                    return;
                }

                String line = "";
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(fileNameForClass));
                    line = bufferedReader.readLine();
                    while (line != null){
                        String currentClassWholeName = LoggingUtil.getCurrentClassWholeName(aClass);
                        if (Pattern.matches(line,currentClassWholeName)){
                            //get all method
                            PsiMethod[] methods = aClass.getMethods();
                            for (PsiMethod method:methods){
                                if (!method.getName().equals("main")) {
                                    visitMethodInClass(method);
                                }
                            }
                        }
                        line = bufferedReader.readLine();
                    }
                    bufferedReader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void visitMethod(PsiMethod method) {
                super.visitMethod(method);
                String line = "";
                final PsiClass currentFileClass = PsiTreeUtil.getParentOfType(method, PsiClass.class);
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(fileNameForMethod));
                    line = bufferedReader.readLine();
                    while (line != null){
                        final String currentClassWholeName = LoggingUtil.getCurrentClassWholeName(currentFileClass);
                        final String currentMethodWholeName = currentClassWholeName+":"+method.getName();
                        if (Pattern.matches(line,currentMethodWholeName)&& isToLog(method)){
                            holder.registerProblem(method,DESCRIPTION_TEMPLATE,criticalOpeJavaInfoQuickfix, criticalOpeJavaConfigQuickfix,criticalOpeJavaFineQuickfix,criticalOpeJavaFinerQuickfix,
                                    criticalOpeJavaFinestQuickfix, criticalOpeJavaSevereQuickfix, criticalOpeJavaWarningQuickfix,criticalOpeLog4jFatalQuickfix,
                                    criticalOpeSlf4jDebugQuickfix,criticalOpeSlf4jErrorQuickfix,criticalOpeSlf4jInfoQuickfix,criticalOpeSlf4jTraceQuickfix,criticalOpeSlf4jWarnQuickfix);
                        }
                        line = bufferedReader.readLine();
                    }
                    bufferedReader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            private void visitMethodInClass(PsiMethod method) {
                super.visitMethod(method);
                if (isToLog(method)) {
                    holder.registerProblem(method, DESCRIPTION_TEMPLATE, criticalOpeJavaInfoQuickfix, criticalOpeJavaConfigQuickfix, criticalOpeJavaFineQuickfix, criticalOpeJavaFinerQuickfix,
                            criticalOpeJavaFinestQuickfix, criticalOpeJavaSevereQuickfix, criticalOpeJavaWarningQuickfix, criticalOpeLog4jFatalQuickfix,
                            criticalOpeSlf4jDebugQuickfix, criticalOpeSlf4jErrorQuickfix, criticalOpeSlf4jInfoQuickfix, criticalOpeSlf4jTraceQuickfix, criticalOpeSlf4jWarnQuickfix);
                }
            }
        };
    }

    private boolean isToLog(PsiMethod method){
        final PsiCodeBlock codeBlock =  method.getBody();
        final PsiStatement[] statements = codeBlock.getStatements();
        if (statements.length == 0) return false;
        //check first
        if (!statements[0].getText().startsWith("log.")){
            return true;
        }
        //check returns
        boolean returnAllLogged = true;
        PsiType returnType = method.getReturnType();;
        if (returnType == PsiType.VOID){
            return false;
        }

        PsiReturnStatement[] returnStatements = PsiUtil.findReturnStatements(method);
        for (PsiReturnStatement returnStatement:returnStatements){
            //check previous
            if (!returnStatement.getPrevSibling().getPrevSibling().getText().startsWith("log.")){
                returnAllLogged = false;
            }
        }
        if (returnAllLogged){
            return false;
        }
        return true;
    }


    public boolean isEnabledByDefault() {
        return true;
    }
}
