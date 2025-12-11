package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor {

	private boolean errorDetected =false;
	
	private Obj Prog;
	private Struct currType;
	
	private int currLevel=0; 
	
	private int currConstValue;
	private Struct currConstValueType;
	
	private Obj currMethod;
	private int numberMethodParams=0;
	private boolean mainExists=false;
	
	private boolean returnExists=false;
	private Struct returnExprType;
	
	private int inDoWhileLvl=0; //I Sava ovako uradio
	
	ArrayList<Struct> actParamsTypes = new ArrayList<>();
	
	Logger log = Logger.getLogger(getClass());

	 int nVars;
	
	/* LOG MESSAGES */
	public void report_error(String message, SyntaxNode info) {
		errorDetected  = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public boolean passed() {
		return !errorDetected;
	}
	
	//OBILAZAK CVOROVA
		
	//POPUNJAVANJE TABELE SIMBOLA
	
	 @Override 	
	 public void visit(ProgramName progname)
	{
		
		Prog = Tab.insert(Obj.Prog, progname.getI1(), Tab.noType);
		Prog.setAdr(-1);Prog.setLevel(-1);
		Tab.openScope();
	}
	 @Override
     public void visit(Program prog)
	{ 
		nVars=Tab.currentScope().getnVars();
		 if(!mainExists)
			 report_error("U programu mora postojati main metoda." + " Greska", prog);
		 
		  Tab.chainLocalSymbols(Prog);
	      Tab.closeScope();
	      Prog=null;
	}
	 
	 //Obilazak cvora Type
	 
	 @Override public void visit(Type type)
		{
		
			String typeName=type.getI1();
			Obj typeObj = Tab.find(typeName);
			
			if(typeObj == Tab.noObj)
			{
				report_error("Nepostojeci tip podatka: " + typeName + ". Greska", type);
				currType=Tab.noType;
			}
			else if (typeObj.getKind()!= Obj.Type)
			{
				report_error("Neadekvatan tip podatka: " + typeName + ". Greska", type);
				currType=Tab.noType;
			}
			else {
				if(typeObj.getName().equals("set") && currMethod!=null)
				{
				     currType=Tab.intType;	
				     report_info("idemo",type);
				}
				
				else {
					currType=typeObj.getType();
				}
			}
			type.struct=currType;
			
		
		}
	 
	
	 //KONSTANTE (MORAJU BITI GLOBALNE)
	 
	@Override public void visit(ConDecl con)
	{
		 Obj temp=Tab.find(con.getI1());
		 if(temp==Tab.noObj) {
 
			 if(currConstValueType.assignableTo(currType)==false)
			 {
				 report_error("Nekompatibilni tipovi!" + " Greska", con);
				 return;
			 }
	        temp = Tab.insert(Obj.Con, con.getI1(), currType);
	        temp.setAdr(currConstValue); temp.setLevel(0);
		 }
		 else {
			 if(temp.getKind()==Obj.Con) //Konstante mogu biti samo globalne, pa ne moramo da proveravamo opseg
			 {
				   report_error("Visestruka definicja konstante " + con.getI1()+ ". Greska", con);
					currType=Tab.noType;
			 }
		 }
	}
	
	@Override public void visit(NumConst val)
	{
		currConstValue=val.getN1();
		currConstValueType=Tab.intType;
	}
	@Override public void visit(CharConst val)
	{
		currConstValue=val.getC1(); //Implicitna konverzija iz char u int
		currConstValueType=Tab.charType;
	}
	@Override public void visit(BoolConst val)
	{
		//bool smo cuvali kao Integer da bismo ovde lakse sacuvali vrednost
		currConstValue=val.getB1();
		currConstValueType=Compiler.boolType;
	}
	
	
	//GLOBALNE PROMENLJIVE
	
	@Override public void visit(VarDeclVar var)
	{
		Obj temp=Tab.find(var.getI1()); 
		if(temp!=Tab.noObj)
		{
			if(temp.getLevel()==currLevel) {
				report_error("Visestruka deklaracija promenljive u istom opsegu" + var.getI1() + ". Greska", var);
				return;
			}
		}
		
			//Nema provere tipova jer se vrednost ne moze dodeliti globalnoj promenljivoj, tako je definisano gramatikom
		    temp=Tab.insert(Obj.Var, var.getI1(), currType);
		    //AUTOMATSKI temp.setAdr(x);
		    temp.setLevel(currLevel); //globalna promenljiva
		    		
		
	}
	
	@Override public void visit(VarDeclArray var)
	{
		
		Obj temp=Tab.find(var.getI1());
		if(temp!=Tab.noObj)
		{
			if(temp.getLevel()==currLevel) {
				report_error("Visestruka deklaracija promenljive u istom opsegu " + var.getI1()+ ". Greska", var);
				return;
			}
		}
		  //Ne trba provera tipova jer nema dodele vrednosti, tako je definisano gramatikom
		    temp=Tab.insert(Obj.Var, var.getI1(), new Struct(Struct.Array, currType));
		    temp.setLevel(currLevel); 
		  //AUTOMATSKI temp.setAdr(x);
		
		
	}
	
	//METODE
	
	@Override public void visit(MethodRetTypeNameNoVoid met)
	{
		returnExists=false;
		
		if(met.getI2().equals("main"))
		{
			report_error("Metoda main kao povratni tip mora imati void."+ " Greska", met);
		}
		if(met.getI2().equals("main"))mainExists=true;
		Obj temp=Tab.find(met.getI2());
		if(temp!=Tab.noObj)
		{
			if(temp.getLevel()==currLevel) {
				report_error("Visestruka deklaracija promenljive u istom opsegu " + met.getI2()+ ". Greska", met);
				return;
			}
		}
		met.obj=currMethod= Tab.insert(Obj.Meth, met.getI2(), currType);
		currMethod.setAdr(0);
		//setLevel ce  postaviti drugi Visit
		Tab.openScope();
		currLevel=1;
	}
	
	@Override public void visit(MethodRetTypeNameVoid met)
	{
		returnExists=false;
		if(met.getI1().equals("main"))mainExists=true;
		Obj temp=Tab.find(met.getI1());
		if(temp!=Tab.noObj)
		{
			if(temp.getLevel()==currLevel) {
				report_error("Visestruka deklaracija promenljive u istom opsegu " + met.getI1() + ". Greska", met);
				return;
			}
		}
		met.obj = currMethod= Tab.insert(Obj.Meth, met.getI1(), Tab.noType);
		currMethod.setFpPos(0);
	    
	    Tab.openScope();
	    currLevel=1;
	    
	}
	
	
	@Override public void visit(MethodDecl met)
	{
		
		currMethod.setLevel(numberMethodParams);
		numberMethodParams=0;
		Tab.chainLocalSymbols(currMethod);
		Tab.closeScope();
		
		if(!currMethod.getType().equals(Tab.noType))
		{
			if(!returnExists) {
				report_error("Semanticka greska - funkcija "+ currMethod.getName()+ " mora imati return naredbu." + " Greska",met);
			}
			else {
				if(!currMethod.getType().equals(returnExprType)) {
					report_error("Semanitcka greska - u funkciji "+currMethod.getName() + " tip argumenta return naredbe nije odgovarajuci." + " Greska",met);
				}
			}
		}
		else {
			if(returnExists)
			{
				report_error("Semanticka greska - funkcija "+ currMethod.getName()+ " ne sme imati return naredbu." + " Greska",met);
			}
		}
		
		
		currMethod=null; 
		currLevel=0;
		
		
		
		
		
	}
	
	
	//FORMALNI PARAMETRI METODE 
	@Override public void visit(FormParVar fp)
	{
		if(currMethod.getName().equals("main"))
		{
			report_error("Metoda main ne moze imati parametre"+ " Greska", fp);
			return;
		}
		numberMethodParams++;
		Obj temp=Tab.find(fp.getI2());
		if(temp!=Tab.noObj)
		{
			if(temp.getLevel()==currLevel) {
				report_error("Semanticka greska, visestruka deklaracija promenljive" + fp.getI2() + " u istom opsegu. " + fp.getI2() + " Greska", fp);
				return;
			}
		}
		temp=Tab.insert(Obj.Var, fp.getI2(), currType);
		//Automatski temp.setAdr(x);
		temp.setLevel(currLevel);
        temp.setFpPos(1); 	
        
	}
	@Override public void visit(FormParArray fp)
	{
		if(currMethod.getName().equals("main"))
		{
			report_error("Semanticka greska, metoda main ne moze imati parametre."+ " Greska", fp);
			return;
		}
		numberMethodParams++;
		Obj temp=Tab.find(fp.getI2());
		if(temp!=Tab.noObj)
		{
			if(temp.getLevel()==currLevel) {
				report_error("Semanticka greska, visestruka deklaracija promenljive" + fp.getI2() + " u istom opsegu." + fp.getI2() + " Greska", fp);
				return;
			}
		}
		temp=Tab.insert(Obj.Var, fp.getI2(), new Struct(Struct.Array, currType));
		//Automatski temp.setAdr(x);
		temp.setLevel(currLevel);
        temp.setFpPos(1); //1 - formalni parametar, 0 - lokalna promenljiva	
				
	}
	
	//************************************************************************************************************************
	//************************************************************************************************************************
	//************************************************************************************************************************
	
	//PROVERA KONTEKSTNIH USLOBA
	
	
	//DEFINISANJE TIPOVA FAKTORIMA
	
	@Override
	public void visit(FactorValueVar fc)
	{
		fc.struct=fc.getDesignator().obj.getType();
		
		Obj obj=fc.getDesignator().obj;
		
		if(obj.getKind()==Obj.Con)
		{
			report_info("Koriscenje konstante "+ obj.getName(), fc);
		}
		else if(obj.getKind()==Obj.Var && obj.getLevel()==0)
		{
			report_info("Koriscenje globalne promenljive "+ obj.getName(), fc);
		}
		else if(obj.getKind()==Obj.Var && obj.getLevel()==1)
		{
			report_info("Koriscenje lokalne promenljive "+ obj.getName(), fc);
		}
		
	}
	
	@Override
	public void visit(FactorValueMethod fm)
	{
		
		boolean errorHap=false;
		if(fm.getDesignator().obj.getKind()!=Obj.Meth)
		{
			report_error("Semanticka greska - neadekvatno koriscenje promenljive pri pozivu funkcije." + " Greska",fm);
			fm.struct=Tab.noType;
			errorHap=true;
		}
		else 
		{
			report_info("Poziv globalne funkcije " + fm.getDesignator().obj.getName(), fm);
			
			Collection<Obj> formParams=fm.getDesignator().obj.getLocalSymbols();
			ArrayList<Struct> formPar=new ArrayList<>();
			
			if(actParamsTypes.size()!=fm.getDesignator().obj.getLevel())
			{
				report_error("Semanticka greska - broj stvarnih parametara mora odgovarati broju formalnih parametara."+ " Greska",fm);
				fm.struct=Tab.noType;
				errorHap=true;
			}
			else {
			
			String metName=fm.getDesignator().obj.getName();
			boolean check=false;
			if(!metName.equals("chr") && !metName.equals("ord") && !metName.equals("len"))check=true;
			for(Obj o: formParams)
			{   if(!check)formPar.add(o.getType());
			    else if(o.getFpPos()==1) formPar.add(o.getType());
			}			
				
				for(int i=0; i<actParamsTypes.size(); i++)
				{
					if(!actParamsTypes.get(i).assignableTo(formPar.get(i)))
					{
						report_error("Semanticka greska - u funkciji "+ currMethod.getName() + " argument " + (i+1) +" nije odgovarajuceg tipa."+ " Greska",fm);
						fm.struct=Tab.noType;
						errorHap=true;
					}
				}
				
			}
		
		}
		
		if(!errorHap)fm.struct=fm.getDesignator().obj.getType();
		actParamsTypes.clear();
	}
	
	@Override
	public void visit(FactorValueCharacter fc)
	{
		fc.struct=Tab.charType;
	}
	
	@Override
	public void visit(FactorValueNumber fn)
	{
		fn.struct=Tab.intType;
	}
	
	@Override
	public void visit(FactorValueBool fb)
	{
		fb.struct=Compiler.boolType;
	}
	@Override
	public void visit(FactorValueArray fa)
	{
		if(!fa.getExpr().struct.equals(Tab.intType))
		{
			report_error("Semanticka greska, velicina niza mora biti ceo broj." + " Greska",fa);
			fa.struct=Tab.noType;
		}
		else {
			
			fa.struct=new Struct(Struct.Array, currType);
		}
	}
	
	@Override
	public void visit(FactorValueExpr fc)
	{
		fc.struct=fc.getExpr().struct;
	}
	
	
	//GURANJE TIPA NAVISE KROZ STABLO
	
	@Override
	public void visit(Factor1 fc)
	{
		if(!fc.getFactorValue().struct.equals(Tab.intType))
		{
			report_error("Semanticka greska, operator - se  moze naci samo isppred celobrojnih vrednosti."+ " Greska",fc);
			fc.struct=Tab.noType;
		}
		else {
			fc.struct=Tab.intType;
		}
	}
	
	@Override
	public void visit(Factor2 fc)
	{
		fc.struct=fc.getFactorValue().struct;
	}
	
	
	//GURANJE TIPA NAVISE KROZ STABLO
	
	@Override
	public void visit(FactorListTerminate ft)
	{
		//Factor moze biti bilo kog tipa jer ovde nema mnozenja 
		ft.struct=ft.getFactor().struct;

	}
	
	//GURANJE TIPA NAVISE KROZ STABLO
	
	@Override
	public void visit(FactorListExpand ft)
	{
		Struct left=ft.getFactorList().struct;
		Struct right=ft.getFactor().struct;
		if(left.equals(Tab.intType) && right.equals(Tab.intType))
		{
			ft.struct=Tab.intType;
		}
		else {
			report_error("Semanticka greska - operandi operatora MULOP moraju biti celi brojevi."+ " Greska",ft);
			ft.struct=Tab.noType;
		}
		
	}
	
	//GURANJE TIPA NAVISE KROZ STABLO
	
	@Override
	public void visit(Term term)
	{
		//SIN Factor JE OBEZVEDIO DA TIP MORA BITI INT 
		term.struct=term.getFactorList().struct;
		
	}
	
	
	
	@Override
	public void visit(TermListExpand term)
	{
		Struct left=term.getTermList().struct;
		Struct right=term.getTerm().struct;
		
		if(left.equals(Tab.intType) && right.equals(Tab.intType))
		{
			term.struct=Tab.intType;
		}
		else {
			report_error("Semanticka greska - operandi operatora ADDOP moraju biti celi brojevi."+ " Greska",term);
			term.struct=Tab.noType;
		}
		
	}
	
	
	@Override
	public void visit(TermListTerminate term)
	{
		term.struct=term.getTerm().struct;
	}
	
	
	@Override
	public void visit(ExprTerm expr)
	{
		expr.struct=expr.getTermList().struct;
	}
	
	@Override
	public void visit(ExprDesignator expr)
	{
		boolean errorHappend=false;
		Obj leftDes=expr.getDesignator().obj;
		Obj rightDes=expr.getDesignatorArrayName().obj;
		
		if(leftDes.getKind()!=Obj.Meth)
		{
			report_error("Semanticka greska - levi operand operatora MAP mora biti funkcija."+ " Greska",expr);
			errorHappend=true;
		}
		else {
			if(!leftDes.getType().equals(Tab.intType))
			{
				report_error("Semanticka greska - levi operand operatora MAP mora biti funkcija koja vraca int."+ " Greska",expr);
				errorHappend=true;
			}
			Collection<Obj> locParams=leftDes.getLocalSymbols();
			if(locParams.size()!=1)
			{
				report_error("Semanticka greska - levi operand operatora MAP mora biti funkcija koja ima tacno jedan parametar."+ " Greska",expr);
				errorHappend=true;
			}
			else {
				for(Obj o: locParams) {
					if(!o.getType().equals(Tab.intType)) {
						report_error("Semanticka greska - levi operand operatora MAP mora biti funkcija koja ima tacno jedan parametar tipa int." + " Greska",expr);
						errorHappend=true;
					}
				}
			}
		}
		
		if(rightDes.getType().getKind()!=Struct.Array)
		{
			report_error("Semanticka greska - desni operand operatora MAP mora biti niz."+ " Greska",expr);
			errorHappend=true;
		}
		else {
			if(!rightDes.getType().getElemType().equals(Tab.intType))
			{
				report_error("Semanticka greska - desni operand operatora MAP mora biti niz celobrojinih vrednosti."+ " Greska",expr);
				errorHappend=true;
			}
		}
		
		if(errorHappend)expr.struct=Tab.noType;
		else expr.struct=Tab.intType;
		
	}
	
	
	
	//U OVAJ VISIT ULAZIMO KADA NAIDJEMO NA KORISCENJE PROMENNJLIVE KOJA NIJE NIZ
	@Override
	public void visit(DesignatorIdent de)
	{
		Obj temp=Tab.find(de.getI1());
	    if(temp==Tab.noObj)
	    {
	    	report_error("Semanticka greska - upotreba nedeklarisanog simbola."+ " Greska",de);
	    	de.obj=Tab.noObj;
	    }
	    else {
	    	if(temp.getKind()!=Obj.Var && temp.getKind()!=Obj.Con && temp.getKind()!=Obj.Meth)
	    	{
	    		report_error("Semanticka greska - neadekvatno koriscenje simbola " + de.getI1() + "."+ " Greska",de);
		    	de.obj=Tab.noObj;
	    	}
	    	else {
	    		de.obj=temp;
	    	}
	    }
	
	
	}
	//U OVAJ VISIT ULAZIMO KADA NAIDJEMO NA KORISCENJE NIZA
	@Override
	public void visit(DesignatorElemArray de)
	{
		report_info("Pristup elementu niza " + de.getDesignatorArrayName().getI1(),de);
		
		if(de.getDesignatorArrayName().obj==Tab.noObj)
		{
			de.obj=Tab.noObj;
			return;
		}
		else if(!de.getExpr().struct.equals(Tab.intType))
		{
			report_error("Semanticka greska - indeks niza mora biti ceo broj."+ " Greska",de);
	    	de.obj=Tab.noObj;
		}
		else {
		de.obj=new Obj(Obj.Elem,"nebitno", de.getDesignatorArrayName().obj.getType().getElemType());
		 }
	}
	
	//U OVAJ VISIT ULAZIMO KADA NAIDJEMO NA KORISCENJE NIZA
	@Override
	public void visit(DesignatorArrayName de)
	{
		Obj temp=Tab.find(de.getI1());
	    if(temp==Tab.noObj)
	    {
	    	report_error("Semanticka greska - upotreba nedeklarisanog simbola" + de.getI1() + "."+ " Greska",de);
	    	de.obj=Tab.noObj;
	    }
	    else {
	    	if( temp.getType().getKind()!=Struct.Array || temp.getKind()!=Obj.Var)
	    	{
	    		report_error("Semanticka greska - neadekvatna promenljiva niza."+ " Greska",de);
		    	de.obj=Tab.noObj;
	    	}
	    	else {
	    		de.obj=temp;
	    	}
	    }
	}
	
	@Override
	public void visit(StatementSingleReturnArg stat)
	{
		returnExists=true;
		returnExprType=stat.getExpr().struct;
	}
	

	
	//DESIGNATOR STATEMENT
	
	@Override
	public void visit(DesignatorStatementAssign de)
	{
		int kind=de.getDesignator().obj.getKind();
		if(kind!=Obj.Var && kind!=Obj.Elem)
		{
			report_error("Semanticka greska - tip sa leve strane znaka = mora biti promenljiva ili element niza."+ " Greska",de);
		}
		else if (!de.getExpr().struct.assignableTo(de.getDesignator().obj.getType()))
		{
			report_error("Semanticka greska - tip leve i desne strane znaka = moraju biti kompatibilni."+ " Greska",de);
		}
		
	}
	
	@Override
	public void visit(DesignatorStatementInc inc)
	{
		int kind=inc.getDesignator().obj.getKind();
		if(kind!=Obj.Var && kind!=Obj.Elem)
		{
			report_error("Semanticka greska - tip sa leve strane znaka = mora biti promenljiva ili element niza."+ " Greska",inc);
		}
		else if (!inc.getDesignator().obj.getType().equals(Tab.intType))
		{
			report_error("Semanticka greska - inkrement neadekvatne promenljive."+ " Greska",inc);
		}
		
		
	}
	
	@Override
	public void visit(DesignatorStatementDec dec)
	{
		int kind=dec.getDesignator().obj.getKind();
		if(kind!=Obj.Var && kind!=Obj.Elem)
		{
			report_error("Semanticka greska - tip sa leve strane znaka = mora biti promenljiva ili element niza."+ " Greska",dec);
		}
		else if (!dec.getDesignator().obj.getType().equals(Tab.intType))
		{
			report_error("Semanticka greska - dekrement neadekvatne promenljive."+ " Greska",dec);
		}
		
		
	}
	
	//Designator mora označavati nestatičku metodu unutrašnje klase ili globalnu funkciju glavnog programa
	
	@Override
	public void visit(DesignatorStatementActPar fm)
	{
		
	
		if(fm.getDesignator().obj.getKind()!=Obj.Meth)
		{
			report_error("Semanticka greska - neadekvatno koriscenje promenljive pri pozivu funkcije."+ " Greska",fm);
		}
		else 
		{
			Collection<Obj> formParams=fm.getDesignator().obj.getLocalSymbols();
			ArrayList<Struct> formPar=new ArrayList<>();
			
			if(actParamsTypes.size()!=fm.getDesignator().obj.getLevel())
			{
				report_error("Semanticka greska - broj stvarnih parametara mora odgovarati broju formalnih parametara."+ " Greska",fm);		
				
			}
			else {
			
			String metName=fm.getDesignator().obj.getName();
			boolean check=false;
			if(!metName.equals("chr") && !metName.equals("ord") && !metName.equals("len"))check=true;
			for(Obj o: formParams)
			{   if(!check)formPar.add(o.getType());
			    else if(o.getFpPos()==1) formPar.add(o.getType());
			}
			
				for(int i=0; i<actParamsTypes.size(); i++)
				{
					if(!actParamsTypes.get(i).assignableTo(formPar.get(i)))
					{
						report_error("Semanticka greska - u funkciji "+ currMethod.getName() + " argument " + (i+1) +" nije odgovarajuceg tipa." + " Greska",fm);
					}
				}
				
			}
			
		}
		actParamsTypes.clear();
	}
	
	@Override
	public void visit(DesignatorStatementSetop de)
	{
		if(!de.getDesignator().obj.getType().equals(Compiler.setType))
		{
			report_error("Semanticka greska - rezultat union operatora mora biti tipa set." + " Greska",de);
		}
		else if(!de.getDesignator1().obj.getType().equals(Compiler.setType))
		{
			report_error("Semanticka greska - levi operand union operatora mora biti tipa set."+ " Greska",de);
		}
		else if(!de.getDesignator2().obj.getType().equals(Compiler.setType))
		{
			report_error("Semanticka greska - desni operand union operatora mora biti tipa set."+ " Greska",de);
		}
	}
	
	
	@Override
	public void visit(Do dos)
	{
		inDoWhileLvl++;	
	}
	
	
	@Override
	public void visit(StatementSingleDoWHILE ss)
	{
		inDoWhileLvl--;
	}
	
	@Override
	public void visit(StatementSingleBreak ss)
	{
		if(inDoWhileLvl<1)
		{
			report_error("Semanticka greska - break naredba mora biti unutar do-while petlje."+ " Greska",ss);
		}
	}
	
	@Override
	public void visit(StatementSingleContinue ss)
	{
		if(inDoWhileLvl<1)
		{
			report_error("Semanticka greska - continue naredba mora biti unutar do-while petlje."+ " Greska",ss);
		}	
	}
	
	@Override
	public void visit(StatementSingleRead ss)
	{
		
		int kind=ss.getDesignator().obj.getKind();
		if(kind!=Obj.Var && kind!=Obj.Elem)
		{
			report_error("Semanticka greska - argument read naredbe mora biti promenljiva ili element niza."+ " Greska",ss);
		}
		else {
			Struct struct=ss.getDesignator().obj.getType();
			if(!struct.equals(Tab.intType) && !struct.equals(Tab.charType) && !struct.equals(Compiler.boolType))
            {
	
				report_error("Semanticka greska - argument read naredbe mora tipa int, char ili bool."+ " Greska",ss);
           }
		}
	}
	
	
	
	@Override
	public void visit(StatementSinglePrint1 pr)
	{
		
	  Struct type=pr.getExpr().struct;
	  if(!type.equals(Tab.intType) && !type.equals(Tab.charType) && !type.equals(Compiler.boolType)  && !type.equals(Compiler.setType) )
	  {
		  report_error("Semanticka greska - argument print naredbe mora tipa int, char, bool ili set."+ " Greska",pr);
	  }
	}
	

	@Override
	public void visit(StatementSinglePrint2 pr)
	{
		 Struct type=pr.getExpr().struct;
		  if(!type.equals(Tab.intType) && !type.equals(Tab.charType) && !type.equals(Compiler.boolType)  && !type.equals(Compiler.setType) )
		  {
			  report_error("Semanticka greska - argument print naredbe mora tipa int, char, bool ili set."+ " Greska",pr);
		  }
	
	}
		
	
	
	@Override
	public void visit(StatementSingleIfElse ss)
	{
		
	}
	
	@Override  
	public void visit(ActPar ap)
	{
		actParamsTypes.add(ap.getExpr().struct);
	}
	
	
	@Override
	public void visit(CondFact1 cf)
	{
		if(!cf.getExpr().struct.equals(Compiler.boolType)) {
				report_error("Semanticka greska - logicki operand mora biti tipa bool!"+ " Greska",cf);
	         	cf.struct=Tab.noType;
		}
		else {
			cf.struct=Compiler.boolType;
		}
	}

	@Override
	public void visit(CondFact2 cf)
	{
		Struct left=cf.getExpr().struct;
		Struct right=cf.getExpr1().struct;
		if(!left.equals(right)) {
			report_error("Semanticka greska - operandi RELOP operatora moraju biti istog tipa."+ " Greska",cf);
			cf.struct=Tab.noType;
		}
		else {
			if(left.isRefType() || right.isRefType())
			{
				if(cf.getRelop() instanceof RelopEqual || cf.getRelop() instanceof RelopNoEqual)
				{
					cf.struct=Compiler.boolType;
				}
				else {
					report_error("Semanticka greska - za poredjenje referenci se mogu koristiti samo != i == ."+ " Greska",cf);
					cf.struct=Tab.noType;
				}
			}
			else {
				cf.struct=Compiler.boolType;
			}
			
		}
		
	}
	
	
	@Override
	public void visit(CondFactListTerminate ct)
	{
		ct.struct=ct.getCondFact().struct;
	}
	
	
	@Override
	public void visit(CondFactListExpand ct)
	{
		Struct left=ct.getCondFactList().struct;
		Struct right=ct.getCondFact().struct;
		if(!left.equals(Compiler.boolType) || !right.equals(Compiler.boolType))
		{
			report_error("Semanitcka greska - oba operanda AND operatora moraju biti tipa bool."+ " Greska",ct);
		    ct.struct=Tab.noType;
		}
		else {
			ct.struct=Compiler.boolType;
		}
	}
	
	@Override
	public void visit(CondTerm ct)
	{
		ct.struct=ct.getCondFactList().struct;
	}
	
	
	
	@Override
	public void visit(CondTermListTerminate ct)
	{
		ct.struct=ct.getCondTerm().struct;
	}
	@Override
	public void visit(CondTermListExpand ct)
	{
		Struct left=ct.getCondTerm().struct;
		Struct right=ct.getCondTermList().struct;
		
		if(!left.equals(Compiler.boolType) || !right.equals(Compiler.boolType))
		{
			report_error("Semanitcka greska - oba operanda OR operatora moraju biti tipa bool."+ " Greska",ct);
		    ct.struct=Tab.noType;
		}
		else {
			ct.struct=Compiler.boolType;
		}
	}
	
	@Override
	public void visit(Condition c)
	{
		c.struct=c.getCondTermList().struct;
		if(!c.struct.equals(Compiler.boolType))
		{
			report_error("Semanticka greska - uslov mora biti tipa bool."+ " Greska",c);
		}
	}
	
	
}
	

