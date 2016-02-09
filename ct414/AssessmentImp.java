//AssessmentImp.java

package ct414

public class AssessmentImp implements Assessment{
	public Assessment(String information, Date closingDate, List<Question> questions, int stuID){
		int student = stuID, lastQuestion;
		String information = information;
		Date closingDate = closingDate;
		List<Question> questions = questions;
		ArrayList<int> answers = answersInit(questions.size());
		super();
	}
	
	private ArrayList<int> answersInit(int size){
		int i;
		ArrayList<int> answers = new ArrayList<int>(size);
		for (i = 0; i < size; i++){
			answers.add(i,-1);
		}
		return(answers);
	}
	
	public String getInformation(){
		return(information);
	}
	
	public Date getClosingDate(){
		return(closingDate);
	}
	
	public List<Question> getQuestions(){
		return(questions);
	}
	
	public Question getQuestion(int questionNumber) throws 
		InvalidQuestionNumber{
		return(questions.get(questionNumber));
	}
	
	public void selectAnswer(int questionNumber, int optionNumber) throws
		InvalidQuestionNumber, InvalidOptionNumber{
		answers.set(questionNumber, optionNumber);
		lastQuestion = questionNumber;
	}
	
	public int getSelectedAnswer(){
		if (lastQuestion == null){
			System.out.println("No questions answered yet");
			return(0);
		}
		else if (answers.get(lastQuestion) == -1){
			System.out.println("No answer selected yet");
			return(0);
		}
		else{
			return(answers.get(lastQuestion));
		}
	}
	
	public int getAssociatedID(){
		return(student);
	}
}
