package edu.nju.codeInspection;


import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInsight.template.Expression;
import com.intellij.codeInsight.template.ExpressionContext;
import com.intellij.codeInspection.*;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.impl.PsiBuilderImpl;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import com.intellij.psi.impl.source.tree.java.PsiJavaTokenImpl;
import com.intellij.psi.tree.IElementType;
import edu.nju.livetemplates.ResolveLoggerInstance;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class ExceptionInspection extends BaseJavaLocalInspectionTool {
    private static final Logger LOG = Logger.getInstance("#edu.nju.codeInspection.ExceptionInspection");
    private final LocalQuickFix myQuickFix = new MyQuickFix();

    @SuppressWarnings({"WeakAccess"})
    @NonNls
    private static final String DESCRIPTION_TEMPLATE = "Catch到异常,需要输出log";

    @NotNull
    public String getDisplayName() {
        return "Catch到的异常";
    }

    @NotNull
    public String getGroupDisplayName() {
        return GroupNames.BUGS_GROUP_NAME;
    }

    @NotNull
    public String getShortName() {
        return "Exception";
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitReferenceExpression(PsiReferenceExpression psiReferenceExpression) {
            }


            @Override
            public void visitCatchSection(PsiCatchSection section) {
                super.visitCatchSection(section);
                PsiCodeBlock psiCodeBlock = section.getCatchBlock();
                if (psiCodeBlock == null) {
                    return;
                }
                PsiStatement[] psiStatements = psiCodeBlock.getStatements();
                if (psiStatements.length != 0) {
                    for (PsiStatement psiStatement : psiStatements) {
                        //判断是否有log语句
                        if (psiStatement instanceof PsiExpressionStatement) {
                            PsiExpression expression = ((PsiExpressionStatement) psiStatement).getExpression();
                            if (expression instanceof PsiMethodCallExpression) {
                                if (Objects.requireNonNull(((PsiMethodCallExpression) expression).getMethodExpression().getQualifierExpression()).getText().equals("log")) {
                                    return;
                                }
                            }
                        }
                    }
                }
                holder.registerProblem(section, DESCRIPTION_TEMPLATE, myQuickFix);
            }
        };
    }

    private static class MyQuickFix implements LocalQuickFix {
        @NotNull
        public String getName() {
            return "添加log语句";
        }

        @Nls
        @NotNull
        @Override
        public String getFamilyName() {
            return getName();
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
            PsiCatchSection psiCatchSection = (PsiCatchSection) problemDescriptor.getPsiElement();
            PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
            String exceptionTypeName = "";
            String exceptionName = "";
            if (psiCatchSection.getCatchType() != null && psiCatchSection.getParameter() != null) {
                exceptionTypeName = psiCatchSection.getCatchType().getCanonicalText();
                exceptionName = psiCatchSection.getParameter().getName();
            }
            PsiExpressionStatement logStatement= (PsiExpressionStatement) factory.createStatementFromText("log.error(\"" + exceptionTypeName + "\"+" + exceptionName + "+\"\");",null);
            PsiCodeBlock catchBlock = psiCatchSection.getCatchBlock();
            if(catchBlock==null){
                LOG.error("catchBlock is null");
            }
            else{
                logStatement= (PsiExpressionStatement) catchBlock.getFirstBodyElement().replace(logStatement);
//                LOG.info(new ResolveLoggerInstance().calculateResult(new Expression[0], (ExpressionContext) logStatement.getContext()).toString());
            }
        }
    }

    public boolean isEnabledByDefault() {
        return true;
    }
}
