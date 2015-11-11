package Agent;

import MIS_P_IS_simulator.MIS_P_IS_simulator;
import java.util.Random;

public class Agent {
	//メンバ
	public int var;					//id
	public int timer_IS;			//独立ノードを増やすためのタイマ
	public InfoIndependentNode IIn;	//kホップ内で最もidがj低い独立ノードの情報を保持
	
	public Agent () {	//状態を初期化
		Random random = new Random();
		
		//id初期化(重複がないようにしてる)
		while (true) {
			this.var = random.nextInt(MIS_P_IS_simulator.IDMAX);
			if (!MIS_P_IS_simulator.idlist[this.var]) { MIS_P_IS_simulator.idlist[this.var] = true; break; }
		}
		
		//Inを増やすためのタイマを初期化
		this.timer_IS = random.nextInt(MIS_P_IS_simulator.t_max);
		
		//IIn初期化
//		this.IIn = new InfoIndependentNode();
//		this.IIn.var = this.var;
////		this.IIn.var = random.nextInt(MIS_P_IS_simulator.IDMAX);
//		this.IIn.hop = random.nextInt(MIS_P_IS_simulator.n);
//		this.IIn.timer_IIn = random.nextInt(MIS_P_IS_simulator.t_max);

		if (random.nextBoolean()) {
			this.IIn = new InfoIndependentNode();
//			this.IIn.var = this.var;
			this.IIn.var = random.nextInt(MIS_P_IS_simulator.IDMAX);
			this.IIn.hop = random.nextInt(MIS_P_IS_simulator.n);
			this.IIn.timer_IIn = random.nextInt(MIS_P_IS_simulator.t_max);
		}
		else {
			this.IIn = null;
		}
	}
	
	//メソッド
	public boolean IsIndependentNode () {		//自身が独立ノードであるかを表すメソッド
		if(this.IIn == null) return false;
		return this.var==this.IIn.var ? true : false;
	}
}
