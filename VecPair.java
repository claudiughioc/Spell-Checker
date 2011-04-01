//Clasa VecPair contine o pereche de vectori
//Primul va fi ultima linie din matricea care ajuta la gasirea abaterii
//A doua va reprezenta vectorul de inceput pentru calculul matricei urmatorului cuvant din dictionar
public class VecPair {
	public int [] result;
	public int [] vec2;
	public VecPair(int n, int m){
		result = new int[n];
		vec2 = new int[m];
	}
	public VecPair(){};
}
