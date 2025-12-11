// generated with ast extension for cup
// version 0.8
// 21/6/2025 16:47:20


package rs.ac.bg.etf.pp1.ast;

public class ExprDesignator extends Expr {

    private Designator Designator;
    private DesignatorArrayName DesignatorArrayName;

    public ExprDesignator (Designator Designator, DesignatorArrayName DesignatorArrayName) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DesignatorArrayName=DesignatorArrayName;
        if(DesignatorArrayName!=null) DesignatorArrayName.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public DesignatorArrayName getDesignatorArrayName() {
        return DesignatorArrayName;
    }

    public void setDesignatorArrayName(DesignatorArrayName DesignatorArrayName) {
        this.DesignatorArrayName=DesignatorArrayName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(DesignatorArrayName!=null) DesignatorArrayName.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DesignatorArrayName!=null) DesignatorArrayName.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DesignatorArrayName!=null) DesignatorArrayName.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprDesignator(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorArrayName!=null)
            buffer.append(DesignatorArrayName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprDesignator]");
        return buffer.toString();
    }
}
