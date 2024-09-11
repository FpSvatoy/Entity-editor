package model;

public class IsometricCoordsConverter {
	
	/**
	 * @param result - Point object to store the result
	 * @return x and y converted to isometic coords
	 * */
	public static Point cartesianToIsometric(float cartX, float cartY, Point result) {
		result.x = cartX - cartY;
		result.y = (cartX + cartY) / 2;
		return result;
	}
	
	/**
	 * @param result - Point object to store the result
	 * @return x and y converted to cartesian coords
	 * */
	public static Point isometricToCartesian(float x, float y, Point result) {
		result.x = (2 * y + x) / 2;
		result.y = (2 * y - x) / 2;
		return result;
	}	
	
}
