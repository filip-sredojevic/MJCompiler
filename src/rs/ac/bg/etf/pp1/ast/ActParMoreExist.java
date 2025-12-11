// generated with ast extension for cup
// version 0.8
// 21/6/2025 16:47:20


package rs.ac.bg.etf.pp1.ast;

public class ActParMoreExist extends ActParMore {

    private ActPar ActPar;
    private ActParMore ActParMore;

    public ActParMoreExist (ActPar ActPar, ActParMore ActParMore) {
        this.ActPar=ActPar;
        if(ActPar!=null) ActPar.setParent(this);
        this.ActParMore=ActParMore;
        if(ActParMore!=null) ActParMore.setParent(this);
    }

    public ActPar getActPar() {
        return ActPar;
    }

    public void setActPar(ActPar ActPar) {
        this.ActPar=ActPar;
    }

    public ActParMore getActParMore() {
        return ActParMore;
    }

    public void setActParMore(ActParMore ActParMore) {
        this.ActParMore=ActParMore;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActPar!=null) ActPar.accept(visitor);
        if(ActParMore!=null) ActParMore.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActPar!=null) ActPar.traverseTopDown(visitor);
        if(ActParMore!=null) ActParMore.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActPar!=null) ActPar.traverseBottomUp(visitor);
        if(ActParMore!=null) ActParMore.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActParMoreExist(\n");

        if(ActPar!=null)
            buffer.append(ActPar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParMore!=null)
            buffer.append(ActParMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActParMoreExist]");
        return buffer.toString();
    }
}
