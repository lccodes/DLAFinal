package lcamery.engine;

import java.util.Random;

public class Noise {
	double[][] noise;
	
	public Noise(int size, int seed) {
		Random r = new Random(seed);
		noise = new double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				noise[i][j] = r.nextDouble();
			}
		}
	}
	
	public double smooth(double x, double y) {
		double fracX = x - (int) x;
		double fracY = y - (int) y;
		
		int x1 = (((int) x) + noise.length) % noise.length;
		int y1 = (((int) y) + noise.length) % noise.length;
		int x2 = (x1 + noise.length - 1) % noise.length;
		int y2 = (y1 + noise.length - 1) % noise.length;
		
		double toReturn = fracX * fracY * noise[x1][y1];
		toReturn += fracX * (1 - fracY) * noise[x1][y2];
		toReturn += (1 - fracX) * fracY * noise[x2][y1];
		toReturn += (1 - fracX) * (1 - fracY) * noise[x2][y2];
		
		return toReturn;
	}
	
	public double perturb(double x, double y, double size) {
		double val = 0;
		double savedSize = size;
		
		while (size >= 1) {
			val += smooth(x / size, y / size) * size;
			size /= 2; 
		}
		
		return val/savedSize;
	}
	
	public double[][] makeGrid(int size, int fuzz) {
		double[][] out = new double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				out[i][j] = perturb(i, j, fuzz);
			}
		}
		
		return out;
	}

}
