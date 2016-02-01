package com.jetbrains.formatter;

import com.intellij.formatting.Block;
import com.intellij.formatting.FormatTextRanges;
import com.intellij.formatting.FormatterEx;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.JavaCodeStyleSettings;
import com.intellij.psi.formatter.DocumentBasedFormattingModel;
import com.intellij.psi.formatter.FormatterTestCase;
import com.intellij.psi.formatter.FormattingDocumentModelImpl;
import com.intellij.psi.formatter.java.AbstractJavaBlock;
import com.intellij.psi.impl.source.SourceTreeToPsiMap;
import com.intellij.psi.impl.source.codeStyle.PsiBasedFormatterModelWithShiftIndentInside;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.jetbrains.formatter.idea.IIIAbstractJavaBlock;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

//import com.jetbrains.formatter.idea.IIIAbstractJavaBlock;

public class BlockFormattingTest extends FormatterTestCase {
  @Override
  protected String getBasePath() {
    return "java/actions/reformatFileInEditor";
  }

  @Override
  protected String getFileExtension() {
    return "java";
  }

  @Override
  protected String getTestDataPath() {
    return "/home/rita/studies/br/Formatter4IntelliJ/testData/";
  }

  @NotNull
  private static CommonCodeStyleSettings.IndentOptions getJavaIndentOptions(CommonCodeStyleSettings settings) {
    CommonCodeStyleSettings.IndentOptions indentOptions = settings.getIndentOptions();

    assert indentOptions != null : "Java indent options are not initialized";

    return indentOptions;
  }

  public void testFormatTest() throws Exception {


    // From FormatterTestCase
    String fileNameBefore = this.getTestName(true) + "." + this.getFileExtension();
    String fileNameAfter = this.getTestName(true) + "_after." + this.getFileExtension();

    String resultNumber = (String)null;

    String text = this.loadFile(fileNameBefore, (String)null);
    String textAfter = this.loadFile(fileNameAfter, resultNumber);

    String fileName = "before." + this.getFileExtension();
    PsiFile file = this.createFileFromText(text, fileName, PsiFileFactory.getInstance(getProject()));

    final Document document = PsiDocumentManager.getInstance(getProject()).getDocument(file);

    final CodeStyleSettings mySettings = CodeStyleSettingsManager.getSettings(getProject());
    int startOffset = file.getTextRange().getStartOffset();
    int endOffset = file.getTextRange().getEndOffset();

    Collection correctedRanges = Collections.singleton(new TextRange(startOffset, endOffset));


    FormatTextRanges ranges = new FormatTextRanges();
    Iterator rangeIterator = correctedRanges.iterator();

    while (rangeIterator.hasNext()) {
      TextRange textRange = (TextRange)rangeIterator.next();
      ranges.add(textRange, true);
    }

    List textRanges = ranges.getRanges();

    ////
    AbstractJavaBlock r; // for ref

    FileElement fileElement = TreeUtil.getFileElement((TreeElement)SourceTreeToPsiMap.psiElementToTree(file));
    //LOG.assertTrue(fileElement != null, "File element should not be null for " + element);
    CommonCodeStyleSettings commonSettings = mySettings.getCommonSettings(JavaLanguage.INSTANCE);
    JavaCodeStyleSettings customJavaSettings = (JavaCodeStyleSettings)mySettings.getCustomSettings(JavaCodeStyleSettings.class);
    Block block = IIIAbstractJavaBlock.newJavaBlock(fileElement, commonSettings, customJavaSettings);

    FormattingDocumentModelImpl model = FormattingDocumentModelImpl.createOn(file.getContainingFile());

    final DocumentBasedFormattingModel formattingModel
        = new DocumentBasedFormattingModel(
        new PsiBasedFormatterModelWithShiftIndentInside(file.getContainingFile(), block, model),
        document, getProject(), mySettings, file.getFileType(), file);

    //DocumentBasedFormattingModel
    //    formattingModel =
    //    new DocumentBasedFormattingModel(new JavaFormattingModelBuilder().createModel(file, mySettings), document, getProject(), mySettings, file.getFileType(), file);


    final FormatterEx formatterEx = FormatterEx.getInstanceEx();

    CommonCodeStyleSettings.IndentOptions var31 = mySettings
        .getIndentOptionsByFile(file, textRanges.size() == 1 ? ((FormatTextRanges.FormatTextRange)textRanges.get(0)).getTextRange() : null);

    WriteCommandAction.runWriteCommandAction(getProject(), new Runnable() {
      @Override
      public void run() {
        formatterEx.format(formattingModel, mySettings, var31, ranges, false);
      }
    });


    assertEquals(textAfter, document.getText());
    PsiDocumentManager.getInstance(getProject()).commitDocument(document);
    assertEquals(textAfter, file.getText());
  }
}
