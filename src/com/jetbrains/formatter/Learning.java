package com.jetbrains.formatter;

import com.intellij.formatting.ASTBlock;
import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingDocumentModel;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.formatter.FormattingDocumentModelImpl;
import com.intellij.psi.formatter.java.JavaBlock;
import com.intellij.psi.impl.DebugUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rita on 18.02.16.
 */
public class Learning {
  List<LearningInfo> listLearningInfos = new ArrayList<>();

  public void printLearningList() {
    for(LearningInfo info: listLearningInfos) {
      System.out.println(info);
    }
  }

  class LearningInfo {
    ASTNode parent;
    ASTNode left;
    ASTNode node;
    ASTNode right;

    public LearningInfo(ASTNode parent, ASTNode node) {
      this.parent = parent;
      this.node = node;
    }

    @Override
    public String toString() {
      return "LearningInfo{" +
             "parent=" + parent +
             ", node=" + node +
             '}';
    }
  }

  public static void assertInvalidRanges(final int startOffset, final int newEndOffset, FormattingDocumentModel model, String message) {
    @NonNls final StringBuilder buffer = new StringBuilder();
    buffer.append("Invalid formatting blocks:").append(message).append("\n");
    buffer.append("Start offset:");
    buffer.append(startOffset);
    buffer.append(" end offset:");
    buffer.append(newEndOffset);
    buffer.append("\n");

    int minOffset = Math.max(Math.min(startOffset, newEndOffset) - 20, 0);
    int maxOffset = Math.min(Math.max(startOffset, newEndOffset) + 20, model.getTextLength());

    buffer.append("Affected text fragment:[").append(minOffset).append(",").append(maxOffset).append("] - '")
        .append(model.getText(new TextRange(minOffset, maxOffset))).append("'\n");

    final StringBuilder messageBuffer = new StringBuilder();
    messageBuffer.append("Invalid ranges during formatting");
    if (model instanceof FormattingDocumentModelImpl) {
      messageBuffer.append(" in ").append(((FormattingDocumentModelImpl)model).getFile().getLanguage());
    }

    buffer.append("File text:(").append(model.getTextLength()).append(")\n'");
    buffer.append(model.getText(new TextRange(0, model.getTextLength())).toString());
    buffer.append("'\n");
    buffer.append("model (").append(model.getClass()).append("): ").append(model);

    Throwable currentThrowable = new Throwable();
    if (model instanceof FormattingDocumentModelImpl) {
      final FormattingDocumentModelImpl modelImpl = (FormattingDocumentModelImpl)model;
      buffer.append("Psi Tree:\n");
      final PsiFile file = modelImpl.getFile();
      final List<PsiFile> roots = file.getViewProvider().getAllFiles();
      for (PsiFile root : roots) {
        buffer.append("Root ");
        DebugUtil.treeToBuffer(buffer, root.getNode(), 0, false, true, true, true);
      }
      buffer.append('\n');
      //currentThrowable = makeLanguageStackTrace(currentThrowable, file);
    }

    //LogMessageEx.error(LOG, messageBuffer.toString(), currentThrowable, buffer.toString());
  }

  public List<LearningInfo> getLearningListInfo(Block rootBlock) {
    getLearningInfo(rootBlock, null);
    return this.listLearningInfos;
  }


  public void getLearningInfo(Block curBlock, @Nullable final Block parentBlock) {
    //TextRange textRange = curBlock.getTextRange();
    //final int blockStartOffset = textRange.getStartOffset();

    //CompositeBlockWrapper parent = null;
    //final FormattingDocumentModel myModel = null;

    //if (parent != null) {
    //  if (textRange.getStartOffset() < parent.getStartOffset()) {
    //    assertInvalidRanges(
    //        textRange.getStartOffset(),
    //        parent.getStartOffset(),
    //        myModel,
    //        "child block start is less than parent block start"
    //    );
    //  }
    //
    //  if (textRange.getEndOffset() > parent.getEndOffset()) {
    //    assertInvalidRanges(
    //        textRange.getEndOffset(),
    //        parent.getEndOffset(),
    //        myModel,
    //        "child block end is after parent block end"
    //    );
    //  }
    //}

    //IIIWhiteSpace myCurrentWhiteSpace = new IIIWhiteSpace(getStartOffset(rootBlock), true);


    ASTNode parentNode = null;
    ASTNode curNode = null;

    if (parentBlock instanceof ASTBlock) {
      ASTBlock astParentBlock = (ASTBlock)parentBlock;
      parentNode = astParentBlock.getNode();
    } else if (parentBlock instanceof JavaBlock) {
      JavaBlock astParentBlock = (JavaBlock)parentBlock;
      parentNode = astParentBlock.getFirstTreeNode();
    }

    if (curBlock instanceof ASTBlock) {
      ASTBlock astCurBlock = (ASTBlock)curBlock;
      curNode = astCurBlock.getNode();
    } else if (curBlock instanceof JavaBlock) {
      JavaBlock astCurBlock = (JavaBlock)curBlock;
      curNode = astCurBlock.getFirstTreeNode();
    }
    this.listLearningInfos.add(new LearningInfo(parentNode, curNode));

    for (Block block : curBlock.getSubBlocks()) {
      getLearningInfo(block, curBlock);
    }



  }
}
