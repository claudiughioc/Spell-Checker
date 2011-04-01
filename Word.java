//Clasa descrie un cuvant (sau un subsir) contine un String (body) si frecventa acestuia in dictionar
public class Word {
	public String body = "";
	public int frec = Integer.MIN_VALUE;
	public Word(){};
	public Word(String body, int frec){
		this.body = body;
		this.frec = frec;
	}
}
