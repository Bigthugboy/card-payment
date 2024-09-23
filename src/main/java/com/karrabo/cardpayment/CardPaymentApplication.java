package com.karrabo.cardpayment;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.jcraft.jsch.*;


import java.io.InputStream;

@SpringBootApplication
public class CardPaymentApplication {
        public static void main(String[] args) {
            String user = "superuser_name";    // Superuser name
            String host = "44.238.1.233";            // VPN IP address
            int port = 22;                     // SSH port (usually 22)
            String password = "Administrator1_"; // Superuser password

            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession(user, host, port);
                session.setPassword(password);

                // Avoid asking for key confirmation
                session.setConfig("StrictHostKeyChecking", "no");

                System.out.println("Connecting to VPN server...");
                session.connect();  // Connect to VPN

//                JSch jsch = new JSch();
//                Session session = jsch.getSession(user, host, port);
//                session.setPassword(password);
//                session.setConfig("StrictHostKeyChecking", "no");
//                session.connect();


                // Run command on VPN (e.g., ping an external IP)
                String command = "ping -c 4 20.119.72.7"; // Command to execute

                ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
                channelExec.setCommand(command);

                channelExec.setErrStream(System.err);
                channelExec.connect();

                // Read command output
                InputStream in = channelExec.getInputStream();
                byte[] tmp = new byte[1024];
                while (true) {
                    while (in.available() > 0) {
                        int i = in.read(tmp, 0, 1024);
                        if (i < 0) break;
                        System.out.print(new String(tmp, 0, i));
                    }
                    if (channelExec.isClosed()) {
                        if (in.available() > 0) continue;
                        System.out.println("Exit Status: " + channelExec.getExitStatus());
                        break;
                    }
                    Thread.sleep(1000);
                }

                channelExec.disconnect();
                session.disconnect();
                System.out.println("Disconnected from VPN server.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }