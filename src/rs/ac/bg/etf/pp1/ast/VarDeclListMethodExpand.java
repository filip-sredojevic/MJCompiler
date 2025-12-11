// generated with ast extension for cup
// version 0.8
// 21/6/2025 16:47:20


package rs.ac.bg.etf.pp1.ast;

public class VarDeclListMethodExpand extends VarDeclListMethod {

    private VarDeclListMethod VarDeclListMethod;
    private VarDeclList VarDeclList;

    public VarDeclListMethodExpand (VarDeclListMethod VarDeclListMethod, VarDeclList VarDeclList) {
        this.VarDeclListMethod=VarDeclListMethod;
        if(VarDeclListMethod!=null) VarDeclListMethod.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
    }

    public VarDeclListMethod getVarDeclListMethod() {
        return VarDeclListMethod;
    }

    public void setVarDeclListMethod(VarDeclListMethod VarDeclListMethod) {
        this.VarDeclListMethod=VarDeclListMethod;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclListMethod!=null) VarDeclListMethod.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclListMethod!=null) VarDeclListMethod.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclListMethod!=null) VarDeclListMethod.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclListMethodExpand(\n");

        if(VarDeclListMethod!=null)
            buffer.append(VarDeclListMethod.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclListMethodExpand]");
        return buffer.toString();
    }
}
