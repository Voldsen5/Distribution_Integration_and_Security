package Game2022.Extra;

public class Player {
	private String name = "";
	private int points;
	private int id;

	private Pair location = new Pair(0,0);
	private Direction direction = Direction.Up;

	public Player(int id, String name) {
		this.name = name;
		this.points = 0;
		this.id = id;
	};

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Pair getLocation() {
		return this.location;
	}
	public void setLocation(Pair p) {
		this.location=p;
	}

	public int getXPos() {
		return location.x;
	}
	public void setXPos(int xPos) {
		this.location.x = xPos;
	}

	public int getYPos() {
		return location.y;
	}
	public void setYPos(int yPos) {
		this.location.y = yPos;
	}

	public Direction getDirection() {
		return direction;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void addPoints(int p) {
		points +=p;
	}
	public int getPoints() {
		return points;
	}

	public String toString() {
		return String.format("%s#%02d:%dp",name,id, points);
	}

	public String introduction() {
		return id+" "+name;
	}
}