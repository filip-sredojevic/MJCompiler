// generated with ast extension for cup
// version 0.8
// 21/6/2025 16:47:20


package rs.ac.bg.etf.pp1.ast;

public class StatementOne extends Statement {

    private StatementSingle StatementSingle;

    public StatementOne (StatementSingle StatementSingle) {
        this.StatementSingle=StatementSingle;
        if(StatementSingle!=null) StatementSingle.setParent(this);
    }

    public StatementSingle getStatementSingle() {
        return StatementSingle;
    }

    public void setStatementSingle(StatementSingle StatementSingle) {
        this.StatementSingle=StatementSingle;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatementSingle!=null) StatementSingle.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementSingle!=null) StatementSingle.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementSingle!=null) StatementSingle.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementOne(\n");

        if(StatementSingle!=null)
            buffer.append(StatementSingle.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementOne]");
        return buffer.toString();
    }
}
