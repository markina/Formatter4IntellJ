package com.jetbrains.formatter;


import com.intellij.ide.highlighter.ProjectFileType;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiDocumentManagerImpl;
import com.intellij.psi.impl.PsiFileFactoryImpl;
import com.intellij.psi.impl.PsiManagerImpl;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.testFramework.PlatformTestCase;
import com.intellij.util.LocalTimeCounter;
import junit.framework.TestCase;
import org.jetbrains.annotations.NonNls;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class testFormatter extends TestCase {
  protected static Project ourProject;
  @NonNls private static final String LIGHT_PROJECT_MARK = "Light project: ";

  public static Project getProject() {
    return ourProject;
  }

  protected FileType getFileType(String fileName) {
    return FileTypeManager.getInstance().getFileTypeByFileName(fileName);
  }

  protected PsiFile createFileFromText(String text, String fileName, final PsiFileFactory fileFactory) {
    return fileFactory.createFileFromText(fileName, getFileType(fileName), text, LocalTimeCounter.currentTime(), true, false);
  }

  void doTest(String path) throws IOException {
    File subFile = new File(path);
    if (subFile.isFile() && subFile.getName().endsWith(".java")) {
      final byte[] bytes = FileUtil.loadFileBytes(subFile);
      final String text = new String(bytes);

      System.out.print(text);
      final File projectFile = FileUtil.createTempFile("prefix", "suffix");
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      new Throwable(projectFile.getPath()).printStackTrace(new PrintStream(buffer));
      ourProject = PlatformTestCase.createProject(projectFile, LIGHT_PROJECT_MARK + buffer.toString());

      final PsiFile file = createFileFromText(text, "file", PsiFileFactory.getInstance(getProject()));

      System.out.print(file);

    }
  }

  public void testFirst() throws Exception {
    doTest("./testData/java/actions/reformatFileInEditor/formatOptimizeRearrangeWholeFile_after.java");
  }
}
