//clientApp.java

package ct414;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.List;

public class clientApp{
	public static void main(String args[]){
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "ExamServer";
            Registry registry = LocateRegistry.getRegistry(args[0]);
            ExamServer examServ = (ExamServer) registry.lookup(name);
            Scanner scanner = new Scanner(System.in);
            String password = args[2], choice = "";
            int stuID = Integer.parseInt(args[1]), token = examServ.login(stuID, password);
            System.out.print("Welcome student ");
            System.out.print(stuID);
            System.out.print("\n");
            System.out.println("Note: At any stage type the word 'exit' and press the return key to log off");
            while (true){
            	System.out.println("Open assessments:");
            	//May need to manually print this list. On compile: yep!!
            	System.out.print(examServ.getAvailableSummary(token, stuID));
            	System.out.println("Which assessment would you like to complete?(give course code)");
            	//May need a stronger check than this one
            	while (choice == ""){
            		choice = scanner.next();
            		System.out.format("%s is not a valid input. Please type assessment course code or type 'exit' and hit return.\n", choice);
            	}
            	if (choice == "exit"){
            		break;
            	}
            	//May need to check that a user is still logged on at each of the 2 below stages
            	Assessment test = examServ.getAssessment(token, stuID, choice);
				test = runTest(test);
            	examServ.submitAssessment(token, stuID, test);
            }
        }
        catch (Exception e) {
            System.err.println("clientApp exception:");
            e.printStackTrace();
        }
    }
    
    private static Assessment runTest(Assessment exam){
    	List<Question> Qs = exam.getQuestions(); 
		List<Integer> openQs;
    	Scanner scanner = new Scanner(System.in);
    	int choice = -2, ans, ind;
    	System.out.println("Information on this Assessment:");
    	System.out.print(exam.getInformation());
    	for (int i = 1; i <= Qs.size(); i++){
    		openQs.add(i);
    	}
    	while (true){
    		for (int i = 0; i < openQs.size(); i++){
    			//May need to print questions and answers
				//Need to access Lists below using iterators
    			System.out.format("Question %d %s \n", openQs[i], Qs[openQs[i]]);
    		}
    		System.out.println("Specify Question number that you want to answer or 0 to exit:");
    		choice = Integer.parseInt(scanner.next());
    		while (choice < 0 && choice >= Qs.size()){
            		choice = Integer.parseInt(scanner.next());
            		System.out.format("%d is not a valid input. Please give question number or 0 to exit:\n", choice);
            }
            if (choice == 0){
            	break;
            }
            Question curQuestion = exam.getQuestion(choice-1);
            System.out.println(curQuestion.getQuestionDetail());
            String[] posAnswers = curQuestion.getAnswerOptions();
            for (int j = 1; j < posAnswers.length; j++){
            	System.out.format("%d %s \n", j, posAnswers[j-1]);
            }
            System.out.println("Please give answer number or 0 to leave blank:");
            ans = Integer.parseInt(scanner.next());
            while (ans < 0 && ans >= posAnswers.length){
            	System.out.println("Please give a valid answer number or 0 to leave blank:");
            	ans = Integer.parseInt(scanner.next());
            }
            if (ans == 0){
            	continue;
            }
            else{
            	exam.selectAnswer(choice-1, ans-1);
            	ind = openQs.indexOf(choice);
				if (ind != -1){
					openQs.remove(ind);
				}
            }
        }
        return(exam);
    }
}
            	
