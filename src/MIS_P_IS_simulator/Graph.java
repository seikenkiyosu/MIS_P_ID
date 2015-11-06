package MIS_P_IS_simulator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Graph {
	//グラフを作る方法
	public static final int 
		GRID   = 0,
		TORUS  = 1,
		LINEAR = 2,
		RANDOM = 3;

	//メンバ
	public int n;
	public boolean List[][];			//グラフ
	
	public Graph (int n, int MethodToGenerate) {
		this.n = n;
		
		//リストのメモリ用意
		List = new boolean[n][];
		for (int i = 0; i < n; i++) { List[i] = new boolean[n]; }
		
		//グラフの作り方
		switch (MethodToGenerate) {
			case GRID  :	//Delta = 4
				for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) { List[i][j] = false; }
				for (int i = 0; i < n; i++){
					if (i % (int)Math.sqrt(n) != 0) List[(i-1+n)%n][i] = true;
					if (i % (int)Math.sqrt(n) != (int)Math.sqrt(n)-1) List[(i+1+n)%n][i] = true;
					if (i < (int)Math.sqrt(n)*((int)Math.sqrt(n)-1))List[(i+(int)Math.sqrt(n)+n)%n][i] = true;
					if (i >= (int)Math.sqrt(n)) List[(i-(int)Math.sqrt(n)+n)%n][i] = true;
				}
				break;
			case TORUS  :	//Delta = 4
				for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) { List[i][j] = false; }
				for (int i = 0; i < n; i++){
					if (i % (int)Math.sqrt(n) != 0) List[(i-1+n)%n][i] = true;
					else { List[i+(int)Math.sqrt(n)-1][i] = true; }
					if (i % (int)Math.sqrt(n) != (int)Math.sqrt(n)-1) List[(i+1+n)%n][i] = true;
					else { List[i-(int)Math.sqrt(n)+1][i] = true; }
					List[(i+(int)Math.sqrt(n)+n)%n][i] = true;
					List[(i-(int)Math.sqrt(n)+n)%n][i] = true;
				}
				break;
			case LINEAR :	//Delta = 2
				for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) { List[i][j] = false; }
				for (int i=0; i < n-1; i++) {
//				for (int i=0; i < n; i++) {
//					List[(i+1+n)%n][i] = true;	//リングを作りたいとき
//					List[(i-1+n)%n][i] = true;
					List[i][i+1] = true;
					List[i+1][i] = true;
				}
				break;
			case RANDOM :
				while (!IsConnected()) {
					Random R = new Random();
					for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) { List[i][j] = true; }
					for (int i = 0; i < n/2; i++) {
						while (true) {	//最大字数がDelta以下になるように制御
							int degree = 0;
							for (int j = i; j < n; j++) { 
								if (R.nextInt(100) > 90) {
									List[i][j] = false;
									List[j][i] = false;
								}
								else {
									degree++;
								}
							}
							if (degree <= MIS_P_IS_simulator.DELTA) { break; }	//適切なdegreeだったら次のノードを見る
						}
					} 
//					if (IsConnected()) {	//連結かどうか
//						System.out.println("connected");
//					}
//					else {
//						System.out.println("non-connected");
//					}
				}
				break;
		}
	}
	
	//グラフは連結かどうかを判定
	private boolean IsConnected () {
		boolean[] marked = new boolean[n];
		Queue<Integer> queue = new LinkedList<Integer>();
		boolean found;
		
		boolean iscorrectinit = false;
		for (int initagent = 0; initagent < n; initagent++) {
			for (int i = 0; i < n; i++) if (List[initagent][i]) iscorrectinit = true;
			if (iscorrectinit) break;
		}
		if (!iscorrectinit) return false;
		
		for (int target = 0; target < n; target++) {	//各ノードにがagent[0]から到達可能かどうかを調べる
			for (int i = 0; i < n; i++) { marked[i] = false; }
			found = false;
			while (!queue.isEmpty()) queue.poll();	//キューを空にする
			queue.add(0);
			while (!queue.isEmpty()) {
				int v = queue.poll();
				if (v==target) {
					found = true;
					break;
				}
				marked[v] = true;	//vを探索済みに
				for (int i = 0; i < n; i++) {
					if (List[v][i] && !marked[i]) {		//vの隣接ノードをキューに
						queue.add(i);
					}
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}
	
	public void MoveAction (int MoveModel) {
		
	}
	
	public void ShowGraph () {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				if (List[i][j]) { System.out.print(1 + " "); }
				else { System.out.print(0 + " "); }
			System.out.print ("\n");
		}
	}
}
