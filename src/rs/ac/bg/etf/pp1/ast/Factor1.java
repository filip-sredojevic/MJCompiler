// generated with ast extension for cup
// version 0.8
// 21/6/2025 16:47:20


package rs.ac.bg.etf.pp1.ast;

public class Factor1 extends Factor {

    private FactorValue FactorValue;

    public Factor1 (FactorValue FactorValue) {
        this.FactorValue=FactorValue;
        if(FactorValue!=null) FactorValue.setParent(this);
    }

    public FactorValue getFactorValue() {
        return FactorValue;
    }

    public void setFactorValue(FactorValue FactorValue) {
        this.FactorValue=FactorValue;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FactorValue!=null) FactorValue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FactorValue!=null) FactorValue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FactorValue!=null) FactorValue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Factor1(\n");

        if(FactorValue!=null)
            buffer.append(FactorValue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Factor1]");
        return buffer.toString();
    }
}
