package app.entities;

public class Rating {
	
	private double score;
	
	private int numberOfRatings;
	
	/**
	 * @param score
	 * @param numberOfRatings
	 */
	public Rating(double score, int numberOfRatings) {
		super();
		this.score = score;
		this.numberOfRatings = numberOfRatings;
	}

	public Rating() {}

	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}

	/**
	 * @return the numberOfRatings
	 */
	public int getNumberOfRatings() {
		return numberOfRatings;
	}

	/**
	 * @param numberOfRatings the numberOfRatings to set
	 */
	public void setNumberOfRatings(int numberOfRatings) {
		this.numberOfRatings = numberOfRatings;
	}
	
	/**
	 * Update the score using this new review
	 * score = sum(ratings) / numberOfRatings
	 * 
	 * @param newScore number between 1 and 10
	 * @throws RatingOutOfBoundsException when newScore is not between 1 and 10
	 */
	public void addReview(int newScore) throws RatingOutOfBoundsException {
		if (score < 1 || score >10) {
			throw new RatingOutOfBoundsException("Score must be between 1 and 10");
		}
		double totalScore = score * numberOfRatings + newScore;
		numberOfRatings += 1;
		score = totalScore/numberOfRatings;
	}
}