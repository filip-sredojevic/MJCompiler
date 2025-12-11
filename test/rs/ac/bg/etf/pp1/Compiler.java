package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;

import rs.etf.pp1.mj.runtime.*;

public class Compiler {

	public static Struct boolType;
	
	public static Struct setType;
	
	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void main(String[] args) throws Exception {
		
		Logger log = Logger.getLogger(Compiler.class);
		
		Reader br = null;
		try {
			File sourceCode = new File("test/program.mj"); //Otvara se fajl
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode)); //Pravi se bufer koji ce sluziti za citanje B po B
			Yylex lexer = new Yylex(br); //Pravi se objekat leksera
			
			MJParser p = new MJParser(lexer); //Pravi se objekat parsera, kroz konstruktor se prosledjuje objekat leksera
	        Symbol s = p.parse();  //formiranje AST, s.value sadrzi adresu korena stabla
	        
	        Program prog = (Program)(s.value); //Formira se pokazivac prog na koren stabla 
	        
			// ispis AST
			log.info(prog.toString(""));
			log.info("=====================================================================");

			   
			/*Inicijalizacija tabele simbola*/
			//nalazimo se u universe opsegu
			 Tab.init();
			 
			 
			 //Dodavanje tipa bool u Tabelu simbola
			 boolType = new Struct(Struct.Bool);
			 Obj bo = Tab.insert(Obj.Type, "bool", boolType);
			 bo.setAdr(-1);bo.setLevel(-1);
			 
			 //Dodavanje tipa set u Tabelu simbola
			// setType=new Struct(Struct.Enum, Tab.intType);
			 
			 setType=new Struct(Struct.Array, Tab.intType);
			 
			 Obj so=Tab.insert(Obj.Type, "set", setType);
			 so.setAdr(-1); so.setLevel(-1);
			 
			 
			 //Dodavanje nepostojecih predefinisanih imena
			 Obj temp1, temp2;
			 
			 Tab.currentScope.addToLocals(temp1=new Obj(Obj.Meth, "add", Tab.noType, 0, 2));
			{
				    
					Tab.openScope();
					Tab.currentScope.addToLocals(temp2=new Obj(Obj.Var, "a", setType, 0, 1));
					temp2.setFpPos(1);
					Tab.currentScope.addToLocals(temp2=new Obj(Obj.Var, "b", Tab.intType, 0, 1));
					temp2.setFpPos(1);
					Tab.currentScope.addToLocals(temp2=new Obj(Obj.Var, "c", Tab.intType, 0, 1));
					temp2.setFpPos(0);
					temp1.setLocals(Tab.currentScope.getLocals());
					Tab.closeScope();
			}
			Tab.currentScope.addToLocals(temp1 = new Obj(Obj.Meth, "addAll", Tab.noType, 0, 2));
				{
					Tab.openScope();
					Tab.currentScope.addToLocals(temp2=new Obj(Obj.Var, "a", setType, 0, 1));
					temp2.setFpPos(1);
					Tab.currentScope.addToLocals(temp2 =new Obj(Obj.Var, "b", setType, 0, 1));
					temp2.setFpPos(1);
					Tab.currentScope.addToLocals(temp2 =new Obj(Obj.Var, "c", Tab.intType, 0, 1));
					temp2.setFpPos(0);
					Tab.currentScope.addToLocals(temp2 =new Obj(Obj.Var, "d", Tab.intType, 0, 1));
					temp2.setFpPos(0);
					temp1.setLocals(Tab.currentScope.getLocals());
					Tab.closeScope();
				}
				
				
				Tab.currentScope.addToLocals(temp1 = new Obj(Obj.Meth, "prt", Tab.noType, 0, 0));
				{
					Tab.openScope();
					Tab.currentScope.addToLocals(temp2=new Obj(Obj.Var, "a", Tab.intType, 0, 1));
					temp2.setFpPos(1);
					Tab.currentScope.addToLocals(temp2=new Obj(Obj.Var, "b", Tab.intType, 0, 1));
					temp2.setFpPos(0);
					temp1.setLocals(Tab.currentScope.getLocals());
					Tab.closeScope();
				}
				
				
				Tab.currentScope.addToLocals(temp1 = new Obj(Obj.Meth, "uni", Tab.noType, 0, 0));
				{
					Tab.openScope();
					Tab.currentScope.addToLocals(temp2=new Obj(Obj.Var, "d", Tab.intType, 0, 1));
					temp2.setFpPos(1);
					Tab.currentScope.addToLocals(temp2=new Obj(Obj.Var, "s1", Tab.intType, 0, 1));
					temp2.setFpPos(1);
					Tab.currentScope.addToLocals(temp2=new Obj(Obj.Var, "s2", Tab.intType, 0, 1));
					temp2.setFpPos(1);
					Tab.currentScope.addToLocals(temp2=new Obj(Obj.Var, "i1", Tab.intType, 0, 1));
					temp2.setFpPos(0);
					Tab.currentScope.addToLocals(temp2=new Obj(Obj.Var, "i2", Tab.intType, 0, 1));
					temp2.setFpPos(0);
					Tab.currentScope.addToLocals(temp2=new Obj(Obj.Var, "i3", Tab.intType, 0, 1));
					temp2.setFpPos(0);
					temp1.setLocals(Tab.currentScope.getLocals());
					Tab.closeScope();
				}
				Tab.currentScope.addToLocals(temp1 = new Obj(Obj.Meth, "mapi", Tab.noType, 0, 0));
				{
					Tab.openScope();
					Tab.currentScope.addToLocals(temp2=new Obj(Obj.Var, "a", Tab.intType, 0, 1));
					temp2.setFpPos(1);
					Tab.currentScope.addToLocals(temp2=new Obj(Obj.Var, "b", Tab.intType, 0, 1));
					temp2.setFpPos(1);
					Tab.currentScope.addToLocals(temp2=new Obj(Obj.Var, "sum", Tab.intType, 0, 1));
					temp2.setFpPos(0);
					Tab.currentScope.addToLocals(temp2=new Obj(Obj.Var, "i", Tab.intType, 0, 1));
					temp2.setFpPos(0);
					temp1.setLocals(Tab.currentScope.getLocals());
					Tab.closeScope();
				}
				
				
				
				
			 
			 /*Semanticka analiza */
			 
			SemanticAnalyzer sa=new SemanticAnalyzer();	
			prog.traverseBottomUp(sa);
				
				
			 /*Ispis tabele simbola */
			 log.info("=====================================================================");
			 Tab.dump();
			 
			 
			if(!p.errorDetected && sa.passed()){
				
				//Ispravan program, sve spremno za generisanje koda
				File objFile = new File("test/program.obj"); //Fajl u koji ce biti upisan nas bytecode, tj. sadrzaj Code Memory
				if(objFile.exists()) objFile.delete();
				
				CodeGenerator cg = new CodeGenerator();
				prog.traverseBottomUp(cg);
				Code.dataSize = sa.nVars;
				Code.mainPc = cg.getmainPc();
				Code.write(new FileOutputStream(objFile));
				
				log.info("Generisanje uspesno zavrseno!");
			}else{
				log.error("Parsiranje NIJE uspesno zavrseno!");
			}
			
		} 
		finally {
			if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
		}

	}
	
	
}
