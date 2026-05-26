package com.codeCLi;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CliRender extends AbstractVisitor {
    private final StringBuilder sb = new StringBuilder();

    static final String BOLD = "\u001B[1m";
    static final String ITALIC = "\u001B[3m";
    static final String CODE = "\u001B[36m"; // cyan
    static final String RESET = "\u001B[0m";
    static final String H1 = "\u001B[1;33m"; // bold yellow
    static final String H2 = "\u001B[1;32m"; // bold green

    public static String renderResponse(String text) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(text);
        CliRender renderer = new CliRender();
        document.accept(renderer);
        return renderer.sb.toString();
    }

    @Override
    public void visit(Heading h) {
        sb.append(h.getLevel() == 1 ? H1 : H2);
        visitChildren(h);
        sb.append(RESET).append("\n");
    }

    @Override
    public void visit(StrongEmphasis e) {
        sb.append(BOLD);
        visitChildren(e);
        sb.append(RESET);
    }

    @Override
    public void visit(Emphasis e) {
        sb.append(ITALIC);
        visitChildren(e);
        sb.append(RESET);
    }

    @Override
    public void visit(Code c) {
        sb.append(CODE).append(c.getLiteral()).append(RESET);
    }

    @Override
    public void visit(FencedCodeBlock b) {
        sb.append(CODE).append(b.getLiteral()).append(RESET).append("\n");
    }

    @Override
    public void visit(Text t) {
        sb.append(t.getLiteral());
    }

    @Override
    public void visit(SoftLineBreak n) {
        sb.append("\n");
    }

    @Override
    public void visit(HardLineBreak n) {
        sb.append("\n\n");
    }


}
