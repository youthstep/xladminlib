package com.xunlei.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ShellUtils {
	/**
	 * 执行一段linux脚本
	 * 
	 * @param cmd
	 * @param out
	 * @return
	 * @throws Exception
	 */
	public static final int executeShell(String cmd, OutputStream out)
			throws IOException, InterruptedException {
		if (cmd == null || cmd.trim().length() == 0)
			return 0;
		if (out != null) {
			out.write(cmd.getBytes());
			out.write("\r\n".getBytes());
		}
		Process process = Runtime.getRuntime().exec(cmd);
		StreamGobber error = new StreamGobber(process.getErrorStream(),
				"error", out);
		StreamGobber output = new StreamGobber(process.getInputStream(),
				"output", out);
		error.start();
		output.start();
		int exitValue = process.waitFor();
		return exitValue;
	}

	static class StreamGobber extends Thread {
		InputStream ins;

		String type;

		OutputStream out;

		StreamGobber(InputStream ins, String type) {
			this(ins, type, null);
		}

		StreamGobber(InputStream ins, String type, OutputStream redirect) {
			this.ins = ins;
			this.type = type;
			this.out = redirect;
		}

		public void run() {
			try {
				PrintWriter writer = null;
				if (out != null) {
					writer = new PrintWriter(out);
				}
				InputStreamReader isr = new InputStreamReader(ins);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null) {
					if (writer != null) {
						writer.println(line);
						writer.flush();
					}
					// System.out.println(type + ">" + line);
				}
				if (writer != null) {
					writer.flush();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
