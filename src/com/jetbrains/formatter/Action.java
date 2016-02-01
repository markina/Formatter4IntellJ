package com.jetbrains.formatter;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;


public class Action extends AnAction {

  public Action() {
    super("Text _Boxes");
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    Project project = e.getProject();

    if (project == null) {
      return;
    }

    //FileEditor[] files = FileEditorManager.getInstance(project).getAllEditors();
    //Editor current_file = FileEditorManager.getInstance(project).getSelectedTextEditor();

    Messages.showMessageDialog(project, "+", "Information", Messages.getInformationIcon());

    PsiFile psi_current_file = e.getData(DataKeys.PSI_FILE);

    if (psi_current_file == null) {
      return;
    }


    print_level(psi_current_file, 0);
  }

  private void print_level(PsiElement file, int n) {
    for (PsiElement elem : file.getChildren()) {
      for (int i = 0; i < n; i++) {
        System.out.print(" ");
      }
      System.out.println(elem);
      for (int i = 0; i < n; i++) {
        System.out.print(" ");
      }
      System.out.println("Text: '" + elem.getText() + "'");
      print_level(elem, n + 2);
    }
  }
}

