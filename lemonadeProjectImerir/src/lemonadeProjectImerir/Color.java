package lemonadeProjectImerir;

public class Color {
	int r, g, b;
	
	public Color(){
		this.r=(int)(Math.random()*255);
		this.g=(int)(Math.random()*255);
		this.b=(int)(Math.random()*255);
	}
	
	public Color(int r, int g, int b){
		this.b=b;
		this.r=r;
		this.g=g;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}
}
