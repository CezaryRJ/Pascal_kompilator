
package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class AssignStatm extends Statement {

	AssignStatm(int lNum) {
		super(lNum);
		// TODO Auto-generated constructor stub
	}

	Variable var;
	Expression exp;

	public String identify() {
		return "<assign-statm> on line " + lineNum;
	}

	public void prettyPrint() {
		var.prettyPrint();
		Main.log.prettyPrint(" := ");
		exp.prettyPrint();
		Main.log.prettyPrintLn(";");

	}

	static AssignStatm parse(Scanner s) {
		enterParser("assign-statm");
		AssignStatm as = new AssignStatm(s.curLineNum());

		as.var = Variable.parse(s);
		s.skip(assignToken);
		as.exp = Expression.parse(s);

		leaveParser("assign-statm");
		return as;

	}

	void check(Block curScope,Library lib){
		var.check(curScope,lib);
		exp.check(curScope,lib);
		var.varRef.checkWhetherAssignable(this);
		var.type.checkType(exp.type, "same type", this, "not same type");
		
		
		
	}

	
	void genCode(CodeFile f) {
		exp.genCode(f);
	
		if (var.varRef instanceof VarDecl){
			f.genInstr("", "movl",   (var.varRef.level * -4)+ "(%ebp),%edx" ,"");
			f.genInstr("", "movl", "%eax," + ((((VarDecl) var.varRef).rekke * -4) - 36) + "(%edx)", "");
			
		}
		else if (var.varRef instanceof FuncDecl){
			f.genInstr("", "movl", -4 * var.varRef.level + "(%ebp),%edx", "");
			f.genInstr("", "movl", "%eax,-32(%edx)", "");
			
		}
		
		
	}
}
