package org.fc.edit;

public class OpenFileCommand {
	public String filename;
	public int reclen;
	public boolean readonly;
	public boolean littleEndian;
	public boolean varlen;
	
	public OpenFileCommand(String filename, int reclen, boolean readonly, boolean littleEndian, boolean varlen) {
		super();
		this.filename = filename;
		this.reclen = reclen;
		this.readonly = readonly;
		this.littleEndian = littleEndian;
		this.varlen = varlen;
	}
}