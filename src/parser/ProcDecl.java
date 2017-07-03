package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class ProcDecl extends PascalDecl{
	//String name;
	ParamDeclList pdl = null;
	Block block;
	int labelnr;
	ProcDecl(String name ,int lnum) {
		super(name, lnum);
	}
	public String identify() {
		return "<ProcDecl> on line " + lineNum;
	}
	void prettyPrint() {
		Main.log.prettyPrint("procedure ");
		Main.log.prettyPrint(name + " ");
		if (pdl != null) {
			pdl.prettyPrint();
		}
		Main.log.prettyPrintLn(":");
		block.prettyPrint();
		Main.log.prettyPrint(";"); 
		Main.log.prettyPrintLn();
	}
	static ProcDecl parse(Scanner s) {
		enterParser("procdecl");
		
		ProcDecl pd = new ProcDecl(s.curToken.id,s.curLineNum());
		s.skip(procedureToken);
		pd.name = s.curToken.id;
		s.skip(nameToken);
		if(s.curToken.kind == leftParToken) {
			pd.pdl = ParamDeclList.parse(s);
		}
		s.skip(semicolonToken);
		pd.block = Block.parse(s);
		s.skip(semicolonToken);
		leaveParser("procDecl");
		return pd;
	}
	@Override
	void checkWhetherAssignable(PascalSyntax where) {
		where.error("You cannot assign to a procedure.");

		
	}
	@Override
	void checkWhetherFunction(PascalSyntax where) {
		where.error("A procedure isn't a function.");
		
	}
	@Override
	void checkWhetherProcedure(PascalSyntax where) {
		
	}
	@Override
	void checkWhetherValue(PascalSyntax where) {
		where.error("Procedures doesn't return values.");
		
	}
	
	void check(Block curScope,Library lib ){
		labelnr = lib.counter();
		curScope.addDecl(name,this);
		block.outerScope = curScope;
		block.level = curScope.level + 1;
		level = curScope.level + 1;
		if(pdl != null){
			pdl.check(block,lib);
			
		}
		block.check(curScope, lib);
	}
	
	public void genCode(CodeFile f){
	
		for(int i = 0; i < block.proc.size();i++){
			block.proc.get(i).genCode(f);
			
		}
		
		for(int i = 0; i < block.funk.size();i++){
			block.funk.get(i).genCode(f);
			
		}
			
		f.genInstr("proc$" + name + "_" + labelnr ,"","","");
		
	if(block.variables != null){
		f.genInstr("","enter" ,"$" + (32 + (block.variables.variables.size() * 4)) + ",$" + level, "");
	}
	else {
		f.genInstr("","enter" ,"$32,$" + level, "");
	}
		
		
		block.genCode(f);
		f.genInstr("", "leave", "", "");
		f.genInstr("", "ret", "", "");
	}
}
