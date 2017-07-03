package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

import java.util.ArrayList;

public class Term extends PascalSyntax {

	ArrayList<Factor> fac = new ArrayList<>();
	ArrayList<FactorOpr> facopr = new ArrayList<>();
	//en vilkaaerlig mengde factorer og factor operatorer

	
	types.Type type;
	Term(int n) {
		super(n);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String identify() {
		return "<Term> on line " + lineNum;
	}

	public void prettyPrint() {
		fac.get(0).prettyPrint();
		for (int i = 1; i < fac.size(); i++) {
			facopr.get(i - 1).prettyPrint();
			fac.get(i).prettyPrint();

		}

	}

	static Term parse(Scanner s) {
		enterParser("term");
		Term trm = new Term(s.curLineNum());
		trm.fac.add(Factor.parse(s));
		while (s.curToken.kind == multiplyToken || s.curToken.kind == modToken || s.curToken.kind == divToken
				|| s.curToken.kind == andToken) {
			//hvis vi har en factor operator , aa maa den ha en factor aa operere paa
			trm.facopr.add(FactorOpr.parse(s));

			trm.fac.add(Factor.parse(s));

		}

		leaveParser("term");
		return trm;
	}
	void check(Block curScope,Library lib){
		Variable tmp;
		fac.get(0).check(curScope, lib);
		type = fac.get(0).type;
			for(int i = 1;  i<fac.size();i++){
				fac.get(i).check(curScope,lib);
				if(fac.get(i) instanceof Variable){
					tmp = (Variable) fac.get(i);
					tmp.varRef.checkWhetherValue(this);
				}
				type.checkType(fac.get(i).type, "not Equal", this, "Types are not equal");
				if(facopr.get(i-1).operator.equals("and")){
					type.checkType(lib.booleantype, "Boolen type", this, "Not a boolean");
				
				}
				else {
					type.checkType(lib.inttype, "Integer type", this, "Not integer type");
				}
			
			}
		}
	public void genCode(CodeFile f) {
		fac.get(0).genCode(f);
		for (int i = 1; i < fac.size(); i++) {
			f.genInstr("", "pushl", "%eax", "");
			fac.get(i).genCode(f);
			f.genInstr("", "movl", "%eax,%ecx", "");
			f.genInstr("", "popl", "%eax", "");
			if (facopr.get(i-1).operator == "*") {
				f.genInstr("", "imull", "%ecx,%eax", "");
			}
			else if (facopr.get(i-1).operator == "div") {
				f.genInstr("", "cdq", "", "");
				f.genInstr("", "idivl", "%ecx", "");
			}
			else if (facopr.get(i-1).operator == "mod") {
				f.genInstr("", "cdq", "", "");
				f.genInstr("", "idivl", "%ecx", "");
				f.genInstr("", "movl", "%edx,%eax", "");
			}
			else {
				f.genInstr("", "andl", "%ecx,%eax", "");
			}
		}
	}
}
