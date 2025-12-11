// generated with ast extension for cup
// version 0.8
// 21/6/2025 16:47:20


package rs.ac.bg.etf.pp1.ast;

public class StatementSingleDoWHILE extends StatementSingle {

    private Do Do;
    private Statement Statement;
    private While While;
    private WhileStatement WhileStatement;

    public StatementSingleDoWHILE (Do Do, Statement Statement, While While, WhileStatement WhileStatement) {
        this.Do=Do;
        if(Do!=null) Do.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.While=While;
        if(While!=null) While.setParent(this);
        this.WhileStatement=WhileStatement;
        if(WhileStatement!=null) WhileStatement.setParent(this);
    }

    public Do getDo() {
        return Do;
    }

    public void setDo(Do Do) {
        this.Do=Do;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public While getWhile() {
        return While;
    }

    public void setWhile(While While) {
        this.While=While;
    }

    public WhileStatement getWhileStatement() {
        return WhileStatement;
    }

    public void setWhileStatement(WhileStatement WhileStatement) {
        this.WhileStatement=WhileStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Do!=null) Do.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(While!=null) While.accept(visitor);
        if(WhileStatement!=null) WhileStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Do!=null) Do.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(While!=null) While.traverseTopDown(visitor);
        if(WhileStatement!=null) WhileStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Do!=null) Do.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(While!=null) While.traverseBottomUp(visitor);
        if(WhileStatement!=null) WhileStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementSingleDoWHILE(\n");

        if(Do!=null)
            buffer.append(Do.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(While!=null)
            buffer.append(While.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(WhileStatement!=null)
            buffer.append(WhileStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementSingleDoWHILE]");
        return buffer.toString();
    }
}
