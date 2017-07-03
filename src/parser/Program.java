package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class Program extends PascalSyntax {
	int labelnr;
	String name;
	Block body;
	Program(int lnum) {
		super(lnum);
	}
	public String identify() {
		return "<Program> on line " + lineNum;
	}
	public void prettyPrint() {
		Main.log.prettyPrint("program ");
		Main.log.prettyPrint(name);
		Main.log.prettyPrintLn(";");
		body.prettyPrint();
		Main.log.prettyPrint(".");
	}
	public static Program parse(Scanner s) {
		enterParser("program");
		
		
		Program p = new Program(s.curLineNum());
		s.skip(programToken);
		
		p.name = s.curToken.id;
		s.skip(nameToken);
		
		s.skip(semicolonToken);
		
		p.body = Block.parse(s);
		s.skip(dotToken);
		
		leaveParser("program");
		return p;
	}
	public void check(Block curScope, Library lib) {
		labelnr = lib.counter();
		body.check(curScope,lib);
		
		
	}
	
	public void genCode(CodeFile f) {	
		
		
			f.genInstr("", ".globl", "main", "");
			f.genInstr("main","","","");
			f.genInstr("", "call" ,"prog$" + name + "_" + labelnr,"");
			f.genInstr("", "movl","$0,%eax","");
			f.genInstr("", "ret","","");
			for(int i = 0; i < body.proc.size();i++){
				body.proc.get(i).genCode(f);
				
			}
			
			for(int i = 0; i < body.funk.size();i++){
				body.funk.get(i).genCode(f);
				
			}
			
			
			f.genInstr("prog$" + name + "_" + labelnr, "", "", "");
			if(body.variables != null){
			f.genInstr("","enter" ,"$" + (32 + (body.variables.variables.size() * 4)) + ",$1", "");
			}
			else {
				f.genInstr("","enter" ,"$32,$1", "");
				
			}
			body.genCode(f);
			f.genInstr("", "leave", "", "");
			f.genInstr("", "ret", "", "");
		
		
			
			
	}
}
