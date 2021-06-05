package sgradeprogram;

import java.util.Scanner;

/* Boundary classes:
	- Keypad represents the keypad of the ATM.
	- Screen represents the screen of the ATM.
	- Menu represents the main menu of the ATM
*/

class Keypad {
	private Scanner input;
	                      
	public Keypad() { input = new Scanner( System.in ); } 
	public int getInput() { return input.nextInt(); }
	public String getStringinput() {return input.next();}
}


class Screen {
	public void displayMessage( String message ) { System.out.print( message ); }
	public void displayMessageLine( String message ) { System.out.println( message ); }
	public void displaystudentinfo( Student sinfo ) {  } 
}

class Menu {
	// menu option constants
	public static final int Studentinfoinput = 1;
	public static final int Studentgradecal = 2;
	public static final int Printstudentgrade = 3;
	public static final int EXIT=4;

	// display main menu and get user command
	public int displayMainMenu(Screen screen, Keypad keypad) {
		screen.displayMessageLine( "\n\t\t     MENU:\n" );
		screen.displayMessageLine( "\t\t1 - inputstudentinfo" );
		screen.displayMessageLine( "\t\t2 - calstudentgrade" );
		screen.displayMessageLine( "\t\t3 - printstudentgrade" );
		screen.displayMessageLine( "\t\t4 - exit");
		screen.displayMessage( "\tChoice: " );
		return keypad.getInput();
	}
}
/*
	message management class
*/

class Message {
	// constants corresponding to messages
	public static final String WELCOME = "\nWelcome!\n";
	public static final String GOODBYE = "\tBye...";
	
	public static final String ERR_CHOICE = "\tWrong choice. Enter number between 1 and 5";
	
	public static final String INPUT_NAME = "\tEnter student name: ";
	public static final String INPUT_STUINFO ="name:\tKoreansorce:\tEnglishscore:\tMathscore:\t";
	public static final String SETGRADESTART ="\set current database student avg and grade";
	public static final String PRINTALLAVGANDGRADE ="name:\tavg:\tgrade:";
	public static final String SETGRADEEND ="\set all current database student avg and grade";
}
/* Entity classes:
	- Account represents a bank account.
	- BankDatabase represents the bank account information database.
*/

class Student {
	private String name;
	private int Koreanscore;
	private int Englishscore;
	private int Mathscore;
	private double avgscore;
	private String grade;
	
	public Student()  {
		
	}
	
	public String getstudentname() {return name;}
	public int getKoreanscore() {return Koreanscore;}
	public int getEnglishscore() {return Englishscore;}
	public int getMathscore() {return Mathscore;}
	public void setnewbasicinfo(String inname, int ks, int es, int ms) {
		name=inname;
		Koreanscore=ks;
		Englishscore=es;
		Mathscore=ms;
		avgscore=0;
		grade="";
	}
	public void setavgandgrade() {
		avgscore=(Koreanscore+Englishscore+Mathscore)/3;
		if(avgscore>=90) {
			grade="A";
			return;
		}
		else if(avgscore>=80) {
			grade="B";
			return;
		}
		else if(avgscore>=70) {
			grade="C";
			return;
		}
		else if(avgscore>=60) {
			grade="D";
			return;
		}
		else {
			grade="F";
		}
	}
	public String printstudentgrade() {
		return this.grade;
	}
	public double printstudentavg() {
		return this.avgscore;
	}
} // end class Account

class StudentDatabase { // class BankDatabase<Integer, Account> extends TreeMap<Integer, Account>
	private Student students[]; // array of Accounts
	private int stindex=0;
	public StudentDatabase() { 
		students = new Student[ 10 ]; 
	      	
	} 
	
	public String getstudentname(String name) {return getStudent(name).getstudentname();}
	public int getKoreanscore(String name) {return getStudent(name).getKoreanscore();}
	public int getEnglishscore(String name) {return getStudent(name).getEnglishscore();}
	public int getMathscore(String name) {return getStudent(name).getMathscore();}
	
	public void setnewbasicinfo(String inname, int ks, int es, int ms) {
		Student temp=new Student();
		temp.setnewbasicinfo(inname, ks, es, ms);
		students[stindex]=temp;
		stindex++;
	}
	public void setavgandgrade() {
		for ( Student tst : students ) {
			if(tst==null)
			{
				return;
			}
			else {
			tst.setavgandgrade();
			}
		}
	}
	public void printallStudnetgrade() {
		for ( Student tst : students ) {
			if(tst==null)
			{
				return;
			}
			else {
				System.out.println(tst.getstudentname()+"\t"+tst.printstudentavg()+"\t"+tst.printstudentgrade());
			}
		}
	}
	// helper method
	private Student getStudent( String name ) {
		for (  int i=0;i<10;i++ ) {
			if ( students[i].getstudentname().equals(name)) 
				return students[i];
		}
		return null;
	} 
} // end class StudentDatabase
/* Control classes:
	- ATM represents an automated teller machine (MAIN CONTROLLER)
		(1) accepts user command
		(2) performs the command using DELEGATION to Transaction
	- ITransaction is an interface representing an ATM transaction per use case.
	- Transcation is an adapter class that implements Transaction. (for code reuse)
	- BalanceInquiry represents a balance inquiry ATM transaction.
	- Deposit represents a deposit ATM transaction.
	- Withdrawal represents a withdrawal ATM transaction.
*/

interface ITransaction {
	abstract public StudentDatabase getStudentDatabase();
	abstract public void 		execute();

}

// class CreateAccount implements ITransaction {...}

class Transaction implements ITransaction{
	//protected String curStudentname;
	protected Screen screen;		// composition of a collaborating class
	protected StudentDatabase database; 	// composition of a collaborating class

	public Transaction(  Screen screen, StudentDatabase database ) {
		
		this.screen = screen;
		this.database = database;
	} 
 
	public StudentDatabase getStudentDatabase() { 
		return database; 
	}

	public void execute() {
		// do nothing ( or abstract void execute(); )
	}
} // end class Transaction

class Inputstudentinfo extends Transaction {
	protected Keypad keypad;
	public Inputstudentinfo( Screen screen, StudentDatabase database, Keypad keypad ) {
		super( screen, database );
		this.keypad=keypad;
	}
	
	// performs the task of input new student
	public void execute() {
		screen.displayMessageLine( Message.INPUT_STUINFO); 
		String nstu=keypad.getStringinput();
		int tks=keypad.getInput();
		int tes=keypad.getInput();
		int tms=keypad.getInput();
		getStudentDatabase().setnewbasicinfo(nstu, tks, tes, tms);
	} 
} // end class BalanceInquiry


class Setavggrade extends Transaction {
	

	public Setavggrade( Screen screen, StudentDatabase database ) {
		super( screen, database );
	}

	/* performs the task of set grades */
	public void execute() {
		screen.displayMessageLine(Message.SETGRADESTART);
		getStudentDatabase().setavgandgrade();
		screen.displayMessageLine(Message.SETGRADEEND);
	}
} // end class Deposit


class Printgrade extends Transaction {
	
	
	public Printgrade( Screen screen, StudentDatabase database ) {
		super(  screen, database );
	}

	// perform transaction
	public void execute() {
		screen.displayMessageLine(Message.PRINTALLAVGANDGRADE);
		getStudentDatabase().printallStudnetgrade();
	} 
} // end class Printgrade

class Ssystem {
	private String curStudentname; // current student name

	/* Collaborating classes */
	private Screen screen;
	private Keypad keypad;
	private Menu mainMenu;
	private StudentDatabase database;
	ITransaction currentTransaction = null; // Polymorphic Composition

	public Ssystem() {
		
		curStudentname= ""; // no current account number to start
		screen = new Screen();
		keypad = new Keypad();
		mainMenu = new Menu();
		database = new StudentDatabase();
	}

	/* start ATM : welcome, authenticate user, perform transactions */
	public void run() {
		while ( true ) {
			screen.displayMessageLine(Message.WELCOME);
			performTransactions(); // Do THE REQUIRED TASK

			// initializatin for next ATM session
			
			curStudentname = "";
			screen.displayMessageLine(Message.GOODBYE);
		} 
   	} // run()

	// display main menu and Execute transactios
	private void performTransactions() {
			boolean usercexit=true;
		while ( usercexit ) {     
			// (1) accepts user request (command)
			int command = mainMenu.displayMainMenu(screen, keypad);

			// (2) performs the command using DELEGATION to Transaction
			switch ( command ) {
			case Menu.Studentinfoinput:
			case Menu.Studentgradecal: 
			case Menu.Printstudentgrade:
				currentTransaction = createTransaction( command );
				currentTransaction.execute();
				currentTransaction = null;
				break; 
			case Menu.EXIT:
				System.exit(0);
				break;
				
			default:
				screen.displayMessageLine(Message.ERR_CHOICE);
				break;
			} 
		}
	} // performTransactions()
   
	// Create transaction
	private ITransaction createTransaction( int type ) {
		ITransaction temp = null;
	   
		switch ( type ) {
		case Menu.Studentinfoinput:
			temp = new Inputstudentinfo(screen,database,keypad);
			break;
		case Menu.Studentgradecal:
			temp = new Setavggrade(screen,database);
			break; 
		case Menu.Printstudentgrade:
			temp = new Printgrade(screen,database);
			break;
		}
		return temp; 
	}
} // end class Ssystem
/*
	Driver program and ATM machine
	- (usually performs configuration/initialization tasks)
	- Create and run an ATM object

*/
public class Smgmt {
	public static void main( String[] args )  {
		Ssystem stm = new Ssystem();    
		stm.run();
	} 
}

//end
