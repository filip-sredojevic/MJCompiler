package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;
	
	public int getmainPc()
	{
		return this.mainPc;
	}
	
	private int loopStart;  private int loopStartPrt;
	private int jumpToEnd1;
	private int jumpToEnd2;
	private int jumpToEnd3;
	private int jumpToEnd4;
	
	//addALL
	private int loopStartAddAll1;
	private int loopStartAddAll2;
	private int jump1;
	
	int fix1; int fix2; int fix3; int fix4;
	
	int fixPrt;
	 
	int unifix1;
	int unil1;

	int minifix;
	int minil1;
	
     int miniCodePc;
     int miniCodeFix;
	
	private void initializePredeclaredMethods() {
		//************************************mini*********************************
		 Obj mapiMethod = Tab.find("mapi");
	     mapiMethod.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(2);
		Code.put(4);
		
		//Izvrsilo se
		//Code.loadConst(19);
        //Code.put(Code.exit);
        //Code.put(Code.return_);
		
		//Stavljanje arr[i] na ExprStack
		minil1=Code.pc;
		Code.put(Code.load);
		Code.put(1);
		Code.put(Code.load);
		Code.put(3);
		Code.put(Code.aload); //ES: arr[i]
		
		
		
		//Poziv funkcije za argument arr[i]
		//int s=funcAdr-Code.pc; //Sacuvamo Code.pc i adresu prepravke
		miniCodePc=Code.pc;
		Code.put(Code.call);
		Code.put2(0);
		miniCodeFix=Code.pc-2;
		
		
		//Pozvana metoda sigurno ima return naredbu, rezultat izvrsavanja ostavlja na ExprStack
	    //Rezultat funkcije dodajemo u promenljivu sum
		Code.put(Code.load);
	    Code.put(2);
	    Code.put(Code.add);
	    Code.put(Code.store);
	    Code.put(2);
	    
	    //Inkrementiranje brojacai
	    Code.put(Code.load);
	    Code.put(3);
	    Code.loadConst(1);
	    Code.put(Code.add);
	    Code.put(Code.store);
	    Code.put(3);
	    
	    //Provera da li smo stigli do kraja niza arr
	    Code.put(Code.load);
	    Code.put(1);
	    Code.put(Code.arraylength);
	    Code.put(Code.load);
	    Code.put(3);
	    
	    Code.putFalseJump(Code.ne, 0);
	    minifix=Code.pc-2;
	    Code.putJump(minil1);
	    
	    Code.fixup(minifix);
	    
	    //Dohvatanje promenljive sum
	    Code.put(Code.load);
	    Code.put(2);
	    
	    Code.put(Code.exit);
	    Code.put(Code.return_);
		
		//************************************union*******************************
		 Obj uniMethod = Tab.find("uni");
	     uniMethod.setAdr(Code.pc);
	     Code.put(Code.enter);
	     Code.put(3);
	     Code.put(6);
		
		 unil1=Code.pc;
	     //Elemente iz src1 samo prepisemo u dst
	     Code.put(Code.load);
	     Code.put(0);//ES: adr(dst)
	     Code.put(Code.load);
	     Code.put(3); //ES adr(dst) i1
	     Code.put(Code.load);
	     Code.put(1);//ES: adr(dst) adr(src1) 
	     Code.put(Code.load);
	     Code.put(4);
	     Code.put(Code.aload); //ES adr(dst) i1 src1[i2]
	     Code.put(Code.astore);
	     //Cim upisemo nesto u dst moramo inkrementirait i1
	     Code.put(Code.load);
	     Code.put(3);
	     Code.loadConst(1);
	     Code.put(Code.add);
	     Code.put(Code.store);
	     Code.put(3);
	     //Inkrementiramo i2
	     Code.put(Code.load);
	     Code.put(4);
	     Code.loadConst(1);
	     Code.put(Code.add);
	     Code.put(Code.store);
	     Code.put(4);
	     //Da li smo ispucali sve elemente niza s1
	     Code.put(Code.load);
	     Code.put(1);
	     Code.put(Code.arraylength);
	     Code.put(Code.load);
	     Code.put(4);
	     
	     Code.putFalseJump(Code.ne, 0);
	     unifix1=Code.pc-2;
	     Code.putJump(unil1);
	//IZNAD OK     
	//************************************************************************************************************************************     
	     Code.fixup(unifix1);
	     
	     //src2->src1
	     Code.put(Code.load);
	     Code.put(2);
	     Code.put(Code.store);
	     Code.put(1);
	     //0 -> src2
	     Code.loadConst(0);
	     Code.put(Code.store);
	     Code.put(2);
	     //0 ->i1 
	     Code.loadConst(0);
	     Code.put(Code.store);
	     Code.put(3);
	     
	     
	   //******************************Umetnut kod***********************************
	     loopStartAddAll1=Code.pc;
	        Code.loadConst(0);
	        Code.put(Code.store); //radimo clear brojaca c koji ide po setu, promenljiva c (3. po redu)
	        Code.put(2);
	        
	        loopStartAddAll2=Code.pc;
	        Code.put(Code.load); //stavljanje adrese set-a na ExprStack #1
	        Code.put(0); //stavljanje adrese set-a na ExprStack         #1
	        Code.put(Code.load); //stavljanje lokalne promenljive c na stek (index tekuceg elementa SETA) #1
	        Code.put(2); //stavljanje lokalne promenljive c na stek (index tekuceg elementa SETA) #1
	        Code.put(Code.aload); //Stavljanje vrednosti set[c] na ExprStack
	        //ES: set[c]
	        
	        Code.put(Code.load); //stavljanje adrese niza  na ExprStack #1
	        Code.put(1); //stavljanje adrese niza na ExprStack         #1
	        Code.put(Code.load); //stavljanje lokalne promenljive d na stek (index tekuceg elementa niza) #1
	        Code.put(3); //stavljanje lokalne promenljive d na stek (index tekuceg elementa Niza) #1
	        Code.put(Code.aload); //Stavljanje vrednosti arr[d] na ExprStack
	        //ES: set[c], arr[d]
	        
	        Code.putFalseJump(Code.ne, 0);
	        fix1=Code.pc-2; //arr[d] vec posotji u set-u, treba preci obradi sledeceg elementa niza arr[d+1] 
	       //set[c]!=arr[d];
	        //Ispitujemo da li je slucajno element skupa slobodan
	        //ES: 
	        
	       
	        Code.put(Code.load); //stavljanje adrese set-a na ExprStack #1
	        Code.put(0); //stavljanje adrese set-a na ExprStack         #1
	        Code.put(Code.load); //stavljanje lokalne promenljive c na stek (index tekuceg elementa) #1
	        Code.put(2); //stavljanje lokalne promenljive c na stek (index tekuceg elementa) #1
	        Code.put(Code.aload); //Stavljanje vrednosti set[c] na ExprStack
	        
	        //ES: set[c]
	        
	        Code.loadConst(0); //Proveravamo da li element set[a] nije slucajno 0, ako je 0 onda  mozeom da ubacimo arr[d] na set[c]
	        
	        Code.putFalseJump(Code.ne, fix2);
	        fix2=Code.pc-2;//fix2 treba da skoci na mesto gde ubacuje arr[d] na mesto set[c]
	        //set[c]!=arr[d] && set[c]!=0 Izvrsi poredjenje sa sledecim elementom u set-u
	        
	        //c=c+1
	        Code.put(Code.load); 
	        Code.put(2);
	        Code.loadConst(1);
	        Code.put(Code.add);
	        Code.put(Code.store); 
	        Code.put(2);
	        
	        //Proveri da li si stigao do kraja seta
	        Code.put(Code.load);
	        Code.put(0); 
	        Code.put(Code.arraylength);
	        Code.put(Code.load);
	        Code.put(2);
	        //ES: n
	        
	        Code.putFalseJump(Code.ne, 0);//skace se ukoliko je c==n
	        fix3=Code.pc-2;//fix3 treba da skoci na obradu sledeceg elementa niza arr[d+1]
	        
	        //set[c]!=arr[d] && set[c]!=0 88 c<n => c je vec inkrementiran, idemo poredjenje set[c+1] sa arr[d]
	        Code.putJump(loopStartAddAll2); //Idemo proveravamo sa sledecim elementom u setu
	        
	        //Element set[c] = 0 pa mozemo da ubacimo arr[d]
	        
	        //**********************************************************
	        Code.fixup(fix2);
	        
	        Code.put(Code.load);
	        Code.put(0);
	        Code.put(Code.load);
	        Code.put(2);
	        Code.put(Code.load);
	        Code.put(1);
	        Code.put(Code.load);
	        Code.put(3);
	        Code.put(Code.aload);
	        //Es: adr(set), c, arr[d]
	        Code.put(Code.astore);//arr[d] -> set[c]
	        
	        //*************************************************************
	        Code.fixup(fix1);
	        Code.fixup(fix3);
	        
	        
	        //d=d+1
	        Code.put(Code.load);
	        Code.put(3);
	        Code.loadConst(1);
	        Code.put(Code.add);
	        Code.put(Code.store);
	        Code.put(3);
	        
	        
	        
	        //Proveravamo da li smo obradili sve elemente niza arr
	        Code.put(Code.load);
	        Code.put(1);
	        Code.put(Code.arraylength);
	        Code.put(Code.load);
	        Code.put(3);
	        
	        Code.putFalseJump(Code.ne, 0);
	        fix4=Code.pc-2;
	        //Nismo dosli do kraja niza arr, d je inkrementirano  => idemo na sledeci element
	        Code.putJump(loopStartAddAll1);
	        
	        //***********************************************************************************
	        Code.fixup(fix4);
	        
	        
	        Code.put(Code.exit);
	        Code.put(Code.return_);
	     
	     
	   //******************************Umetnut kod***********************************
	     
		//***************ord i chr*****************88
       
        Obj ordMethod = Tab.find("ord");
        Obj chrMethod = Tab.find("chr");
        ordMethod.setAdr(Code.pc);
        chrMethod.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(1);
        Code.put(1); 
        Code.put(Code.load_n);
        Code.put(Code.exit);
        Code.put(Code.return_);
        
        //*****************addALL*****************************8
        Obj addMethodAll=Tab.find("addAll");
        addMethodAll.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(2);
        Code.put(4); //2 parametra i 1 lokalna promenljiva
        //Prazan ExprStack
     
        loopStartAddAll1=Code.pc;
        Code.loadConst(0);
        Code.put(Code.store); //radimo clear brojaca c koji ide po setu, promenljiva c (3. po redu)
        Code.put(2);
        
        loopStartAddAll2=Code.pc;
        Code.put(Code.load); //stavljanje adrese set-a na ExprStack #1
        Code.put(0); //stavljanje adrese set-a na ExprStack         #1
        Code.put(Code.load); //stavljanje lokalne promenljive c na stek (index tekuceg elementa SETA) #1
        Code.put(2); //stavljanje lokalne promenljive c na stek (index tekuceg elementa SETA) #1
        Code.put(Code.aload); //Stavljanje vrednosti set[c] na ExprStack
        //ES: set[c]
        
        Code.put(Code.load); //stavljanje adrese niza  na ExprStack #1
        Code.put(1); //stavljanje adrese niza na ExprStack         #1
        Code.put(Code.load); //stavljanje lokalne promenljive d na stek (index tekuceg elementa niza) #1
        Code.put(3); //stavljanje lokalne promenljive d na stek (index tekuceg elementa Niza) #1
        Code.put(Code.aload); //Stavljanje vrednosti arr[d] na ExprStack
        //ES: set[c], arr[d]
        
        Code.putFalseJump(Code.ne, 0);
        fix1=Code.pc-2; //arr[d] vec posotji u set-u, treba preci obradi sledeceg elementa niza arr[d+1] 
       //set[c]!=arr[d];
        //Ispitujemo da li je slucajno element skupa slobodan
        //ES: 
        
       
        Code.put(Code.load); //stavljanje adrese set-a na ExprStack #1
        Code.put(0); //stavljanje adrese set-a na ExprStack         #1
        Code.put(Code.load); //stavljanje lokalne promenljive c na stek (index tekuceg elementa) #1
        Code.put(2); //stavljanje lokalne promenljive c na stek (index tekuceg elementa) #1
        Code.put(Code.aload); //Stavljanje vrednosti set[c] na ExprStack
        
        //ES: set[c]
        
        Code.loadConst(0); //Proveravamo da li element set[a] nije slucajno 0, ako je 0 onda  mozeom da ubacimo arr[d] na set[c]
        
        Code.putFalseJump(Code.ne, fix2);
        fix2=Code.pc-2;//fix2 treba da skoci na mesto gde ubacuje arr[d] na mesto set[c]
        //set[c]!=arr[d] && set[c]!=0 Izvrsi poredjenje sa sledecim elementom u set-u
        
        //c=c+1
        Code.put(Code.load); 
        Code.put(2);
        Code.loadConst(1);
        Code.put(Code.add);
        Code.put(Code.store); 
        Code.put(2);
        
        //Proveri da li si stigao do kraja seta
        Code.put(Code.load);
        Code.put(0); 
        Code.put(Code.arraylength);
        Code.put(Code.load);
        Code.put(2);
        //ES: n
        
        Code.putFalseJump(Code.ne, 0);//skace se ukoliko je c==n
        fix3=Code.pc-2;//fix3 treba da skoci na obradu sledeceg elementa niza arr[d+1]
        
        //set[c]!=arr[d] && set[c]!=0 88 c<n => c je vec inkrementiran, idemo poredjenje set[c+1] sa arr[d]
        Code.putJump(loopStartAddAll2); //Idemo proveravamo sa sledecim elementom u setu
        
        //Element set[c] = 0 pa mozemo da ubacimo arr[d]
        
        //**********************************************************
        Code.fixup(fix2);
        
        Code.put(Code.load);
        Code.put(0);
        Code.put(Code.load);
        Code.put(2);
        Code.put(Code.load);
        Code.put(1);
        Code.put(Code.load);
        Code.put(3);
        Code.put(Code.aload);
        //Es: adr(set), c, arr[d]
        Code.put(Code.astore);//arr[d] -> set[c]
        
        //*************************************************************
        Code.fixup(fix1);
        Code.fixup(fix3);
        
        
        //d=d+1
        Code.put(Code.load);
        Code.put(3);
        Code.loadConst(1);
        Code.put(Code.add);
        Code.put(Code.store);
        Code.put(3);
        
        
        
        //Proveravamo da li smo obradili sve elemente niza arr
        Code.put(Code.load);
        Code.put(1);
        Code.put(Code.arraylength);
        Code.put(Code.load);
        Code.put(3);
        
        Code.putFalseJump(Code.ne, 0);
        fix4=Code.pc-2;
        //Nismo dosli do kraja niza arr, d je inkrementirano  => idemo na sledeci element
        Code.putJump(loopStartAddAll1);
        
        //***********************************************************************************
        Code.fixup(fix4);
        
        
        Code.put(Code.exit);
        Code.put(Code.return_);
        
        
     
        //***************************prt()*********************************
        Obj prtMethod=Tab.find("prt");
        prtMethod.setAdr(Code.pc);
        Code.put(Code.enter); //Adresu sa vrha steka ce ubaciti u promenljivu a
        Code.put(1);
        Code.put(2); 
        
        loopStartPrt=Code.pc;
        Code.put(Code.load); //Ucitavanje adrese skuoa
        Code.put(0);
        
        Code.put(Code.load); //Ucitavanje indeksa tekuceg elementa (b) skupa
        Code.put(1); //??????
        
        Code.put(Code.aload); //Ucitavnje elementa set[b] na stek
        
        Code.put(Code.dup);
        
        //Novo - Provera da li je element set[b]=0, ako je 0 prekini ispisivanje
        Code.loadConst(0);
        Code.putFalseJump(Code.ne, 0);
        fixPrt=Code.pc-2;
        
        
        Code.loadConst(0);
        Code.put(Code.print); //Ispis set[b] 
        
        Code.loadConst(32); //Ispis blanko znaka
        Code.loadConst(0); //Ispis blanko znaka
        Code.put(Code.bprint); //Ispis blanko znaka
        
        
        Code.put(Code.load); //Ucitavanje indeksa tekuceg elementa (b) skupa
        Code.put(1);
        Code.loadConst(1);
        
        Code.put(Code.add);
        Code.put(Code.store);
        Code.put(1);
        
        Code.put(Code.load);
        Code.put(0);
        Code.put(Code.arraylength);
        Code.put(Code.load); //Ucitavanje indeksa tekuceg elementa (b) skupa
        Code.put(1);//???? Izvrsena promena
        
        Code.putFalseJump(Code.ne, 0);
        jumpToEnd4=Code.pc-2;
        Code.putJump(loopStartPrt);
        
        Code.fixup(jumpToEnd4);
        
       
        Code.put(Code.exit);
        Code.put(Code.return_);
        
        //Novo
        Code.fixup(fixPrt);
        Code.put(Code.pop);
        Code.put(Code.exit);
        Code.put(Code.return_);
        
        //*************************add()**********************************
        Obj addMethod=Tab.find("add");
        addMethod.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(2);
        Code.put(3); //2 parametra i 1 lokalna promenljiva
        //Prazan ExprStack
     
        loopStart=Code.pc;
        
        Code.put(Code.load); //stavljanje adrese set-a na ExprStack #1
        Code.put(0); //stavljanje adrese set-a na ExprStack         #1
        Code.put(Code.load); //stavljanje lokalne promenljive c na stek (index tekuceg elementa) #1
        Code.put(2); //stavljanje lokalne promenljive c na stek (index tekuceg elementa) #1
        Code.put(Code.aload); //Stavljanje vrednosti set[c] na ExprStack
        
        Code.put(Code.load); //stavljanje Exor na stek  #1
        Code.put(1); //stavljanje Expr na stek   #1
        
        Code.putFalseJump(Code.ne, 0);
        jumpToEnd1=Code.pc-2; //ELEMENT VEC POSTOJI U SKUPU, ZAVRSI FUNKCIJU
        
        Code.put(Code.load); //stavljanje adrese set-a na ExprStack #1
        Code.put(0); //stavljanje adrese set-a na ExprStack         #1
        Code.put(Code.load); //stavljanje lokalne promenljive c na stek (index tekuceg elementa) #1
        Code.put(2); //stavljanje lokalne promenljive c na stek (index tekuceg elementa) #1
        Code.put(Code.aload); //Stavljanje vrednosti set[a] na ExprStack
        
        Code.loadConst(0); //Proveravamo da li element set[a] nije slucajno 0, ako je 0 onda
        
        Code.putFalseJump(Code.ne, 0);
        jumpToEnd2=Code.pc-2; //Ubaci element na poziciju a i zavrsi funkciju
        
        Code.put(Code.load); //stavljanje lokalne promenljive c na stek (index tekuceg elementa) #1
        Code.put(2); //stavljanje lokalne promenljive c na stek (index tekuceg elementa) #1
        
        Code.loadConst(1); //Inkrementiranje lokalne promenljive c
        Code.put(Code.add); //Inkrementrianje lokalne promenljive c
        
        Code.put(Code.store);
        Code.put(2);
        
        Code.put(Code.load); //stavljanje lokalne promenljive c na stek (index tekuceg elementa), da bismo uporedili sa duzinom steka
        Code.put(2); //stavljanje lokalne promenljive c na stek (index tekuceg elementa), da bismo uporedili sa duzinom steka
        
        Code.put(Code.load); //stavljanje adrese set-a na ExprStack #1
        Code.put(0); //stavljanje adrese set-a na ExprStack      
        
        Code.put(Code.arraylength); //Dohvatanje duzine niza
        
        Code.putFalseJump(Code.ne, 0); //Provera da li smo dosli do kraja set-a
        jumpToEnd3=Code.pc-2;
        
        Code.putJump(loopStart);
        
        //Ubacivanje elemnta na poziciju a
        Code.fixup(jumpToEnd2);
        
        Code.put(Code.load); //stavljanje adrese set-a na ExprStack #1
        Code.put(0); //stavljanje adrese set-a na ExprStack         #1
        Code.put(Code.load); //stavljanje lokalne promenljive c na stek (index tekuceg elementa) #1
        Code.put(2); //stavljanje lokalne promenljive c na stek (index tekuceg elementa) #1
        Code.put(Code.load); //stavljanje adrese set-a na ExprStack #1
        Code.put(1); //stavljanje adrese set-a na ExprStack   
        Code.put(Code.astore);
        
        Code.fixup(jumpToEnd1);
        Code.fixup(jumpToEnd3);
        Code.put(Code.exit);
        Code.put(Code.return_);
        
       // ***********************************************
 
    }
	
	CodeGenerator() {
		this.initializePredeclaredMethods();
	}
	
	@Override 
	public void visit(MethodRetTypeNameNoVoid m)
	{
		
		
		m.obj.setAdr(Code.pc);
		if(m.getI2().equals("main"))this.mainPc=Code.pc;
		
		Code.put(Code.enter); //Operacioni kod je duzine 1 B
		Obj methodObj=m.obj;
		Code.put(methodObj.getLevel()); // broj formalnih parametara
		Code.put(methodObj.getLocalSymbols().size()); //broj formalnih parametara i lokalnih promenljivih
		
		
	}
	
	
	@Override 
	public void visit(MethodRetTypeNameVoid m)
	{
		m.obj.setAdr(Code.pc);
		if(m.getI1().equals("main"))this.mainPc=Code.pc;
		
		
		Code.put(Code.enter); //Operacioni kod je duzine 1 B
		Obj methodObj=m.obj;
		Code.put(methodObj.getLevel());
		Code.put(methodObj.getLocalSymbols().size());
		
	}
	
	@Override
	public void visit(MethodDecl m)
	{
		Code.put(Code.exit); //nema operande
		Code.put(Code.return_); //nema operande
	}
	
	@Override
	public void visit(StatementSinglePrint1 ss)
	{
		
		if(ss.getExpr().struct.equals(Compiler.setType))
		{
			
			Obj obj=Tab.find("prt");
			int s=obj.getAdr()-Code.pc;
			Code.put(Code.call);
			Code.put2(s);
		}
		else if(ss.getExpr().struct.equals(Tab.charType))
		{
			Code.loadConst(0);
			Code.put(Code.bprint);
		}
		else {
			Code.loadConst(0);
			Code.put(Code.print);
		}
	    //Metoda Code.LoadConst sluzi za smestanje konstanti na ExprStack
	   
	   //Obavezno prvo Code.put pa tek onda Code.LoadConst
		
	}
	@Override
	public void visit(StatementSinglePrint2 ss)
	{
		Code.loadConst(ss.getN2());
		if(ss.getExpr().struct.equals(Tab.charType))
		{
			Code.put(Code.bprint);
		}
		else {
			Code.put(Code.print);
		}
	   
	} 
	
	
	
	@Override
	public void visit(TermListExpand te)
	{
		if(te.getAddop() instanceof AddopPlus) {
			Code.put(Code.add);
		}
		else {
		  Code.put(Code.sub);	
		}
	}
	
	@Override
	public void visit(FactorListExpand fe) {
		if(fe.getMulop() instanceof MulopMul)
		{
			Code.put(Code.mul);
		}
		else if(fe.getMulop() instanceof MulopDiv)
		{
			Code.put(Code.div);
		}
		else {
			Code.put(Code.rem);
		}
	}
	
	
	//PUSH FAKTORA NA ExprStack
	@Override
	public void visit(FactorValueNumber  fn)
	{
		Code.loadConst(fn.getN1());
	}
	@Override
	public void visit(FactorValueCharacter fc)
	{
		Code.loadConst(fc.getC1());
	}
	
	@Override
	public void visit(FactorValueBool fb)
	{
		Code.loadConst(fb.getB1());
	}
	
	@Override
	public void visit(FactorValueVar fv)
	{
		Code.load(fv.getDesignator().obj);//Stavljanje svih glob promen, konstanti, lokalnih promenljivih, form parametara, nizove
		//LOAD je napredna funkcija za smestanje sa neke memorije na ExprStack
	}
	
	
	
	@Override
	public void visit(DesignatorArrayName da)
	{
		Code.load(da.obj);
		//Adresa niza ce nam trebati na ExprSteku bilo da je Designator sa leve strane = ili sa desne strane =
	}
	
	@Override
	public void visit(DesignatorStatementAssign ds)
	{
		Code.store(ds.getDesignator().obj);
	}
	@Override
	public void visit(Factor1 f)
	{
		Code.put(Code.neg);
	}
	
	@Override
	public void visit(FactorValueArray fv)
	{
		Code.put(Code.newarray);
		
		
		if(fv.getType().struct.equals(Tab.charType))
		{
			Code.put(0);
		}
		else {
			Code.put(1);
		}
	}
	
	@Override
	public void visit(DesignatorStatementInc ds)
	{
	
		if(ds.getDesignator().obj.getKind()==Obj.Elem)
		{
			Code.put(Code.dup2);
		}
		
		Code.load(ds.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(ds.getDesignator().obj);
		
		
	}
	
	@Override
	public void visit(DesignatorStatementDec dd)
	{
		if(dd.getDesignator().obj.getKind()==Obj.Elem)
		{
			Code.put(Code.dup2);
		}
		Code.load(dd.getDesignator().obj);
		Code.loadConst(-1);
		Code.put(Code.add);
		Code.store(dd.getDesignator().obj);
	}
	
	@Override
	public void visit(StatementSingleReturnNoArg ss)
	{
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	@Override
	public void visit(StatementSingleReturnArg ss)
	{
		// return expr; -> Izacicemo iz metode a expr ce ostati na ExprStack
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	
	
	@Override
	public void visit(StatementSingleRead sr)
	{
		if(sr.getDesignator().obj.getType().equals(Tab.charType))
		{
			Code.put(Code.bread);
		}
		else {Code.put(Code.read);}
		
		Code.store(sr.getDesignator().obj);
	}
	
	
	@Override
	public void visit(DesignatorStatementActPar ds)
	{
		Obj obj=ds.getDesignator().obj;
				
		int s=ds.getDesignator().obj.getAdr()-Code.pc;
		Code.put(Code.call);
		Code.put2(s);
		if(!ds.getDesignator().obj.getType().equals(Tab.noType))
		{
			Code.put(Code.pop);
		 }
		
	}
	
	@Override
	public void visit(FactorValueMethod fv)
	{
		
		int s=fv.getDesignator().obj.getAdr()-Code.pc;
		Code.put(Code.call);
		Code.put2(s);
			
		
	}
	
	private Stack<Integer> skipCondFact = new Stack<>(); //skup niti koje su izbacene i treba ih vratiti u sledece or
	private Stack<Integer> skipCondition =new Stack<>();
	private int offsetToElse;
	private int skipElse;
	
	
	private Stack<Integer> skipThanBranch=new Stack<>();
	private Stack<Integer> skipElseBranch=new Stack<>();
	
	
	
	private Stack<ArrayList<Integer>> skipBreak=new Stack<>();
	
	private Stack<ArrayList<Integer>> skipContinue=new Stack<>();
	
	private int returnRelopCode(Relop r)
	{
		if(r instanceof RelopEqual) return Code.eq;
		else if(r instanceof RelopNoEqual) return Code.ne;
		else if(r instanceof RelopGreater) return Code.gt;
		else if(r instanceof RelopGreaterEqual) return Code.ge;
		else if(r instanceof RelopLess) return Code.lt;
		else return Code.le;
	}
	
	@Override
	public void visit(CondFact1 cf)
	{
		 //Na steku se nalazi samo jedna stvar, pa mi moramo sami gurnuti jos jednu da bismo imali sa cime da poreidimo
		//UKOLIKO je expr 1 znaci da je tacan, ne treba da se skace iz OR
		//UkoLIKO JE EXPR 0 ZNACI DA JE NETACAN, treba da iskocimo iz tekuceg OR
		Code.loadConst(0);
	    Code.putFalseJump(Code.ne, 0); //Ne znamo gde skacemo jer u ovom trenutku ne znamo gde pocinje sledece OR
	    skipCondFact.push(Code.pc-2);
	    //Ovo izvrsavamo ako nismo skocili, tj. ako smo ostali unutar istog OR
		
	}
	
	
	@Override
	public void visit(CondFact2 cf)
	{
		//Imamo dve vrednosti na steku, ne treba mi sami da vestacki dodajemo vrednost za poredjenej
		Code.putFalseJump(returnRelopCode(cf.getRelop()), 0);
		skipCondFact.push(Code.pc-2);
		
	}
	
	@Override
	public void visit(CondTerm ct)
	{
		//Zavrsava se jedan or ali pocinje i drugi
		//Izvrisice se 1 ili vise visita(CondFact)  (samim tim ce postojati nekoliko instanci koda napisanog u tim metodama) pre nego 
		//- sto se izvrsi kod u ovoj metodi
		//Tacnu nit moramo da bacimo jer ce se nakon koda napisanog u ovoj metodi ponovo generisati kod iz visita CondFact
		//jer nakon visit(CondTerm) po gramatici dolazi ponovo visit(CondFact)
		
		//Ako je nit stigla do linije ispod to znaci da nigde nije bacena, odnoso da je prosla sva and unutar jednog OR
		Code.putJump(0);//tacne bacamo na then
		skipCondition.push(Code.pc-2);
		
		//ovde vracamo netacne
		//Niti koje su izbacene iz jednog or 
		while(!skipCondFact.empty())Code.fixup(skipCondFact.pop());
		
	}

	@Override
	public void visit(Condition condition)//prvo izbacene sacuvati a onda tacne pustiti da nastave odavde da se izvrse
	{
		//Nit koja je dosla do ove linije znaci da nije prosla nijedan OR, takve niti bacamo na else granu
		Code.putJump(0); //Netacne bacamo na else
		offsetToElse = Code.pc-2;
		skipThanBranch.add(offsetToElse);
		
		//uzimamo tacne, bice samo jedna
		while(!skipCondition.empty())
			Code.fixup(skipCondition.pop());
		
		//tacne
		
	}
	
	@Override
	public void visit(ElseStatementEpsilon es)
	{
		//Nema else grane => tacne su same dosle ovde, netacne moramo rucno vratiti
	   Code.fixup(skipThanBranch.pop());	
	  
	  //tacne + netacne, skupili smo sve niti
	}
	
	@Override
	public void visit(ElseStatementExist es)
	{
			
	Code.fixup(skipElseBranch.pop());	
	  //netacne + tacne
	}
	@Override
	public void visit(Else e)
	{
		//tacne
		Code.putJump(0); //tacne bacamo na kraj Else
		skipElse=Code.pc-2;
		skipElseBranch.add(skipElse);
		Code.fixup(skipThanBranch.pop());
	}
	
	@Override
	public void visit(If i)
	{
		
		
	}
	
	private Stack<Integer> doAdr=new Stack<>();
	@Override
	public void visit(Do d)
	{
		
		doAdr.push(Code.pc);
		skipBreak.add(new ArrayList<Integer>());
		skipContinue.add(new ArrayList<>());;
	    
	}
	
	
	@Override
	public void visit(StatementSingleDoWHILE ss)
	{
		//fixup ne generise nikakav dodatni kod u Code memoriji, vec samo vrsi popravku na vec postojecem kodu
		
	    Code.putJump(doAdr.pop());
		Code.fixup(skipThanBranch.pop());
		
		List<Integer> off=skipBreak.pop();
		while(off.size()>0)Code.fixup(off.remove(0));
		
	}
	
	
	@Override
	public void visit(Break b)
	{
		Code.putJump(0);
		skipBreak.peek().add(Code.pc-2);
		
	}
	@Override
	public void visit(StatementSingleContinue s)
	{
		
		Code.putJump(0);
		this.skipContinue.peek().add(Code.pc-2);
	}
	@Override
	public void visit(While w)
	{
		
	   List<Integer> off=skipContinue.pop();
	   while(off.size()>0)Code.fixup(off.remove(0));
	}
	
	@Override
	public void visit(DesignatorStatementSetop ds)
	{
		Code.load(ds.getDesignator().obj);
		Code.load(ds.getDesignator1().obj);
		Code.load(ds.getDesignator2().obj);
		
		
		Obj obj=Tab.find("uni");
		int s=obj.getAdr()-Code.pc;
		Code.put(Code.call);
		Code.put2(s);
					
	}
	 int funcAdr; 
	@Override
	public void visit(ExprDesignator e)
	{
		funcAdr=e.getDesignator().obj.getAdr();
		int currPc=Code.pc;
	    
		Code.pc=funcAdr;
		Code.fixup(miniCodeFix); 
	    Code.pc=currPc;
		
	 
	    
		Code.loadConst(funcAdr);  //Na ExprStack ide adresa funkcije
		Code.load(e.getDesignatorArrayName().obj); //Na ExprStack ide adresa niza
		
				
		Obj obj=Tab.find("mapi");
		int s=obj.getAdr()-Code.pc;
		Code.put(Code.call);
		Code.put2(s);
		
	}	
		
	
		
       
	
	
}