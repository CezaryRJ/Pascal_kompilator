package parser;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

import java.util.ArrayList;

public class StatmList extends PascalSyntax {
	ArrayList<Statement> list = new ArrayList<>();

	// har en vilkaarlig mengde statements i seg
	StatmList(int n) {
		super(n);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String identify() {
		return "<statm-list> on line " + lineNum;

	}

	public void prettyPrint() {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).prettyPrint();
			Main.log.prettyPrintLn();
		}
	}

	static StatmList parse(Scanner s) {
		enterParser("statm-list");

		// saa lengde vi finner kommaer forventes det ett nytt element
		StatmList stmlst = new StatmList(s.curLineNum());
		stmlst.list.add(Statement.parse(s));
		while (s.curToken.kind == semicolonToken) {
			s.skip(semicolonToken);
			stmlst.list.add(Statement.parse(s));
		}

		leaveParser("statm_list");
		return stmlst;

	}
	void check (Block curScope,Library lib){
		for(int i = 0; i < list.size();i++){
			list.get(i).check(curScope,lib);
			
		}
		
	}
	public void genCode(CodeFile f){
		for(int i = 0; i < list.size();i++){
			list.get(i).genCode(f);
		}
	}

}
