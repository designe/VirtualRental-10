package video.rental.demo.domain;

class Reward {
	private int numberOfReward;
	private String[] message;
	private int[] rewardPoint;
	private int getNumberOfReward() {
		return numberOfReward;
	}
	public void setNumberOfReward(int numberOfReward) {
		this.numberOfReward = numberOfReward;
	}
	public String[] getMessage() {
		return message;
	}
	public void setMessage(String[] message) {
		this.message = message;
	}
	public int[] getRewardPoint() {
		return rewardPoint;
	}
	private void setRewardPoint(int[] rewardPoint) {
		this.rewardPoint = rewardPoint;
	}

	public Reward(int no, String[] msg, int[] r) {
		this.numberOfReward = no;
		this.message = msg;
		this.rewardPoint = r;
	}
	
}

public class RewardPolicy {
	
	private Reward r;
	RewardPolicy(Reward r) {
		this.r = r;
	}
	
		
	public void displayIfRewardIsAvailable(int point) 
	{
		int cnt = 0;
		for(int p : r.getRewardPoint()) {
			if (point > p)
				System.out.println(r.getMessage()[cnt]);
			cnt++;
		}
		return;
	}
}
