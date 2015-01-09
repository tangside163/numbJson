package org.tangsi.util.json;

public class JsonFormat {

	/**
	 * 缩进量
	 */
	private int indent;

	/**
	 * 缩进字符
	 */
	private String indentChar = "  ";

	public int getIndent() {
		return indent;
	}

	public void setIndent(int indent) {
		this.indent = indent;
	}

	public String getIndentChar() {
		return indentChar;
	}

	public void setIndentChar(String indentChar) {
		this.indentChar = indentChar;
	}
	
	/**
	 * 增加1个缩进
	 * @return
	 */
	public JsonFormat increaseIndent() {
		this.indent++;
		return this;
	}
	
	/**
	 * 减少1个缩进
	 * @return
	 */
	public JsonFormat decreaseIndent() {
		this.indent--;
		return this;
	}

}
