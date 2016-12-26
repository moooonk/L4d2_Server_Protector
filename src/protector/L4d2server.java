package protector;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.util.Calendar;

public class L4d2server extends Thread {
	public String serverpath;
	public String port;
	public String servername, map, player;
	public String commandline;
	public boolean live, islive, isprotect, isonline;
	Process server;
	public int timeout_max, timeout;
	DatagramSocket ds;
	public int restartcount;

	public int restart_hour;
	public int restart_min;

	public long lastrestarttime;

	public L4d2server(/*
					 * String path, int max, String inputport, boolean
					 * isprotect, String commandline
					 */) {
		// serverpath = path;
		live = false;
		islive = false;
		server = null;
		// port = inputport;
		servername = null;
		map = null;
		player = null;
		// timeout_max = max;
		timeout = 0;
		restartcount = 0;
		restart_hour = -1;
		restart_min = -1;
		lastrestarttime = 0;
		// this.isprotect = isprotect;
		// this.commandline = commandline;
	}

	String findpid(String port) {
		Process proc = null;
		int startpos = -1;
		try {
			proc = Runtime.getRuntime().exec("netstat -apn");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			String info = br.readLine();
			while (info != null) {
				if (info.indexOf("PID") >= 0) {
					if (startpos == -1) {
						startpos = info.indexOf("PID");
					} else {
						break;
					}
				}
				if (startpos != -1) {
					if (info.indexOf(":" + port) >= 0) {
						// System.out.println(info);
						for (int i = startpos; i < info.length(); i++) {
							if (info.charAt(i) == '/') {
								proc.destroy();
								return info.substring(startpos, i);
							}
						}
					}
				}
				info = br.readLine();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		proc.destroy();
		return null;

	}

	void killsrcds(String pid) {
		if (pid == null)
			return;
		Process proc = null;
		try {
			proc = Runtime.getRuntime().exec("kill -9 " + pid);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			String info = br.readLine();
			while (info != null) {

				// System.out.println(info);
				info = br.readLine();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		proc.destroy();
	}

	public void forcedstop() {
		live = false;
		killsrcds(findpid(port));
		if (server != null)
			server.destroy();
	}

	public void startserver() {
		try {
			String hand = "screen -AmdS " + port + " ";
			timeout = 0;
			while (findpid(port) != null) {// 端口占用检测
				killsrcds(findpid(port));
				// System.out.println("端口占用");
				sleep(3000);
			}
			server = Runtime.getRuntime().exec(
					hand + serverpath + " " + commandline, null,
					new File(new File(serverpath).getParent()));
			restartcount++;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void restartserver() {
		forcedstop();
		startserver();
	}

	public boolean checkautorestart() {
		if (restart_hour != -1 && restart_min != -1) {
			Calendar restarttime = Calendar.getInstance();
			restarttime.set(Calendar.HOUR_OF_DAY, restart_hour);
			restarttime.set(Calendar.MINUTE, restart_min);
			if (restarttime.getTime().getTime() <= System.currentTimeMillis()
					&& System.currentTimeMillis() - lastrestarttime > 1000 * 60 * 60 * 24) {
				lastrestarttime = System.currentTimeMillis();
				return true;
			}
		}
		return false;
	}

}
