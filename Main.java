import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;


public class Main {
	
	// Metoda intoarce o pereche de doi vectori: rezultatul si vectorul de incepere pentru urmatoarea comparatie
	public static VecPair distance (String first, String second, int init, int[] vec, int urm){
		int n = first.length();
		int m = second.length();
		VecPair pair = new VecPair(n+1, n+1);				//Initializez o pereche de doi vectori de lungime n+1
		if (urm< init){										//Daca prima litera a doua cuvinte nu este comuna, calculez matricea de la inceput
			init = -1;
		}
		int [][] mat = new int[m+1][n+1];					//Initializez matricea
		for (int j = 0; j<n+1; j++){
			mat[0][j] = j;
		}
		for (int i = 0; i<m+1; i++){
			mat[i][0] = i;
		}
		if (init>=0){
			mat[init+1] = vec;								//Matricea de pe linia coresppunzatoare cu init este vectorul anterior
		}
		for (int i = init+2; i<m+1; i++){
			for (int j = 1; j<n+1; j++){			
				if (first.charAt(j-1) == second.charAt(i-1)){
					mat[i][j] = mat[i-1][j-1];
				}
				else mat[i][j] = 1+ Math.min(Math.min(mat[i-1][j], mat[i-1][j-1]), mat[i][j-1]);
			}
		}
		pair.result = mat[m];								//Rezulatul este ultima linie a matricei
		pair.vec2 = mat[urm+1];								//Rezultat auxiliar, vectorul de inceput pentru urmatorul cuvant in ordine lexicografica
		return pair;
	}
	//Intoarce pozitia (de la 0) pe care stringurile sunt ultima oare egale
	public static int MyCompare(String s1, String s2){
		int i;
		for (i = 0; i< Math.min(s1.length(),s2.length()); i++ ){
			if (s1.charAt(i) != s2.charAt(i)){
				break;
			}
		}
		return (i-1);
	}
	
	//Metoda Intoarce un vector de Field-uri care contin distantele subsirurilor Stringului word, fata de cel mai apropiat cuvant din dictionar
	//Pe ultima pozitie din vector se afla distanta si cuvantul cel mai apropiat pentru sirul dat ca parametru
	public static Field [] MinDistance (ArrayList<Word> dic, String word){
		int n = word.length();
		Field [] vecmin = new Field [n+1];					//Initializez vectorul de Field-uri
		for (int i =0; i<n+1; i++){
			vecmin[i] = new Field();
		}
		
		Word w1 = dic.get(0);								//cuvantul curent
		Word w2 = null;										//cuvantul urmator
		int pos = 0;
		int init = -1;
		int [] vec = new int[n+1];							//Vectorul intermediar pentru pozitiile urmatoare
		for (int i = 0; i< n+1; i++){						//initializare vector de referinta
			vec[i] = 0;
		}
		VecPair pair = new VecPair(n+1, n+1);				//Creez o pereche de doi vectori de dimensiune n+1
		for (int i = 1; i< dic.size(); i++){
			w2 = dic.get(i);
			pos = MyCompare(w1.body, w2.body);
			pair = distance( word, w1.body,init, vec ,pos);	//Am obtinut doi int[] in pair
			for (int j=0;j<n+1;j++){						//Parcurg vectorul rezultat
				if (pair.result[j] < vecmin[j].dist){		//Calculez valoarea minima pentru fiecare pozitie
					vecmin[j].dist = pair.result[j];
					vecmin[j].word = w1;					//... si cuvantul corespunzator valorii minime
				}
				if (pair.result[j] == vecmin[j].dist){		//Daca sunt egale le aleg dupa frecventa
					if (w1.frec > vecmin[j].word.frec){
						vecmin[j].word = w1;
					}
				}
			}
			w1 = w2;
			vec = pair.vec2;
			init = pos;
			
		}
		return vecmin;
	}
	public static Field compose(Field a, Field b){ 			//Aduna valorile din doua campuri
		Field c = new Field();
		c.dist = a.dist + b.dist;							//Distantele fata de cuvant se aduna
		c.nrw = a.nrw + b.nrw;								//Numarul de cuvinte se aduna
		c.word.body = a.word.body+ " "+ b.word.body;		//Stringurile se concateneaza, cu un spatiu intre ele
		c.word.frec = a.word.frec + b.word.frec;			//Frecventele se aduna
		return c;
	}
	public static void main (String [] args){
		BufferedReader in = null;
		String s;
		ArrayList<Word> dic = new ArrayList<Word>();		//Vector de cuvinte din dictionar
		try{
			in = new BufferedReader(new FileReader ("dict.txt"));			//Citesc din dictionar
			s =  in.readLine();
			while (s != null){
				StringTokenizer t = new StringTokenizer(s, " ");			//Elimin spatiile din rand
				String body = t.nextToken();
				int frec = Integer.parseInt(t.nextToken());
				Word w = new Word (body, frec);								//Creez un cuvant pentru fiecare linie
				dic.add(w);													//Si il adaug in dictionar
				s = in.readLine();
			}
		}
		catch (Exception e){
			System.out.println("Eroare la citire din fisier");
		}
		Scanner sc = new Scanner(System.in);								//Citesc sirul de la standard input
		String intrare = sc.nextLine();
		String sir = "";
		StringTokenizer t = new StringTokenizer(intrare, " ");				//Elimin spatiile
		while (t.hasMoreTokens()){											//Creez sirul fara spatii
			sir += t.nextToken();
		}
		int n = sir.length();
		Field[][] mat = new Field [n+1][n+1];								//Creez matricea principala de Field-uri
		Field[] vecmin;
		for (int i=0; i<n+1; i++){
			for (int j =0; j<n+1; j++){
				mat[i][j] = new Field();
			}
		}
		for (int i = 0; i<n; i++){											//Creez sufixe incepand cu tot cuvantul si terminand cu ultima litera
			String sub = sir.substring(i);
			vecmin = MinDistance(dic, sub);									//Calculez vectorul resultat pentru fiecare sufix
			for (int j = sir.length() - sub.length(); j<n+1; j++ ){			//Le adaug in matrice deasupra diagonalei principale
				mat[i][j] = vecmin[j-sir.length() + sub.length()];
			}
		}																	//Pe diagonala principala voi avea abaterea fiecare litere din sir
		int k =2;															//Incep sa analizez portiuni din sir de lungime de 2 litere
		while (k<= n){														//Completez fiecare diagonala deasupra celei principale
			for (int i =0; i<n-k+1;i++){
				int j = i+k;
				Field result = mat[i][j];//Frecventa maxima de pana acum
				for (int q =i+1; q< j; q++){
					Field c = compose(mat[i][q], mat[q][j]);
					if (c.dist < result.dist){														//Daca distanta este mai mica
						result = c;
					}
					if (c.dist == result.dist){
						if (c.nrw < result.nrw){
							result = c;
						}
						if (c.nrw == result.nrw){
							if (c.word.frec > result.word.frec){
								result = c;
							}
							if (c.word.frec == result.word.frec){
								int com = c.word.body.compareTo(result.word.body);
								if (com < 0){
									result = c;
								}
							}
						}
					}
				}
				mat[i][j] = result;
			
			}
			k++;
		}
		System.out.println(mat[0][n].word.body);

	}
}

