// generated with ast extension for cup
// version 0.8
// 21/6/2025 16:47:20


package rs.ac.bg.etf.pp1.ast;

public class StatementListExpand extends StatementList {

    private StatementList StatementList;
    private StatementSingle StatementSingle;

    public StatementListExpand (StatementList StatementList, StatementSingle StatementSingle) {
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
        this.StatementSingle=StatementSingle;
        if(StatementSingle!=null) StatementSingle.setParent(this);
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
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
        if(StatementList!=null) StatementList.accept(visitor);
        if(StatementSingle!=null) StatementSingle.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
        if(StatementSingle!=null) StatementSingle.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        if(StatementSingle!=null) StatementSingle.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementListExpand(\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementSingle!=null)
            buffer.append(StatementSingle.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementListExpand]");
        return buffer.toString();
    }
}
