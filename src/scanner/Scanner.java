package scanner;

import main.Main;
import static scanner.TokenKind.*;

import java.io.*;

public class Scanner {

	public Token curToken = null, nextToken = null;

	private LineNumberReader sourceFile = null;
	private String sourceFileName, sourceLine = "";
	private int sourcePos = 0;

	public Scanner(String fileName) {
		sourceFileName = fileName;
		try {
			sourceFile = new LineNumberReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			Main.error("Cannot read " + fileName + "!");
		}
		readNextLine();/// la til denne , maa lese inn en linje for man starter
						/// behandling

		readNextToken();
		readNextToken();
	}

	public String identify() {
		return "Scanner reading " + sourceFileName;
	}

	public int curLineNum() {
		return curToken.lineNum;
	}

	private void error(String message) {
		Main.error("Scanner error on " + (curLineNum() < 0 ? "last line" : "line " + curLineNum()) + ": " + message);
	}

	public void readNextToken() {

		curToken = nextToken;
		nextToken = null;
		// this just goes to the nextline in case the end of the line has been
		// found.
		if (sourcePos >= sourceLine.length() - 1) {
			readNextLine();

		}

		/*
		 * Del 1 her: here we skip all skippable code, and also check if it is
		 * the end of the file. If it is, the skipStuff method returns 2, and if
		 * not, it doesnt. no matter which, it does skip all skippable code.
		 */
		if (skipStuff() == 2) {
			nextToken = new Token(eofToken, getFileLineNum());

			// if the first character we find is not a special one, it will try
			// and extract a string for making a token.
		} else if (!ifspeschar(sourceLine.charAt(sourcePos))) {
			int stringstart = sourcePos;
			while (true) {
				// if a spes char is found , make the string and breaks out of
				// the loop.
				if (ifspeschar(sourceLine.charAt(sourcePos))) {

					if (isInteger(sourceLine.substring(stringstart, sourcePos))) {

						nextToken = new Token(Integer.parseInt(sourceLine.substring(stringstart, sourcePos)),
								getFileLineNum());
						break;
					} else {
						nextToken = new Token(sourceLine.substring(stringstart, sourcePos).toLowerCase(), getFileLineNum());
						break;
					}
				} else {
					// if not found it continues searching.

					sourcePos++;

				}

			}

		}
		/*
		 * here we find all the special tokens, by checking for each and every
		 * one of them. there are some of them that are 2 chars long, and these
		 * we check for when we find the first character.
		 */
		else {
			if (sourceLine.charAt(sourcePos) == '+') {
				nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
				nextToken.kind = addToken;
				sourcePos++;

			} else if (sourceLine.charAt(sourcePos) == ':') {
				if (sourceLine.charAt(sourcePos + 1) == '=') {
					/// assign token
					nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
					nextToken.kind = assignToken;
					sourcePos += 2;

				} else {
					// colon token
					nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
					nextToken.kind = colonToken;
					sourcePos++;
				}

			} else if (sourceLine.charAt(sourcePos) == ',') {
				// comma token
				nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
				nextToken.kind = commaToken;
				sourcePos++;
			} else if (sourceLine.charAt(sourcePos) == '.') {
				if (sourceLine.charAt(sourcePos + 1) == '.') {
					// range roken
					nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
					nextToken.kind = rangeToken;
					sourcePos += 2;

				} else {
					nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
					nextToken.kind = dotToken;
					sourcePos++;
				}

			}

			else if (sourceLine.charAt(sourcePos) == '=') {
				// equals toke n
				nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
				nextToken.kind = equalToken;
				sourcePos++;

			} else if (sourceLine.charAt(sourcePos) == '>') {
				if (sourceLine.charAt(sourcePos + 1) == '=') {
					// greaterequal token
					nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
					nextToken.kind = greaterEqualToken;
					sourcePos += 2;
				} else {
					// greatertoken
					nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
					nextToken.kind = greaterToken;
					sourcePos++;
				}

			} else if (sourceLine.charAt(sourcePos) == '[') {
				// left bracket token
				nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
				nextToken.kind = leftBracketToken;
				sourcePos++;

			}

			else if (sourceLine.charAt(sourcePos) == '(') {
				nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
				nextToken.kind = leftParToken;
				sourcePos++;

				// lwft par token
			} else if (sourceLine.charAt(sourcePos) == '<') {
				if (sourceLine.charAt(sourcePos + 1) == '=') {
					// lesequals token
					nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
					nextToken.kind = lessEqualToken;
					sourcePos += 2;
				} else if (sourceLine.charAt(sourcePos + 1) == '>') {
					// not equal token
					nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
					nextToken.kind = notEqualToken;
					sourcePos += 2;
				} else {
					// less token
					nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
					nextToken.kind = lessToken;
					sourcePos++;
				}

			} else if (sourceLine.charAt(sourcePos) == '*') {
				// multiply token
				nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
				nextToken.kind = multiplyToken;
				sourcePos++;

			} else if (sourceLine.charAt(sourcePos) == ']') {
				// right braket token
				nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
				nextToken.kind = rightBracketToken;
				sourcePos++;

			} else if (sourceLine.charAt(sourcePos) == ')') {
				nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
				nextToken.kind = rightParToken;
				sourcePos++;
			} else if (sourceLine.charAt(sourcePos) == ';') {
				nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
				nextToken.kind = semicolonToken;
				sourcePos++;

			} else if (sourceLine.charAt(sourcePos) == '-') {
				// subtract token
				nextToken = new Token(sourceLine.charAt(sourcePos), getFileLineNum());
				nextToken.kind = subtractToken;
				sourcePos++;
			} else {
				// if not a special char , then a normal char
				if (sourceLine.charAt(sourcePos) == '\'' && sourceLine.charAt(sourcePos + 1) == '\''
						&& sourceLine.charAt(sourcePos + 2) == '\'' && sourceLine.charAt(sourcePos + 3) == '\'') {
					// this is for the case of having a ' character, which is
					// notified by '''', which makes it 4 characters long, not
					// 3.
					nextToken = new Token('\'', getFileLineNum());
					sourcePos += 4;
				}
				else if (sourceLine.charAt(sourcePos) == '\'' && sourceLine.charAt(sourcePos + 2) == '\'') {
					// if statement checks if the char is correctly formatted
					nextToken = new Token(sourceLine.charAt(sourcePos + 1), getFileLineNum());
					sourcePos += 3;
					
				} else if (sourceLine.charAt(sourcePos) == '\'') {
					//If there is a ' character it is not considered a generic illegal character, and so need its own error message.
					error("Illegal char literal!");
				} else {
					//this error is if there is a character that is not allowed to use normally. 
					error("Illegal character: \'" + sourceLine.charAt(sourcePos) + "\'!");
				}

			}

		}

		Main.log.noteToken(nextToken);
	}

	private void readNextLine() {
		if (sourceFile != null) {
			
			try {
				sourceLine = sourceFile.readLine();
									
				if (sourceLine == null) {
					sourceFile.close();
					sourceFile = null;
					sourceLine = "";
				} else {
					sourceLine += " ";
				}
			
				sourcePos = 0;
			} catch (IOException e) {
				Main.error("Scanner error: unspecified I/O error!");
			}
		}
		
		
		if (sourceFile != null)
			Main.log.noteSourceLine(getFileLineNum(), sourceLine);
	}

	private int getFileLineNum() {
		return (sourceFile != null ? sourceFile.getLineNumber() : 0);
	}

	// Character test utilities:

	private boolean isLetterAZ(char c) {
		return 'A' <= c && c <= 'Z' || 'a' <= c && c <= 'z';
	}

	private boolean isDigit(char c) {
		return '0' <= c && c <= '9';
	}

	// Parser tests:

	public void test(TokenKind t) {
		if (curToken.kind != t)
			testError(t.toString());
	}

	public void testError(String message) {
		Main.error(curLineNum(), "Expected a " + message + " but found a " + curToken.kind + "!");
	}

	public void skip(TokenKind t) {
		test(t);
		readNextToken();
	}

	/*
	 * This method skips the parts of the code that is nto supposed to be read
	 * and inturpreted. It also finds if it is the end of the file, and if so it
	 * returns 2 instead of 1 or 0.
	 */
	int skipStuff() {

		// if a line is empty that means the end of the file has been reached,
		// and therefore it returns 2.
		if (sourceLine.equals("")) {
			return 2;
		}
		// if the line is empty, read the next one
		else if (sourceLine.equals(" ")) {
			readNextLine();
			if (sourceLine.equals("")) {// eof check
				return 2;
			}
		}
		if (sourceLine.length() == sourcePos) {// end of line
			// nextline
			readNextLine();
			if (sourceLine.equals("")) {// eof check
				return 2;
			}
		}
		// When it encounters a space it merely skips it.
		else if (sourceLine.charAt(sourcePos) == ' ') {
			sourcePos++;
		}
		// When it encounters a tab it skips it too.
		else if (sourceLine.charAt(sourcePos) == '	') {
			sourcePos++;
		}
		/*
		 * When it encounters a comment it goes on until it can find the closing
		 * characters. It can go for multiple lines by calling the
		 * readNextLine() method.
		 */
		else if (sourceLine.charAt(sourcePos) == '/') {
			if (sourcePos + 1 == sourceLine.length()) {
				return 0;
			}
			if (!(sourceLine.charAt(sourcePos + 1) == '*')) {
				return 0;
			}
			sourcePos += 2;
			while (!((sourceLine.charAt(sourcePos - 1) == '*') && (sourceLine.charAt(sourcePos) == '/'))) {
				if (sourceLine.length() == sourcePos) {
					//nextline
					readNextLine();
					if (sourceLine.equals("")) {
						error("No end for comment starting on line " + curLineNum() + "!");
						System.exit(0);
						return 2;
					}
				}
				sourcePos++;
			}
			sourcePos++;
		}
		// this is also for skipping comments.
		else if (sourceLine.charAt(sourcePos) == '{') {
			sourcePos++;
			while (!(sourceLine.charAt(sourcePos - 1) == '}')) {
				if (sourceLine.length() == sourcePos) {
					// nextline
					readNextLine();
					if (sourceLine.equals("")) {
						return 2;
					}
				}
				if (sourceLine.charAt(sourcePos) == '\n') {
					// nextline
					readNextLine();
					if (sourceLine.equals("")) {
						return 2;
					}
				}
				sourcePos++;
			}

		}
		/*
		 * If the Method finds skippable chars, it will call on itself again
		 * because it is supposed to skip all skippable chars. When it doesnt
		 * find any skippable chars, it ends.
		 */
		else {
			return 0;
		}
		return skipStuff();

	}

	// this checks if a character is not a letter or number.
	public boolean ifspeschar(char a) {
		if (isDigit(a) || isLetterAZ(a)) {
			return false;

		}

		return true;
	}
	// this method checks if a string is really a number, that is when all the
	// characters in the string are numbers.

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}

		return true;
	}
}
