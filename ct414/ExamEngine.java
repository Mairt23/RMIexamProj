
package ct414;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;

public class ExamEngine implements ExamServer {
	
	private ArrayList<String> cCodes;
	
	private String[] mathsQs;
	private String[][] mathsAs;
	private String[] computersQs;
	private String[][] computersAs;
	
	private ArrayList<Integer> users;
	private ArrayList<String> passwords;
	private ArrayList<Integer> tokens;
	private ArrayList<Date> dates;
	
	//private ArrayList<ArrayList<Assessment>> oldAss;
	private Map<Integer, ArrayList<Assessment>> saveans = new HashMap<Integer, ArrayList<Assessment>>();
	
    // Constructor is required
    public ExamEngine() {
        super();
        cCodes = new ArrayList<String>(Arrays.asList("ma101", "cs101"));
        mathsQs = new String[] {"1+1=", "5+4=", "9-7=", "13-7="};
        mathsAs = new String[][] {{"1","2","3","4"},{"5","7","9","11"},{"0","2","4","3"},{"7","6","9","5"}};
        computersQs = new String[] {"Which of the following is a type of loop?", "Which of the following is a common conditional clause keyword?", "What does [] commonly signify?", "What letter precided by a \\ sigifies the end of line in a file?"};
        computersAs = new String[][] {{"when","file","else","while"},{"if","for","open","move"},{"a file","an array","an int","a dictionary"},{"t","s","n","b"}};
        
        users = new ArrayList<Integer>(Arrays.asList(123,456,789));
        passwords = new ArrayList<String>(Arrays.asList("pass", "word", "hi"));
		dates = new ArrayList<Date>(Arrays.asList());
		Date tempdate = new Date(2016, 2, 6);
		dates.add(tempdate);
		dates.add(tempdate);
		
		int j;
		for(j = 0; j < users.size(); j++){
			saveans.put(users.get(j), new ArrayList<Assessment>(Arrays.asList()));
		}
    }

    // Implement the methods defined in the ExamServer interface...
    // Return an access token that allows access to the server for some time period
    public int login(int studentid, String password) throws 
                UnauthorizedAccess, RemoteException {
				
				if(users.contains(studentid)){
					if(passwords.contains(password)){
						int token = Random.nextInt();
						tokens.add(token); //!!!!Need to set seed!!!!!!!
						return(token);
						}
					else{
						throw new UnauthorizedAccess("Password not recognised");
					}
				}
				else{
					throw new UnauthorizedAccess("Student I.D not recognised");
					}
    }

    // Return a summary list of Assessments currently available for this studentid
    public List<String> getAvailableSummary(int token, int studentid) throws
                UnauthorizedAccess, NoMatchingAssessment, RemoteException {
				
				if(!users.contains(studentid)){
					throw new UnauthorizedAccess("student i.d not recognized");
					}
				if(!tokens.contains(token)){
					throw new UnauthorizedAccess("login failed, please ensure you have logged in.  ");
					}
				return (cCodes);

        return null;
    }

    // Return an Assessment object associated with a particular course code
    public Assessment getAssessment(int token, int studentid, String courseCode) throws
                UnauthorizedAccess, NoMatchingAssessment, RemoteException {
				
				int i;
				int j;
				ArrayList <String> studcodes;
				ArrayList <Assessment> studrec = saveans.get(studentid);
				for(j = 0; j < studrec.size(); j++){
					studcodes.add(studrec.get(0).getInformation());
				}
				if(cCodes.indexOf(courseCode)==0){
					if(studcodes.contains(cCodes.get(0))){
						return(studrec.get(studcodes.indexOf(cCodes.get(0))));
						}
					else{
						List<Question> questions = new ArrayList<Question>(Arrays.asList());
						for(i = 0; i < mathsQs.length; i++){
							questions.add(new q(mathsQs[i], mathsAs[i], i));
							}
						Test temp = new Test(cCodes.get(0), dates.get(0), questions, studentid);
						return(temp);
					}
				}
				else if(cCodes.indexOf(courseCode)==1){
					if(studcodes.contains(cCodes.get(1))){
						return(studrec.get(studcodes.indexOf(cCodes.get(1))));
						}
					else{
						List<Question> questions = new ArrayList<Question>(Arrays.asList());
						for(i = 0; i < computersQs.length; i++){
							questions.add(new q(computersQs[i], computersAs[i], i));
							}
						Test temp = new Test(cCodes.get(1), dates.get(1), questions, studentid);
						return(temp);
					}
				}
				else{
					throw new NoMatchingAssessment("No assessment for course code");
					}
    }

    // Submit a completed assessment
    public void submitAssessment(int token, int studentid, Assessment completed) throws 
                UnauthorizedAccess, NoMatchingAssessment, RemoteException {
				
				if(!users.contains(studentid)){
					throw new UnauthorizedAccess("student i.d not recognized");
				}
				if(!tokens.contains(token)){
					throw new UnauthorizedAccess("login failed, please ensure you have logged in.  ");
				}
				ArrayList<Assessment> sturec = saveans.get(studentid);
				sturec.add(completed);
				saveans.put(studentid, sturec);
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "ExamServer";
            ExamServer engine = new ExamEngine();
            ExamServer stub =
                (ExamServer) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("ExamEngine bound");
        } catch (Exception e) {
            System.err.println("ExamEngine exception:");
            e.printStackTrace();
        }
    }
}
