package MIS_P_IS_simulator;

import Agent.Agent;
import Agent.InfoIndependentNode;

public class Interaction {
	public static void interaction(Agent x, Agent y){
		//lid更新プロセス
		if (x.IsIndependentNode()) {x.IIn.hop = 0; x.IIn.timer_IIn = MIS_P_IS_simulator.t_max;}
		if (y.IsIndependentNode()) {y.IIn.hop = 0; y.IIn.timer_IIn = MIS_P_IS_simulator.t_max;}
		//どちらかのIIn空のとき，もしくはidが等しく相手のホップ数+1の方が自分のホップ数よりも小さいとき，相手のIInに書き換える
		if ((x.IIn == null && y.IIn!=null) || ((x.IIn != null && y.IIn != null) && (x.IIn.var == y.IIn.var && x.IIn.hop > y.IIn.hop+1)) ) {	//xのIInが書き換えられる 
			x.IIn = new InfoIndependentNode();
			x.IIn.var = y.IIn.var;
			x.IIn.hop = y.IIn.hop + 1;
			x.IIn.timer_IIn = y.IIn.timer_IIn;
		}
		if ((y.IIn == null && x.IIn!=null) || ((y.IIn != null && x!= null) &&(y.IIn.var == x.IIn.var && y.IIn.hop > x.IIn.hop+1)) ) {	//yのIInが書き換えられる 
			y.IIn = new InfoIndependentNode();
			y.IIn.var = x.IIn.var;
			y.IIn.hop = x.IIn.hop + 1;
			y.IIn.timer_IIn = x.IIn.timer_IIn;
		}

			
		if (x.IIn != null && y.IIn !=null) {
			if (y.var == x.IIn.var && !y.IsIndependentNode()) {
					x.IIn.var = y.IIn.var;
					x.IIn.hop = y.IIn.hop+1;
				}
			if (x.var == y.IIn.var && !x.IsIndependentNode()) {
				y.IIn.var = x.IIn.var;
				y.IIn.hop = x.IIn.hop+1;
			}	
			if (x.IIn.var != y.IIn.var) {
				//既知の独立ノードのidが相手と異なるときで，hop数の和+1がk以下のとき，IIn.var比較で大きい方を小さい方のIInに書き換える
				if (x.IIn.hop + y.IIn.hop + 1 <= MIS_P_IS_simulator.k) {
					if (x.IIn.var > y.IIn.var) {
						x.IIn.var = y.IIn.var;
						x.IIn.hop = y.IIn.hop + 1;
						x.IIn.timer_IIn = y.IIn.timer_IIn;
					}
					else if (x.IIn.var < y.IIn.var) {
						y.IIn.var = x.IIn.var;
						y.IIn.hop = x.IIn.hop + 1;
						y.IIn.timer_IIn = x.IIn.timer_IIn;
					}
				}
				//既知の独立ノードのidが相手と異なるときで，hop数の和+1がk以下のとき，IIn.hop比較で大きい方を小さい方のIInに書き換える
				else {
					if (x.IIn.hop > y.IIn.hop+1) {
						x.IIn.var = y.IIn.var;
						x.IIn.hop = y.IIn.hop + 1;
						x.IIn.timer_IIn = y.IIn.timer_IIn;
					}
					else if (y.IIn.hop > x.IIn.hop+1) {
						y.IIn.var = x.IIn.var;
						y.IIn.hop = x.IIn.hop + 1;
						y.IIn.timer_IIn = x.IIn.timer_IIn;
					}
				}

			}
		
			//falseなInと嘘の独立ノード(削除された独立ノードの情報)を消す (timer_IInに関するプロセス)
			if (x.IIn.var == y.IIn.var && (x.IsIndependentNode()||y.IsIndependentNode())) {
				x.IIn.timer_IIn = y.IIn.timer_IIn =  MIS_P_IS_simulator.t_max; 
			}
			else if (x.IIn.var == y.IIn.var) {
				x.IIn.timer_IIn = y.IIn.timer_IIn = max(x.IIn.timer_IIn-1, y.IIn.timer_IIn-1, 0);
				if (x.IIn.hop == 0) { x.IIn = null; }
				if (y.IIn.hop == 0) { y.IIn = null; }
			}
			else if (x.IIn.var != y.IIn.var) { 
				x.IIn.timer_IIn = max(x.IIn.timer_IIn-1, 0); 
				y.IIn.timer_IIn = max(y.IIn.timer_IIn-1, 0);
			}	//IInのidが違うときは不安度が増していく
			
			
			//タイムアウトしたらIInの情報を忘れる
			if (x.IIn != null && x.IIn.timer_IIn == 0) {
				x.IIn = null;
			}
			if (y.IIn != null && y.IIn.timer_IIn == 0) {
				y.IIn = null;
			}
		}	//両個体がnullでないときの処理終了
		
		
		
		
		
		
		
		//Inを増やすためのプロセス
		//独立ノードが見当たらなければtimer_ISをデクリメント
		if (x.IIn == null || x.IIn.hop > MIS_P_IS_simulator.k) { 
			x.timer_IS = max(x.timer_IS-1, 0);
		} 
		else { 
			x.timer_IS = MIS_P_IS_simulator.t_max;
		}
		if (y.IIn == null || y.IIn.hop > MIS_P_IS_simulator.k) { 
			y.timer_IS = max(y.timer_IS-1, 0);
		} 
		else { 
			y.timer_IS = MIS_P_IS_simulator.t_max; 
		}
		//独立ノードにする
		if (x.timer_IS == 0) {
			if (x.IIn == null) x.IIn = new InfoIndependentNode();
			x.IIn.var = x.var;
			x.IIn.hop = 0;
			x.IIn.timer_IIn = MIS_P_IS_simulator.t_max;
		}
		if (y.timer_IS == 0) {
			if (y.IIn == null) y.IIn = new InfoIndependentNode();
			y.IIn.var = y.var;
			y.IIn.hop = 0;
			y.IIn.timer_IIn = MIS_P_IS_simulator.t_max;
		}
		if (x.IIn != null && y.IIn != null) {
			if (x.IsIndependentNode()) {x.IIn.hop = 0; x.IIn.timer_IIn = MIS_P_IS_simulator.t_max;}
			if (y.IsIndependentNode()) {y.IIn.hop = 0; y.IIn.timer_IIn = MIS_P_IS_simulator.t_max;}
		}
		
		
		
	}
	
	
	
	
	
	
	//交流に用いるメソッド
	private static int max (int a, int b) {
		return a >= b ? a : b;
	}
	private static int max (int a, int b, int c) {
		return max (a, b) > max(b, c) ? max(a, b) : max(b, c); 
	}
	private static int min (int a, int b) {
		return a < b ? a : b;
	}
}
