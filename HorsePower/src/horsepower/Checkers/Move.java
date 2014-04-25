package horsepower.Checkers;

import java.util.ArrayList;
import java.util.List;

public class Move {

	private int _from, _to;
	private int _stepTaken;
	private Boolean _isJump, _player;
	private List<int[]> _jumpList = new ArrayList<int[]>();
	
	private String[] _converterTool;
	
	public Move(int from, int to, Boolean player) {
		_player = player;
		_from = from;
		_to = to;
		_stepTaken = (_to - _from);
		_isJump = false;
	}
	
	public void addJump(int from, int to) {
		int[] newJ = {from, to};
		_jumpList.add(newJ);
	}
	
	public String getMessage() {
		return this.convertMove();
	}
	public String convertMove() {
		this.makeConverterTool();
		String tempMsg = "";
		if (this.isJump()) {
			for (int[] jump : _jumpList) {
				int to = jump[1];
				if (tempMsg.equals("")) { //tempMsg isn't empty string so take both the 'from' and 'to' components
					int from = jump[0];
					tempMsg = _converterTool[from]+":"+_converterTool[to];
				} else { //just take the 'to' component of next jump
					tempMsg = tempMsg + ":"+_converterTool[to];
				}
			}
			return tempMsg;
		} else {
			tempMsg = _converterTool[_from]+":"+_converterTool[_to];
			return tempMsg;
		}
	}
	
	
	public Boolean isJump() {
		return _isJump;
	}
	public void makeJump(Boolean t) {
		int[] newJump = new int[2];
		newJump[0] = _from;
		newJump[1] = _to;
		_jumpList.add(newJump);
		_isJump = t;
	}
	public List<int[]> getJumpList() {
		return _jumpList;
	}
	public Boolean player() {
		return _player;
	}
	public int from() {
		return _from;
	}
	public int to() {
		return _to;
	}
	
	/*
	 * set the step value that was used to evaluate the before and after positions of the 
	 * result of this move
	 */
	public void updateStep(int s) {
		_stepTaken = s;
	}
	public int getStepTaken() {
		return _stepTaken;
	}
	
	
	
	/*
	 * super unelegant but what ever -- feel free beautify if you'd like
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






