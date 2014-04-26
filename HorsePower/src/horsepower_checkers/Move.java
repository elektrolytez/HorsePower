package horsepower_checkers;

import java.util.ArrayList;
import java.util.List;

public class Move {

	private List<int[]> _actionList = new ArrayList<int[]>();
	private Boolean _player, _isActionAJump;
	private String[] _converterTool;
	
	public Move(Boolean player) {
		_player = player;
	}
	
	public Move(Move m) { //move cloner constructor
		this._actionList = m._actionList;
		this._player = m._player;
	}
	
	public void addAction(int from, int to, boolean isJump) {
		_isActionAJump = isJump;
		int[] newJ = {from, to};
		_actionList.add(newJ);
	}
	
	public String getMessage() {
		return this.convertMove();
	}
	public String convertMove() {
		this.makeConverterTool();
		String tempMsg = "";
		for (int[] act : _actionList) {
			int to = act[1];
			if (tempMsg.equals("")) { // tempMsg isn empty string so take both
										// the 'from' and 'to' components
				int from = act[0];
				tempMsg = _converterTool[from] + ":" + _converterTool[to];
			} else { // just take the 'to' component - to comply with server
						// move syntax
				tempMsg = tempMsg + ":" + _converterTool[to];
			}
		}
		
		return tempMsg;
	}

	public List<int[]> getJumpList() {
		return _actionList;
	}
	public Boolean isMoveAJump() {
		return _isActionAJump;
	}
	public Boolean player() {
		return _player;
	}
	public void removeLastAct() {
		_actionList.remove(_actionList.size()-1);
	}
	public int[] getFirstAct() {
		int[] firstAct = this._actionList.get(0);
		return firstAct;
	}
	public int[] getLastAct() {
		int[] lastAct = this._actionList.get(_actionList.size()-1);
		return lastAct;
	}
	
	/*
	 * super unelegant but what ever -- feel free to beautify if you'd like
	 */
	public void makeConverterTool() {
		this._converterTool = new String[36];
		
		_converterTool[1] = "(7:1)";
		_converterTool[2] = "(7:3)";
		_converterTool[3] = "(7:5)";
		_converterTool[4] = "(7:7)";
		_converterTool[5] = "(6:0)";
		_converterTool[6] = "(6:2)";
		_converterTool[7] = "(6:4)";
		_converterTool[8] = "(6:6)";
		
		_converterTool[10] = "(5:1)";
		_converterTool[11] = "(5:3)";
		_converterTool[12] = "(5:5)";
		_converterTool[13] = "(5:7)";
		_converterTool[14] = "(4:0)";
		_converterTool[15] = "(4:2)";
		_converterTool[16] = "(4:4)";
		_converterTool[17] = "(4:6)";
		
		_converterTool[19] = "(3:1)";
		_converterTool[20] = "(3:3)";
		_converterTool[21] = "(3:5)";
		_converterTool[22] = "(3:7)";
		_converterTool[23] = "(2:0)";
		_converterTool[24] = "(2:2)";
		_converterTool[25] = "(2:4)";
		_converterTool[26] = "(2:6)";
		
		_converterTool[28] = "(1:1)";
		_converterTool[29] = "(1:3)";
		_converterTool[30] = "(1:5)";
		_converterTool[31] = "(1:7)";
		_converterTool[32] = "(0:0)";
		_converterTool[33] = "(0:2)";
		_converterTool[34] = "(0:4)";
		_converterTool[35] = "(0:6)";
	}
}






