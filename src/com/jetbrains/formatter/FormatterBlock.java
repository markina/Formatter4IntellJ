package com.jetbrains.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.psi.formatter.common.AbstractBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FormatterBlock extends AbstractBlock {

  @NotNull
  @Override
  public ASTNode getNode() {
    return myNode;
  }


  protected FormatterBlock(@NotNull ASTNode node,
                           @Nullable Wrap wrap,
                           @Nullable Alignment alignment) {
    super(node, wrap, alignment);
  }

  @NotNull
  @Override
  public List<Block> getSubBlocks() {
    return new ArrayList<>();
  }

  @Override
  protected List<Block> buildChildren() {
    return null;
  }

  @Nullable
  @Override
  public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    return null;
  }

  @Override
  public boolean isLeaf() {
    return false;
  }
}
