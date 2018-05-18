package edu.nju.codeInspection;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;
import java.util.StringTokenizer;

public class SwitchInspection extends BaseJavaLocalInspectionTool {
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
        return "重要switch需要记日志";
    }

    @NotNull
    public String getGroupDisplayName() {
        return GroupNames.BUGS_GROUP_NAME;
    }

    //对应html

    @NotNull
    public String getShortName() {
        return "Switch";
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
            public void visitSwitchStatement(PsiSwitchStatement psiSwitchStatement) {

                super.visitSwitchStatement(psiSwitchStatement);

                PsiExpression psiExpression = psiSwitchStatement.getExpression();
                PsiCodeBlock codeBlock=psiSwitchStatement.getBody();

                if(psiExpression==null || codeBlock==null){
                    return;
                }

                LOG.info("hi");

                //如果switch语句中有log语句，则不打
                PsiStatement[] psiStatements = codeBlock.getStatements();
                if (psiStatements.length != 0) {
                    for (PsiStatement psiStatement : psiStatements) {

                        if (psiStatement instanceof PsiExpressionStatement) {
                            PsiExpression expression = ((PsiExpressionStatement) psiStatement).getExpression();
                            //判断是否是函数调用
                            if (expression instanceof PsiMethodCallExpression) {
                                //是否有log
                                if (Objects.requireNonNull(((PsiMethodCallExpression) expression).getMethodExpression().getQualifierExpression()).getText().equals("log")) {
                                    return;
                                }
                            }
                        }
                    }
                }


                holder.registerProblem( psiExpression,
                        "switch need to log", myQuickFix);

                int a=0;
                switch (a){
                    case '0':
                        System.out.println("hi");
                        System.out.println("hi");
                        break;
                    case '1':
                        System.out.println("1");
                        break;
                }

            }
        };
    }

    private static class MyQuickFix implements LocalQuickFix {
        @NotNull
        public String getName() {
            // The test (see the TestThisPlugin class) uses this string to identify the quick fix action.
//            return InspectionsBundle.message("inspection.comparing.references.use.quickfix");

            return "重要分支branch可log";
        }


        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            try {
                PsiSwitchStatement psiSwitchStatement=(PsiSwitchStatement)descriptor.getPsiElement().getParent();
                PsiCodeBlock codeBlock=psiSwitchStatement.getBody();
                PsiElement[] psiElements=codeBlock.getChildren();

                for(PsiElement psiElement:psiElements){
                    if(psiElement instanceof PsiSwitchLabelStatement){

                        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
                        PsiExpressionStatement logCall;

                        //获取switch各分支的value
                        PsiExpression psiExpression =((PsiSwitchLabelStatement) psiElement).getCaseValue();

                        if(psiExpression==null){
                            //进入default
                            logCall = (PsiExpressionStatement) factory.createStatementFromText("log.info(\"enter default  please_input_your_branch_name \");",null);
                        }
                        else{
                            //进入case分支
                           logCall = (PsiExpressionStatement) factory.createStatementFromText("log.info(\"enter case"+psiExpression.getText()+" please_input_your_branch_name \");",null);

                        }
                        codeBlock.addAfter(logCall,psiElement);
                    }
                }
            } catch (IncorrectOperationException e) {
                LOG.error(e);
            }
        }

        @NotNull
        public String getFamilyName() {
            return getName();
        }
    }

    public JComponent createOptionsPanel() {
//        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        final JTextField checkedClasses = new JTextField(CHECKED_CLASSES);
//        checkedClasses.getDocument().addDocumentListener(new DocumentAdapter() {
//            public void textChanged(DocumentEvent event) {
//                CHECKED_CLASSES = checkedClasses.getText();
//            }
//        });
//
//        panel.add(checkedClasses);
//        return panel;
        return null;
    }

    public boolean isEnabledByDefault() {
        return true;
    }
}
