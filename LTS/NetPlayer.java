/**
 * @name Last Tank Standing
 * @version 1.3
 * @class NetPlayer
 * @author
 * 		Jasper A. Sibayan
 * 		2009-46112
 * @author
 *		Wilbert G. Verayin
 *		2009-60315
 * @date October 17, 2012 1st Semester, AY 2012-2013
 * 		CMSC 137 B-5L
 *		
 *		University of the Philippines Los Banos
 * 		Institute of Computer Science
 */

import java.net.InetAddress;

public class NetPlayer {
	//Properties
	private InetAddress address;
	private int port;
	private String name;
	private int x, y, b1x, b1y, b2x, b2y, direction, hp, score, id;
	//Constructor
	public NetPlayer(String name, InetAddress address, int port, int id) {
		this.address = address;
		this.port = port;
		this.name = name;
		this.id = id;
		this.hp = 100;
		this.score = 0;
	}
	//Methods
	//SETTERS	
	public void setX(int num) {
		this.x = num;
	}
	public void setY(int num) {
		this.y = num;
	}
	public void setb1X(int num)	{
		this.b1x = num;
	}
	public void setb1Y(int num)	{
		this.b1y = num;
	}
	public void setb2X(int num)	{
		this.b2x = num;
	}
	public void setb2Y(int num)	{
		this.b2y = num;
	}
	public void setDirection(int direction)	{
		this.direction = direction;
	}
	public void setHP(int hp) {
		this.hp = hp;
	}
	public void setScore(int score)	{
		this.score = score;
	}	
	//GETTERS
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getb1X() {
		return b1x;
	}
	public int getb1Y() {
		return b1y;
	}
	public int getb2X() {
		return b2x;
	}
	public int getb2Y() {
		return b2y;
	}
	public int getDirection() {
		return direction;
	}
	public int getHP() {
		return hp;
	}
	public int getScore() {
		return score;
	}
	public int getID() {
		return id;
	}
	public int getPort() {
		return port;
	}
	public InetAddress getAddress() {
		return address;
	}
	public String getName() {
		return name;
	}
	public String toString() {
		String retval = "";
		retval+="PLAYER ";
		retval+=name+" ";
		retval+=x+" ";
		retval+=y+" ";
		retval+=b1x+" ";
		retval+=b1y+" ";
		retval+=b2x+" ";
		retval+=b2y+" ";
		retval+=direction+" ";
		retval+=hp+" ";
		retval+=score+" ";
		retval+=id+" ";
		return retval;
	}
}