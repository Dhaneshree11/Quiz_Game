
import java.io.*;
import java.util.Scanner;


abstract class Question{
	String question;
	String correctAnswer;
	public Question(String question,String correctAnswer){
		this.question = question;
		this.correctAnswer = correctAnswer;
	}
	abstract void display();
	abstract boolean checkAnswer(String answer);
		
	
}
class MultipleChoiceQuestion extends Question{
     String[] options;
	public MultipleChoiceQuestion(String question, String correctAnswer,String[] options) {
		super(question, correctAnswer);
		this.options = options;
		
	}

	@Override
	void display() {
		System.out.println("\n"+question);
		for(String option : options) {
			System.out.println(" "+option);
		}
		
	}



	@Override
	boolean checkAnswer(String answer) {
		
		return answer.trim().equalsIgnoreCase(correctAnswer);
	}
	
}
class TrueFalseQuestion extends Question{
	public TrueFalseQuestion(String question, String correctAnswer) {
		super(question,correctAnswer);
	}

	@Override
	public void display() {
		System.out.println("\n  "+question+"(True/False)");
		
	}

	@Override
	public boolean checkAnswer(String answer) {
		return answer.trim().equalsIgnoreCase(correctAnswer);
		
		
	}
	
}


class QuestionFactory{
	public static Question createMCQ(String question,String correctAnswer,String[] options) {
		return new MultipleChoiceQuestion(question,correctAnswer,options);
		
	}
	public static Question createTrueFalse(String question, String correctAnswer) {
		return new TrueFalseQuestion(question,correctAnswer);
	}
}

class QuizCategory{
	String name;
	Question[] questions;
	int count = 0;
	public QuizCategory(String name,int size) {
		this.name = name;
		this.questions = new Question[size];
	}
	public String getName() {
		return name;
	}
	public Question[] getQuestions() {
		return questions;
	}
	public void addQuestion(Question q) {
		questions[count] = q;
		count++;
	}
	
	
	
}
class ScoreFilehandler{
	 static String  File = "Quiz_scores.txt";
	 public static void saveScore(String player, String category,int score) throws IOException {
		 File file = new File("D:\\\\File Handling\\Quiz_scores.txt");
		 FileWriter write = new FileWriter(file,true);
		 BufferedWriter writebuffer = new BufferedWriter(write);
		 writebuffer.write("Player"+player+" Category "+category+"  Scores: "+score);
		 writebuffer.newLine();
		 writebuffer.close();
	 }
	 public static void loadScores() throws IOException {
		 File file = new File("D:\\\\File Handling\\Quiz_scores.txt");
		 FileReader read = new FileReader(file);
		 BufferedReader readBuffer = new BufferedReader(read);
		 String line;
		 int x = readBuffer.read();
//		 while(x!=-1) {
//			 char ch = (char)x;
//			 System.out.print(ch);
//			 x = readBuffer.read();
//		 }
		 while((line=readBuffer.readLine())!=null) {
			 System.out.println(line);
		 }
		 readBuffer.close();
	 }
	 
}

class QuestionTimer implements Runnable{
	int seconds;
	boolean stopped = false;
	boolean timeup = false;
	public QuestionTimer(int seconds) {
		this.seconds = seconds;
	}
	
	public void run() {
		while(seconds>0) {
			if(stopped) return;
			System.out.println("Time left: "+seconds+"  Seconds");
			seconds--;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				return;
			}
		}
		timeup = true;
	}
	
}

class QuizManager{
	static QuizManager instance;
	int highScore;
	String highScorePlayer;
	private QuizManager(){
		
	}
	public static QuizManager getInstance() {
	
		if(instance==null) {
			instance = new QuizManager();
			
		}
		return instance;
	}
	
	public void updateHighScore(String player,int score) {
		if(score>highScore) {
			highScore = score ;
			highScorePlayer = player;
		}
	}
	public void showHighScore() {
		System.out.println("High Score  "+ highScorePlayer + " - "+ highScore+" / 10");
		
	}
}











public class Quiz_Game {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the name");
		String playerName = sc.next();
		QuizCategory science = new QuizCategory("Science",3);
		science.addQuestion(QuestionFactory.createMCQ(
			    "What planet is closest to the sun?",
			    "C",
			    new String[]{"A) Earth", "B) Venus", "C) Mercury", "D) Mars"}
			));
		science.addQuestion(QuestionFactory.createMCQ(
			    "What gas do plants absorb?",
			    "D",
			    new String[]{"A) Oxygen", "B) Nitrogen", "C) Hydrogen", "D) Carbon Dioxide"}
			));
		science.addQuestion(QuestionFactory.createTrueFalse(
			    "The Earth revolves around the Moon.", "False"
			));
		
		int score = 0;
		for(Question q:science.getQuestions()) {
			q.display();
			QuestionTimer timer = new QuestionTimer(15);
			Thread timerThread = new Thread(timer);
			timerThread.start();
			System.out.print("Your answer:  ");
			String answer = sc.next();
			timer.stopped = true;
			try {
				timerThread.join();
			} catch (InterruptedException e) {
				
			}
			if(timer.timeup) {
				 System.out.println("Too slow!"+q.correctAnswer);
				
				
			}
			else if(q.checkAnswer(answer)) {
				System.out.println("Correct");
				score++;
				
			}
			else {
				System.out.println(" Wrong "+q.correctAnswer);
			}
		}
		System.out.println("Your Score: "+ score+ " /3");
		QuizManager.getInstance().updateHighScore(playerName, score);
		ScoreFilehandler.saveScore(playerName, playerName, score);
		QuizManager.getInstance().showHighScore();
		

	}

}
