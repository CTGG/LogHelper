package edu.nju.codeInspection;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.formatting.WhiteSpace;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.StdLanguages;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.tree.IElementType;
import com.intellij.ui.DocumentAdapter;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.util.StringTokenizer;


/**
 * 检测分支语句
 * if if-else switch
 */
public class BranchStatementsInspection extends BaseJavaLocalInspectionTool {
    private static final Logger LOG = Logger.getInstance("#com.com.intellij.codeInspection.BranchStatementsInspection");

    private final LocalQuickFix myQuickFix = new MyQuickFix();

    @SuppressWarnings({"WeakerAccess"})
    @NonNls
    public String CHECKED_CLASSES = "java.lang.String;java.util.Date";
//    @NonNls
//    private static final String DESCRIPTION_TEMPLATE =
//            InspectionsBundle.message("inspection.branchStatements.problem.somethingwrong");

    @NotNull
    public String getDisplayName() {
        return " if/ if-else need to log";
    }

    @NotNull
    public String getGroupDisplayName() {
        return GroupNames.BUGS_GROUP_NAME;
    }

    //对应html

    @NotNull
    public String getShortName() {
        return "BranchStatements";
    }

    private boolean isCheckedType(PsiType type) {
        if (!(type instanceof PsiClassType)) return false;

        StringTokenizer tokenizer = new StringTokenizer(CHECKED_CLASSES, ";");
        while (tokenizer.hasMoreTokens()) {
            String className = tokenizer.nextToken();
            if (type.equalsToText(className)) return true;
        }

        return false;
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            @Override
            public void visitReferenceExpression(PsiReferenceExpression psiReferenceExpression) {

            }


            /**
             *
             * @param
             */
            @Override
            public void visitIfStatement(PsiIfStatement statement) {

                super.visitIfStatement(statement);

                // 先获得if-else代码块  在里面写提示"if-else hhh"
                PsiExpression expression = statement.getCondition();

                //获取then分支
                PsiBlockStatement thenbranch = (PsiBlockStatement) statement.getThenBranch();

                //获取else分支
                PsiBlockStatement elsebranch = (PsiBlockStatement) statement.getElseBranch();
                if(expression==null|| thenbranch ==null || elsebranch==null){
                    return;
                }
                PsiStatement[] thenbranchs = thenbranch.getCodeBlock().getStatements();

                PsiStatement[] elsebranchs = elsebranch.getCodeBlock().getStatements();
                //开始报问题
                holder.registerProblem(expression,
                            "重要分支语句缺少log", myQuickFix);

            }
        };
    }

    private static class MyQuickFix implements LocalQuickFix {
        @NotNull
        public String getName() {
            // The test (see the TestThisPlugin class) uses this string to identify the quick fix action.
//            return InspectionsBundle.message("inspection.comparing.references.use.quickfix");

            return "为重要分支 添加log语句";
        }


        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            try {

                //获取当下的元素
                PsiBinaryExpression binaryExpression=(PsiBinaryExpression) descriptor.getPsiElement();

                //获取分支主元素
                PsiIfStatement ifStatement = (PsiIfStatement)binaryExpression.getParent();

                //暂时支持单层的if-else
                PsiBlockStatement ifBlock = (PsiBlockStatement) binaryExpression.getNextSibling().getNextSibling();
                PsiBlockStatement elseBlock = (PsiBlockStatement)ifStatement.getLastChild();

                LogPsiBlockStatement(ifBlock,project);
                LogPsiBlockStatement(elseBlock,project);

            } catch (IncorrectOperationException e) {
                LOG.error(e);
            }
        }


        private void LogPsiBlockStatement(PsiBlockStatement psiBlockStatement,Project project){

            PsiCodeBlock psiCodeBlock = psiBlockStatement.getCodeBlock();

            PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();

            PsiExpressionStatement logCall = (PsiExpressionStatement) factory.createStatementFromText("log.info(\"enter please_input_your_branch_name\");",null);

            //如果有log分支，则不打印
            if(psiCodeBlock==null){
                LOG.error("psiCodeBlock is null");
            }
            else{
                PsiStatement psiStatement=psiCodeBlock.getStatements()[0];
                psiCodeBlock.addBefore(logCall,psiStatement);
            }
        }

        @NotNull
        public String getFamilyName() {
            return getName();
        }
    }


    public JComponent createOptionsPanel() {
        return null;
    }

    public boolean isEnabledByDefault() {
        return true;
    }
}
