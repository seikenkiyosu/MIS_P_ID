package MIS_P_IS_simulator;

import java.util.Date;
import java.util.Random;

import Agent.Agent;

public class MIS_P_IS_simulator {
	public static final boolean LookData = false;

	//ネットワーク
	public static final int METHOD = Graph.GRID;
	
	//k-極大独立集合問題
	public static final int k = 7;
	
	//個体数
	public static final int n = 625;
	
	//タイマを決めるための定数
//	public static final int N = 900;		//個体数上限
	
	public static final int DELTA = 4;		//最大次数の上限
	public static final int t_max = 8*k*DELTA;
	
	public static final int IDMAX = n+100;		//IDの上限
	public static Boolean idlist[];
	
	public static void main (String args[]) {
		
		//初期化
		Graph graph = new Graph(n, METHOD);
		idlist = new Boolean[IDMAX];
		for (int i = 0; i < IDMAX; i++) idlist[i] = false;
		Agent[] agent = new Agent[n];
		for (int i = 0; i < n; i++) agent[i] = new Agent();

		
		//作業用変数
//		long CT = 0;
		
//		boolean CT_count_flag = true;
//		long HT = 0;
//		boolean HT_count_flag = false;
		
		boolean lastconfiguration[][] = new boolean[(int )Math.sqrt(n)][(int )Math.sqrt(n)];
		lastconfiguration = new boolean[(int )Math.sqrt(n)][];
		for (int i = 0; i < (int )Math.sqrt(n); i++) { lastconfiguration[i] = new boolean[(int )Math.sqrt(n)]; }
		
		
		while (true) {
//		for (int i = 0; i < 1000000; i++) {
			if (!LookData) {
				if (!LastOneIsSameConfiguration(lastconfiguration, agent)) {
					Date date = new Date();
					System.out.println("k = " + k + "\t" + date.toString());
					ShowConfiguration(agent);
				}
			}
			else {
				ShowConfiguration(agent);
			}
			
			if (IsOver(graph, agent)) {
				Date date = new Date();
				System.out.println("k = " + k + "\t" + date.toString());
				ShowConfiguration(agent);
				break;
			}
			
			//initiatorとresponderを決める
			Random R = new Random();
			int initiator = -1, responder = -1;
			initiator = R.nextInt(n);
			while (true) { 
				responder = R.nextInt(n);
				if (graph.List[initiator][responder]) break;
			}
			//交流
			Interaction.interaction(agent[initiator], agent[responder]);
		}
	}
	
//	private static boolean AlgorithmIsOver (Agent agent[]) {
//		//作業用変数
//		boolean[] marked = new boolean[n];
//		Queue<Integer> queue = new LinkedList<Integer>();
//		boolean found;
//		
//		//実処理
//		for (int target = 0; target < n; target++) {	//各独立ノードが別の独立ノードにk-hop以内に到達可能かをを調べる
//			for (int i = 0; i < n; i++) { marked[i] = false; }
//			found = false;
//			while (!queue.isEmpty()) queue.poll();	//キューを空にする
//			queue.add(0);
//			while (!queue.isEmpty()) {
//				int v = queue.poll();
//				if (v==target) {
//					found = true;
//					break;
//				}
//				marked[v] = true;	//vを探索済みに
//				for (int i = 0; i < n; i++) {
//					if (List[v][i] && !marked[i]) {		//vの隣接ノードをキューに
//						queue.add(i);
//					}
//				}
//			}
//			if (!found) {
//				return false;
//			}
//		}
//		return true;
//	}
	
	private static void ShowConfiguration (Agent agent[]) {
		for (int i = 0; i < (int )Math.sqrt(n); i++) {
			if (!LookData) {
				for (int j = 0; j < (int )Math.sqrt(n); j++) {
//					if (agent[i*(int )Math.sqrt(n)+j].IsIndependentNode()) System.out.print("🔶");
//					else System.out.print("◻️");
					if (agent[i*(int )Math.sqrt(n)+j].IsIndependentNode()) System.out.print("🔶\t");
					else {
						if (agent[i*(int )Math.sqrt(n)+j].IIn != null) System.out.printf("%2d\t", agent[i*(int )Math.sqrt(n)+j].IIn.hop);
					}
	//				if (j!=(int )Math.sqrt(n)-1){ System.out.print("\tー\t"); }
				}
			}
			else {
				for (int j = 0; j < (int )Math.sqrt(n); j++) {
					if (agent[i*(int )Math.sqrt(n)+j].IIn != null) {
//						System.out.printf("%2d %2d %2d %4d", agent[i*(int )Math.sqrt(n)+j].var, agent[i*(int )Math.sqrt(n)+j].IIn.var, agent[i*(int )Math.sqrt(n)+j].IIn.hop, agent[i*(int )Math.sqrt(n)+j].IIn.timer_IIn);
						System.out.printf("%2d %2d %4d", agent[i*(int )Math.sqrt(n)+j].IIn.var, agent[i*(int )Math.sqrt(n)+j].IIn.hop, agent[i*(int )Math.sqrt(n)+j].IIn.timer_IIn);
					}
					if (agent[i*(int )Math.sqrt(n)+j].IsIndependentNode()) System.out.print(" 🔶　\t");
					else System.out.print(" ◻️　\t");
				}
			}
			System.out.print("\n");
//			if(i!=(int )Math.sqrt(n)-1) { for (int j = 0; j < (int )Math.sqrt(n); j++) System.out.print("｜\t\t"); System.out.print("\n");}
		}
		for (int i = 0; i < (int )Math.sqrt(n); i++) {
			System.out.print("--");
		}
		System.out.print("\n");
	}
	
	private static boolean LastOneIsSameConfiguration (boolean lastone[][], Agent agent[]) {
		boolean lastoneissame = true;
		//前の状況から変化があったかどうか
		for (int i = 0; i < (int )Math.sqrt(n); i++) 
			for (int j = 0; j < (int )Math.sqrt(n); j++) 
				if (lastone[i][j] != agent[i*(int )Math.sqrt(n)+j].IsIndependentNode()) {
					lastoneissame = false;			
				}
		//last one 更新
		for (int i = 0; i < (int )Math.sqrt(n); i++)
			for (int j = 0; j < (int )Math.sqrt(n); j++) {
				lastone[i][j] = agent[i*(int )Math.sqrt(n)+j].IsIndependentNode();
			}
		return lastoneissame ? true : false;
	}
	
	private static boolean IsOver (Graph graph, Agent agent[]) {
		for (int i = 0; i < n; i++) {
			boolean flag = false;
			if (agent[i].IIn == null) return false;
			if (agent[i].IIn.hop > k) return false;
			if (agent[i].IsIndependentNode()) continue;
			for (int j = 0; j < n; j++) {
				if (graph.List[i][j] && agent[j].IIn != null) {		//枝があれば
					if (agent[j].IIn.var == agent[i].IIn.var && agent[j].IIn.hop == agent[i].IIn.hop-1) {
						flag = true;
						break;
					}
				}
			}
			if (!flag) return false;
		}
		return true;
	}
}
