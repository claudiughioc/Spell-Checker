//Clasa Field ca fi un element din matricea din algoritm
//Va contine un word, distanta subsirului analizat fata de accesta si numarul de cubinte din care este format
public class Field {
	public Word word;
	public int dist = Integer.MAX_VALUE;
	public Field(){
		word = new Word();
	}
	public int nrw = 1; //Numaul de cuvinte pe care il are un camp
	public Field (Word word, int dist, int nrw){
		this.word = word;
		this.dist = dist;
		this.nrw = nrw;
	}
}
