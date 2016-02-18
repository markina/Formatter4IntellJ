package com.jetbrains.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Learning {
  List<LearningInfo> listLearningInfos = new ArrayList<>();

  public void printLearningList() {
    for (LearningInfo info : listLearningInfos) {
      System.out.println(info);
    }
  }

  class LearningInfo {
    ASTNode parent;
    ASTNode left;
    ASTNode node;
    ASTNode right;
    Wrap wrap;
    Indent indent;
    Alignment alignment;

    public LearningInfo(ASTNode parent, ASTNode node) {
      this.parent = parent;
      this.node = node;
    }

    public LearningInfo(ASTNode parent,
                        ASTNode node,
                        Wrap wrap,
                        Indent indent,
                        Alignment alignment) {
      this.parent = parent;
      this.node = node;
      this.wrap = wrap;
      this.indent = indent;
      this.alignment = alignment;
    }

    @Override
    public String toString() {
      return "LearningInfo{" +
             "parent=" + parent +
             ", node=" + node +
             ", wrap=" + wrap +
             ", indent=" + indent +
             ", alignment=" + alignment +
             '}';
    }
  }

  public List<LearningInfo> getLearningListInfo(Block rootBlock) {
    getLearningInfo(rootBlock, null);
    return this.listLearningInfos;
  }


  public void getLearningInfo(Block curBlock, @Nullable final Block parentBlock) {
    ASTNode parentNode = null;
    ASTNode curNode = null;

    if (parentBlock instanceof ASTBlock) {
      ASTBlock astParentBlock = (ASTBlock)parentBlock;
      parentNode = astParentBlock.getNode();
    } // TODO add instanceof JavaBlock

    if (curBlock instanceof ASTBlock) {
      ASTBlock astCurBlock = (ASTBlock)curBlock;
      curNode = astCurBlock.getNode();
    } // TODO add instanceof JavaBlock

    //this.listLearningInfos.add(new LearningInfo(parentNode, curNode));
    this.listLearningInfos.add(new LearningInfo(parentNode, curNode, curBlock.getWrap(), curBlock.getIndent(), curBlock.getAlignment()));

    assert curBlock != null;
    for (Block block : curBlock.getSubBlocks()) {
      getLearningInfo(block, curBlock);
    }
  }
}
