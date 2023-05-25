package hex;

public class Move implements Comparable<Object> {
    public int row;
    public int column;
    private int value;

    public Move(int row, int column, int value) {
        this.row = row;
        this.column = column;
        this.value = value;
    }

    public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int compareTo(Object other) {
        return this.value - ((Move) other).value;
    }
}
