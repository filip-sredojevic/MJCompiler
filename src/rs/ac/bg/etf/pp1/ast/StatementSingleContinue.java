// generated with ast extension for cup
// version 0.8
// 21/6/2025 16:47:20


package rs.ac.bg.etf.pp1.ast;

public class StatementSingleContinue extends StatementSingle {

    public StatementSingleContinue () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementSingleContinue(\n");

        buffer.append(tab);
        buffer.append(") [StatementSingleContinue]");
        return buffer.toString();
    }
}
