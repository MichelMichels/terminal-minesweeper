import java.util.List;
import java.util.ArrayList;

public class MineList {
	private List<Mine> l;
	private char[][] field = new char[12][12];
	private boolean stop = false;
	
	public MineList(){
		l = new ArrayList<Mine>();
	}
	
	public int getSize(){
		return l.size();
	}
	
	public boolean getStop(){
		return stop;
	}
	
	public void removeAllMines(){
		int i = 0;
		int grootte = l.size();
		
		while(i < grootte){
			l.remove(0);
			i++;
		}
	}
	
	public void showAllBombs(){
		for(int i = 0; i < 10; i++){
			field[l.get(i).getX()][l.get(i).getY()] = '*'; //write where the bombs are
		}
	}
	
	public void addMines(Mine m){
		boolean duplicate = false;
		int i = 0;
		
		while(i < l.size()){
			if(m.getX() == l.get(i).getX() && m.getY() == l.get(i).getY()){ // no duplicates
				duplicate = true;
			}
			i++;
		}
		if(!duplicate){
			l.add(m);
		}
	}
	
	public void initializeField(){
		for(int i = 0; i < 11; i++){                        // field with X's
			for(int j = 0; j < 11; j++){
				field[i][j] = '.';
			}
		}
	}
	
	public void writeField(){
		System.out.println();
		
		for(int i = 0; i < 10; i++){
			field[0][i + 1] = (char) (48 + i);
		}
		for(int i = 0; i < 10; i++){
			field[i + 1][0] = (char)(65 + i);
		}
		
		field[0][0] = '/';
		
		
		/*for(int i = 0; i < 10; i++){
			field[l.get(i).getX()][l.get(i).getY()] = 'B'; //write where the bombs are
		}*/
		
		for(int i = 0; i < 11; i++){
			for(int j = 0; j < 11; j++){
				System.out.print(field[i][j] + " ");
			}
			System.out.println("");
		}
		
		System.out.println();
		
		
	}
	
	public void flag(int x, int y){
		field[x][y] = 'F';
	}
	
	public void tick(int x, int y) throws ArrayIndexOutOfBoundsException {
		stop = false;
		
		for(int i = 0; i < 10; i++){
			if(l.get(i).getX() == x && l.get(i).getY() == y){  // controleren als we geen bommen
				showAllBombs();
				writeField();							// geraakt hebben
				System.out.println();
				System.out.println("*** GAME OVER ***");
				System.out.println();
				stop = true; //boolean om niet meer verder te doen
			}
		}
		
		if(!stop){   // checken of er nog geen bom geraakt is door vorige code
			int bombs = 0;
			
			for(int i = 0; i < 10; i++){ // kijk rond de aangetikte plaats als er bommen zitten door te loopen door de mijnenlijst
				if(l.get(i).getX() == x - 1 && l.get(i).getY() == y - 1) bombs++;
				if(l.get(i).getX() == x - 1 && l.get(i).getY() == y    ) bombs++;
				if(l.get(i).getX() == x - 1 && l.get(i).getY() == y + 1) bombs++;
				if(l.get(i).getX() == x     && l.get(i).getY() == y - 1) bombs++;
				if(l.get(i).getX() == x     && l.get(i).getY() == y    ) bombs++;
				if(l.get(i).getX() == x     && l.get(i).getY() == y + 1) bombs++;
				if(l.get(i).getX() == x + 1 && l.get(i).getY() == y - 1) bombs++;
				if(l.get(i).getX() == x + 1 && l.get(i).getY() == y    ) bombs++;
				if(l.get(i).getX() == x + 1 && l.get(i).getY() == y + 1) bombs++;
			}
			
			if(bombs != 0){
				field[x][y] = (char)(bombs+48);
			} else if(field[x][y] == '.') {
				field[x][y] = 32;
				tick(x-1, y-1);
				tick(x-1, y);
				tick(x-1, y+1);
				tick(x,y-1);
				tick(x,y+1);
				tick(x+1,y-1);
				tick(x+1,y);
				tick(x+1, y+1);			
			}
		}
	
	}
	
	public int numberOfFlags(){
		int flags = 0;
		
		for(int x = 1; x < 12; x++){
			for(int y = 1; y < 12; y++){
				if(field[x][y] == 'F') flags++;
			}
		}
		
		return flags;
	}
	
	public boolean gewonnen(){
		boolean win = true;
		
		for(int x = 1; x < 12; x++){
			for(int y = 1; y < 12; y++){
				if(field[x][y] == '.' || numberOfFlags() != 10){
					win = false;
				}
			}
		}
				
		return win;
	}
}
