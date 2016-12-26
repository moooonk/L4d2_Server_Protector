package protector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;

public class Serverlist extends Thread {
	static public Serverlist serverlist = null;
	String adminname;
	String adminpassword;
	static Object lock = new Object(); // 同步锁
	static ArrayList<String> commandlist;
	static ArrayList<String> server_path;// 存每个模式的服务器文件路径
	static public ArrayList<L4d2server> server_srcds;// 从服务器srcds线程
	static int time_out_wait_max;
	public boolean islogin;
	String configpath = "/root/java/config.txt";
	// String configpath = "D:\\Workspaces\\L4d2_Server_Protector\\config.txt";
	String ip;

	private Serverlist() {
		commandlist = new ArrayList<String>();
		server_path = new ArrayList<String>();
		server_srcds = new ArrayList<L4d2server>();
		commandlist.add("[base]");// 0
		commandlist.add("[server]");// 1
		commandlist.add("[/base]");// 2
		commandlist.add("[/server]");// 3
		commandlist.add("adminname");// 4
		commandlist.add("adminpassword");// 5
		commandlist.add("timeout");// 6
		commandlist.add("path");// 7
		commandlist.add("protect");// 8
		commandlist.add("commandline");// 9
		commandlist.add("ip");// 10
		commandlist.add("autorestart");// 11
		time_out_wait_max = 30;
		islogin = false;
		getconfig();
		System.out.println("初始化");
		// System.out.println(adminname);
		// System.out.println(adminpassword);
		// System.out.println(time_out_wait_max);
		// System.out.println(server_srcds.get(0).servername);
		// System.out.println(server_srcds.get(0).commandline);
		// System.out.println(server_srcds.get(0).port);
		// System.out.println(server_srcds.get(0).isprotect);
		start();
	}

	static public void init() {
		if (serverlist == null) {
			serverlist = new Serverlist();
		}
	}

	public void run() {
		while (true) {
			protectserver();
			autorestart();
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void getserverstatus(String ip, L4d2server server) {
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket();
			ds.setSoTimeout(300);
			// System.out.println(ip);
			DatagramPacket sdp = null; // 发送用的UDP数据包
			DatagramPacket rdp = null; // 接收用的UDP数据包
			byte[] rbuf = new byte[1024]; // 接收缓冲区大小设置为1024
			byte[] sbuf = { (byte) 255, (byte) 255, (byte) 255, (byte) 255, 84,
					83, 111, 117, 114, 99, 101, 32, 69, 110, 103, 105, 110,
					101, 32, 81, 117, 101, 114, 121, 0 };
			sdp = new DatagramPacket(sbuf, sbuf.length,
					InetAddress.getByName(ip), Integer.parseInt(server.port));
			ds.send(sdp);
			rdp = new DatagramPacket(rbuf, 1024); // 生成一个接收用的数据包

			ds.receive(rdp); // 等待并读取服务器的响应
			// SocketAddress reip=rdp.getSocketAddress();
			SocketAddress reip = new InetSocketAddress(ip,
					Integer.parseInt(server.port));
			if (rdp.getSocketAddress().equals(reip)) {
				server.timeout = 0;
				fenxi(rdp, server);
			}
			ds.close();

		} catch (Exception e) {
			// System.out.println("无响应");
			server.timeout++;
			server.isonline = false;
			if (ds != null)
				ds.close();
			// e.printStackTrace();
			// System.out.println("无响应");

		} // 生成一个客户机用的UDP Socket
	}

	private void fenxi(DatagramPacket data, L4d2server server) throws Exception {
		int posa = data.getOffset() + 6, posb = data.getOffset() + 6, num = 0;
		if (data.getData()[posa] == -17)
			posa = data.getOffset() + 9;
		for (int i = data.getOffset() + 6; i < data.getLength(); i++) {
			if (data.getData()[i] == 0 && num == 0) {
				posb = i;
				server.servername = new String(data.getData(), posa, posb
						- posa, "UTF-8");
				num = 1;
				posa = posb;
			} else if (data.getData()[i] == 0 && num == 1) {
				posa++;
				posb = i;
				server.map = new String(data.getData(), posa, posb - posa,
						"UTF-8");
				num = 2;
				posa = posb;
			} else if (data.getData()[i] == 0 && data.getData()[i + 2] == 0x77
					&& data.getData()[i + 3] == 0) {
				posa = i - 2;
				char a[];
				int k[];
				k = new int[2];
				a = new char[2];
				a[0] = (char) data.getData()[posa];
				k[0] = a[0];
				a[1] = (char) data.getData()[posa + 1];
				k[1] = a[1];
				server.player = String.valueOf(k[0]) + "/"
						+ String.valueOf(k[1]);

				break;
			}
		}
		server.isonline = true;

	}

	private void getconfig() { // 解析设置文件，读取参数内容
		if (new File(configpath).exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(new File(configpath));
				InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
				BufferedReader br = new BufferedReader(isr);

				int i = 0;
				String str;
				int type = -1;
				while ((str = br.readLine()) != null) {
					for (i = 0; i < commandlist.size(); i++) {
						if (str.indexOf(commandlist.get(i)) == 0) {
							break;
						}
					}
					switch (i) {
					case 0:
						type = 0;
						break;
					case 1:
						type = 1;
						break;
					case 2:
						type = -1;
						break;
					case 3:
						type = -1;
						break;

					default:
						break;
					}

					if (type == 0) {
						if (i == 4) {
							adminname = str.substring(str.indexOf("=") + 1);
						} else if (i == 5) {
							adminpassword = str.substring(str.indexOf("=") + 1);
						} else if (i == 6) {
							time_out_wait_max = Integer.valueOf(str
									.substring(str.indexOf("=") + 1));
						} else if (i == 10) {
							ip = str.substring(str.indexOf("=") + 1);
						}
					} else if (type == 1) {
						server_srcds.add(new L4d2server());
						type = 2;
					} else if (type == 2) {
						if (i == 7) {
							server_srcds.get(server_srcds.size() - 1).serverpath = str
									.substring(str.indexOf("=") + 1);
						} else if (i == 8) {
							if (Integer
									.valueOf(str.substring(str.indexOf("=") + 1)) == 1) {
								server_srcds.get(server_srcds.size() - 1).isprotect = true;
							} else {
								server_srcds.get(server_srcds.size() - 1).isprotect = false;
							}
						} else if (i == 9) {
							server_srcds.get(server_srcds.size() - 1).commandline = str
									.substring(str.indexOf("=") + 1);
							server_srcds.get(server_srcds.size() - 1).port = str
									.substring(
											str.indexOf("-port") + 6,
											str.indexOf(" ",
													str.indexOf("-port") + 6));
						} else if (i == 11) {
							String[] times = str
									.substring(str.indexOf("=") + 1).split(":");
							if (times.length == 2) {
								server_srcds.get(server_srcds.size() - 1).restart_hour = Integer
										.valueOf(times[0]);
								server_srcds.get(server_srcds.size() - 1).restart_min = Integer
										.valueOf(times[1]);
							}
						}
					}

				}
				fis.close();
				br.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			System.out.println("配置文件读取");
		} else {
			System.out.println("配置文件读取失败");
		}
	}

	public boolean login(String name, String password) {
		if (adminname.equals(name) && adminpassword.equals(password)) {
			return true;
		} else {
			return false;
		}
	}

	private void protectserver() {
		synchronized (server_srcds) {
			for (int i = 0; i < server_srcds.size(); i++) {
				if (server_srcds.get(i).isprotect) {
					getserverstatus(ip, server_srcds.get(i));
				}
				// System.out.println(ip.getHostAddress());
			}

			for (int i = 0; i < server_srcds.size(); i++) {
				if (server_srcds.get(i).timeout > time_out_wait_max) {
					// System.out.println("重启");
					if (server_srcds.get(i).server != null)
						server_srcds.get(i).server.destroy();
					server_srcds.get(i).killsrcds(
							server_srcds.get(i).findpid(
									server_srcds.get(i).port));
					server_srcds.get(i).startserver();
				}
			}
		}
	}

	public boolean addnewserver(String path, String commandline) {
		if (!new File(path).exists()) {
			return false;
		}
		String port = commandline.substring(commandline.indexOf("-port") + 6,
				commandline.indexOf(" ", commandline.indexOf("-port") + 6));
		for (int i = 0; i < server_srcds.size(); i++) {
			if (port.equals(server_srcds.get(i))) {
				return false;
			}
		}
		return true;

	}

	public void writeprotect0(String port) {
		String s = null;
		boolean isok = false;
		File file = new File(configpath);
		ArrayList<String> array = new ArrayList<String>();
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileInputStream fis;
			fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			while ((s = br.readLine()) != null) {
				array.add(s);
			}
			fis.close();
			br.close();

			for (int i = 0; i < array.size(); i++) {
				if (array.get(i).startsWith("[server]")) {
					int protectpos = -1;
					while (true) {
						i++;
						if (array.get(i).startsWith("protect")) {
							protectpos = i;
						}
						if (array.get(i).startsWith("commandline")) {
							if (array
									.get(i)
									.substring(
											array.get(i).indexOf("-port") + 6,
											array.get(i).indexOf(
													" ",
													(array.get(i).indexOf(
															"-port") + 6)))
									.equals(port)) {
								array.set(protectpos, "protect=0");
							} else {
								protectpos = -1;
							}

						}

						if (array.get(i).startsWith("[/server]")) {
							break;
						}
					}
				}
			}

			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			for (int i = 0; i < array.size(); i++) {
				osw.write(array.get(i));
				osw.write("\r\n");
			}
			osw.flush();
			fos.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	public void stopprotect(String port) {
		for (int i = 0; i < server_srcds.size(); i++) {
			if (server_srcds.get(i).port.equals(port)) {
				server_srcds.get(i).isprotect = false;
				break;
			}
		}
		writeprotect0(port);
	}

	private void writeprotect1(String port) {
		String s = null;
		boolean isok = false;
		File file = new File(configpath);
		ArrayList<String> array = new ArrayList<String>();
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileInputStream fis;
			fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			while ((s = br.readLine()) != null) {
				array.add(s);
			}
			fis.close();
			br.close();

			for (int i = 0; i < array.size(); i++) {
				if (array.get(i).startsWith("[server]")) {
					int protectpos = -1;
					while (true) {
						i++;
						if (array.get(i).startsWith("protect")) {
							protectpos = i;
						}
						if (array.get(i).startsWith("commandline")) {
							if (array
									.get(i)
									.substring(
											array.get(i).indexOf("-port") + 6,
											array.get(i).indexOf(
													" ",
													(array.get(i).indexOf(
															"-port") + 6)))
									.equals(port)) {
								array.set(protectpos, "protect=1");
							} else {
								protectpos = -1;
							}

						}

						if (array.get(i).startsWith("[/server]")) {
							break;
						}
					}
				}
			}

			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			for (int i = 0; i < array.size(); i++) {
				osw.write(array.get(i));
				osw.write("\r\n");
			}
			osw.flush();
			fos.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	public void startprotect(String port) {
		for (int i = 0; i < server_srcds.size(); i++) {
			if (server_srcds.get(i).port.equals(port)) {
				server_srcds.get(i).isprotect = true;
				break;
			}
		}
		writeprotect1(port);
	}

	public void restartserver(String port) {
		for (int i = 0; i < server_srcds.size(); i++) {
			if (server_srcds.get(i).port.equals(port)) {
				server_srcds.get(i).restartserver();
				break;
			}
		}
	}

	public void stopserver(String port) {
		for (int i = 0; i < server_srcds.size(); i++) {
			if (server_srcds.get(i).port.equals(port)) {
				server_srcds.get(i).forcedstop();
				server_srcds.get(i).isprotect = false;
				break;
			}
		}
		writeprotect0(port);
	}

	private void writedelete(String port) {
		String s = null;
		boolean isok = false;
		File file = new File(configpath);
		ArrayList<String> array = new ArrayList<String>();
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileInputStream fis;
			fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			while ((s = br.readLine()) != null) {
				array.add(s);
			}
			fis.close();
			br.close();

			for (int i = 0; i < array.size(); i++) {

				if (array.get(i).startsWith("[server]")) {
					int startpos = i;
					int isthis = -1;
					int endpos = -1;
					while (true) {
						i++;
						if (array.get(i).startsWith("commandline")) {
							if (array
									.get(i)
									.substring(
											array.get(i).indexOf("-port") + 6,
											array.get(i).indexOf(
													" ",
													(array.get(i).indexOf(
															"-port") + 6)))
									.equals(port)) {
								isthis = 1;
							}

						}

						if (array.get(i).startsWith("[/server]")) {
							endpos = i;
							if (isthis == 1) {
								for (int j = 0; j <= endpos - startpos; j++) {
									array.remove(startpos);
								}
							}
							break;
						}
					}
				}
			}

			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			for (int i = 0; i < array.size(); i++) {
				osw.write(array.get(i));
				osw.write("\r\n");
			}
			osw.flush();
			fos.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	public void deleteserver(String port) {
		synchronized (server_srcds) {
			for (int i = 0; i < server_srcds.size(); i++) {
				if (server_srcds.get(i).port.equals(port)) {
					server_srcds.remove(i);

					break;
				}
			}
		}
		writedelete(port);
	}

	private boolean writeadd(String path, String commandline) {
		String s = null;
		boolean isok = true;
		File file = new File(configpath);
		ArrayList<String> array = new ArrayList<String>();
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileInputStream fis;
			fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			while ((s = br.readLine()) != null) {
				array.add(s);
			}
			fis.close();
			br.close();
			for (int i = 0; i < server_srcds.size(); i++) {
				if (commandline.substring(
						commandline.indexOf("-port") + 6,
						commandline.indexOf(" ",
								(commandline.indexOf("-port") + 6))).equals(
						server_srcds.get(i).port)) {
					isok = false;
				}
			}

			if (!isok) {
				return false;
			}
			array.add("[server]");
			array.add("path=" + path);
			array.add("protect=1");
			array.add("commandline=" + commandline);
			array.add("[/server]");

			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			for (int i = 0; i < array.size(); i++) {
				osw.write(array.get(i));
				osw.write("\r\n");
			}
			osw.flush();
			fos.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void addserver(String path, String commandline) {
		if (writeadd(path, commandline)) {
			L4d2server server = new L4d2server();
			server.serverpath = path;
			server.commandline = commandline;
			server.port = commandline.substring(
					commandline.indexOf("-port") + 6, commandline.indexOf(" ",
							(commandline.indexOf("-port") + 6)));
			server.isprotect = true;
			synchronized (server_srcds) {
				server_srcds.add(server);
			}
		}
	}

	private void writeautorestart2(String port, int hour, int min) {
		String s = null;
		boolean isok = false;
		File file = new File(configpath);
		ArrayList<String> array = new ArrayList<String>();
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileInputStream fis;
			fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			while ((s = br.readLine()) != null) {
				array.add(s);
			}
			fis.close();
			br.close();

			for (int i = 0; i < array.size(); i++) {
				if (array.get(i).startsWith("[server]")) {
					int autorestartpos = -1;
					while (true) {
						i++;
						if (array.get(i).startsWith("autorestart")) {
							autorestartpos = i;
						}
						if (array.get(i).startsWith("commandline")) {
							if (array
									.get(i)
									.substring(
											array.get(i).indexOf("-port") + 6,
											array.get(i).indexOf(
													" ",
													(array.get(i).indexOf(
															"-port") + 6)))
									.equals(port)) {
								if (autorestartpos == -1) {
									array.add(i, "autorestart=" + hour + ":"
											+ min);
									autorestartpos = -2;
								} else if (autorestartpos == -2) {
									// 什么都不做
								} else {
									array.set(autorestartpos, "autorestart="
											+ hour + ":" + min);
								}
							} else {
								autorestartpos = -1;
							}

						}

						if (array.get(i).startsWith("[/server]")) {
							break;
						}
					}
				}
			}

			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			for (int i = 0; i < array.size(); i++) {
				osw.write(array.get(i));
				osw.write("\r\n");
			}
			osw.flush();
			fos.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	private void autorestart() {
		synchronized (server_srcds) {
			for (int i = 0; i < server_srcds.size(); i++) {
				if (server_srcds.get(i).isprotect) {
					if (server_srcds.get(i).checkautorestart()) {
						System.out.println("自动重启");
						server_srcds.get(i).restartserver();
					}
				}
				// System.out.println(ip.getHostAddress());
			}
		}
	}

	public void setautorestart(String port, int hour, int min) {
		for (int i = 0; i < server_srcds.size(); i++) {
			if (server_srcds.get(i).port.equals(port)) {
				server_srcds.get(i).restart_hour = hour;
				server_srcds.get(i).restart_min = min;
				if (server_srcds.get(i).checkautorestart()) {
					server_srcds.get(i).lastrestarttime = System
							.currentTimeMillis();
				} else {
					server_srcds.get(i).lastrestarttime = 0;
				}
				writeautorestart2(port, hour, min);
				break;
			}
		}
	}
}
