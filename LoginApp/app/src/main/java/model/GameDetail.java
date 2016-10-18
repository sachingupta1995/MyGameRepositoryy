package model;


/**
 * The Class GameDetail.
 */
public class GameDetail {

	/** The quiz. */
	private Quiz quiz;
	
	/** The user. */
	private User user;
	
	/** The question. */
	private Question question;
	
	/** The answer. */
	private Option answer;
	
	/** The time taken to answer. */
	private int timeTakenToAnswer;
	
	/** The score. */
	private int score;
	
	/**
	 * Instantiates a new game detail.
	 *
	 * @param quiz the quiz
	 * @param user the user
	 * @param question the question
	 * @param answer the answer
	 * @param timeTakenToAnswer the time taken to answer
	 * @param score the score
	 */
	public GameDetail(Quiz quiz, User user, Question question, Option answer, int timeTakenToAnswer, int score) {
		super();
		this.quiz = quiz;
		this.user = user;
		this.question = question;
		this.answer = answer;
		this.timeTakenToAnswer = timeTakenToAnswer;
		this.score = score;
	}
	
	/**
	 * Instantiates a new game detail.
	 */
	public GameDetail() {
		super();
	}
	
	/**
	 * Gets the quiz.
	 *
	 * @return the quiz
	 */
	public Quiz getQuiz() {
		return quiz;
	}
	
	/**
	 * Sets the quiz.
	 *
	 * @param quiz the new quiz
	 */
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}
	
	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Gets the question.
	 *
	 * @return the question
	 */
	public Question getQuestion() {
		return question;
	}
	
	/**
	 * Sets the question.
	 *
	 * @param question the new question
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}
	
	/**
	 * Gets the answer.
	 *
	 * @return the answer
	 */
	public Option getAnswer() {
		return answer;
	}
	
	/**
	 * Sets the answer.
	 *
	 * @param answer the new answer
	 */
	public void setAnswer(Option answer) {
		this.answer = answer;
	}
	
	/**
	 * Gets the time taken to answer.
	 *
	 * @return the time taken to answer
	 */
	public int getTimeTakenToAnswer() {
		return timeTakenToAnswer;
	}
	
	/**
	 * Sets the time taken to answer.
	 *
	 * @param timeTakenToAnswer the new time taken to answer
	 */
	public void setTimeTakenToAnswer(int timeTakenToAnswer) {
		this.timeTakenToAnswer = timeTakenToAnswer;
	}
	
	/**
	 * Gets the score.
	 *
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Sets the score.
	 *
	 * @param score the new score
	 */
	public void setScore(int score) {
		this.score = score;
	}
	
	
}
