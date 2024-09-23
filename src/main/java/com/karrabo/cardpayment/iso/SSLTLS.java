package com.karrabo.cardpayment.iso;


import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.tls.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;


@Slf4j
public class SSLTLS {
    public static String hexStringToByteArray(String hex) {
        try {
            int l = hex.length();
            byte[] data = new byte[l / 2];
            for (int i = 0; i < l; i += 2) {
                data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                        + Character.digit(hex.charAt(i + 1), 16));
            }
            String st = new String(data, StandardCharsets.UTF_8);
            return st;
        }catch(Exception e)
        {
            return "-/";
        }
    }

    public static byte[] doSSL(String host, String port, byte[] send) throws Exception {
        Socket socket = null;
        log.info("DONE WITH SSL SOCKET 1");
        try {
            log.info("DONE WITH SSL SOCKET 2");
            java.security.SecureRandom secureRandom = new java.security.SecureRandom();
            log.info("DONE WITH SSL SOCKET 3: " + java.net.InetAddress.getByName(host).toString());
            socket = new Socket(java.net.InetAddress.getByName(host), Integer.parseInt(port));
            //socket.setSoTimeout(10*1000);//Set timeout
            log.info("DONE WITH SSL SOCKET");
            TlsClientProtocol protocol = new TlsClientProtocol(socket.getInputStream(), socket.getOutputStream(), secureRandom);
            DefaultTlsClient client = new DefaultTlsClient() {
                public TlsAuthentication getAuthentication() throws IOException {
                    TlsAuthentication auth = new TlsAuthentication() {
                        // Capture the server certificate information!
                        public void notifyServerCertificate(org.bouncycastle.crypto.tls.Certificate serverCertificate) throws IOException {
                        }

                        public TlsCredentials getClientCredentials(CertificateRequest certificateRequest) throws IOException {
                            return null;
                        }
                    };
                    return auth;
                }
            };

            protocol.connect(client);
            log.info("PROCEED AFTER SSL SOCKET GET");
            OutputStream output = protocol.getOutputStream();
            output.write(send);
            output.flush();

            log.info("PROCEED AFTER SENDING BYTES");
            InputStream input = protocol.getInputStream();


            //Here
            if(host.contains("204.8.207.93"))
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                log.info("PROCEED AFTER SENDING BYTES 2");
                char[] msgLengthBuffer = new char[2];
                int attempts = 0;
                int totalBytesRead = 0;
                while(totalBytesRead < 2 && attempts<10){
                    attempts++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Waiting for data");
                    int bytesRead = reader.read(msgLengthBuffer, totalBytesRead, 2-totalBytesRead);
                    if(bytesRead > 0){
                        System.out.println("Read bytes length: " +  bytesRead);
                        System.out.println("Read bytes value: " +  new String(msgLengthBuffer));
                        totalBytesRead += bytesRead;
                        int msgLength = ((int) 0xFF & msgLengthBuffer[0]) * 256 + (0xFF & msgLengthBuffer[1]);
                        System.out.println("Length to be read: " +  msgLength);
                        char[] response = new char[msgLength];
                        bytesRead = reader.read(response, 0, msgLength);
                        totalBytesRead += bytesRead;
                        System.out.println("2. Read bytes length: " +  totalBytesRead);
                        System.out.println("1. RES: " +  new String(response));
                        return new String(response).getBytes(StandardCharsets.UTF_8);
                    }
                    else{
                        break;
                    }
                }
                return null;
            }else
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                log.info("PROCEED AFTER SENDING BYTES 2");
                String line;
                byte[] ret = null;
                long mEndtTime = System.currentTimeMillis() + 60000;
                while ((line = reader.readLine()) != null
                        && (System.currentTimeMillis() <= mEndtTime)) {
                    if(System.currentTimeMillis() > mEndtTime){
                        log.info("TIME OUT WHILE RECEIVING BYTES");
                        socket.close();
                        return null;
                    }
                    log.info("DATA: " + line);
                    ret = line.getBytes();
                }
                log.info("CLOSE AFTER RECEIVING BYTES");
                socket.close();
                return ret;
            }
        }catch(Exception e)
        {
            if(socket != null)
                socket.close();
            return null;
        }
    }

    public static byte[] secureISOMessageSenderReceiver(String host, String port, byte[] send){
        String serverAddress = host;
        int serverPort[] = new int []{Integer.parseInt(port)};
        for (int i = 0; i < serverPort.length; i++) {
            try {
                // Load the truststore
                System.out.println("PORT---------"+serverPort[i]);
                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[0];
                            }

                            public void checkClientTrusted(
                                    X509Certificate[] certs, String authType) {
                            }

                            public void checkServerTrusted(
                                    X509Certificate[] certs, String authType) {
                            }
                        }};
                SSLContext sc = SSLContext.getInstance("SSL");

                sc.init(null, trustAllCerts, new java.security.SecureRandom());


                // Connect to the server using SSL
                SSLSocketFactory sslSocketFactory = sc.getSocketFactory();
                Socket socket = sslSocketFactory.createSocket(serverAddress, serverPort[i]);

                //logger.info("ISO MESSAGE: " + isoMessage);
                //isoMessage ="0200F23C44D129E09000000000000000002216418742210024332100000000000065650003130048250002070048250313210659990510012C0000000006111129374187422100243321D2106226124306470000020031300482522620390006203900000000006Citi";
                socket.setSoTimeout(30000);
                DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                DataInputStream dIn = new DataInputStream(socket.getInputStream());

                byte[] getSending = new byte[send.length - 2];
                System.arraycopy(send, 2, getSending, 0, send.length - 2);
                System.out.println("sending ...." + new String(getSending, StandardCharsets.UTF_8));
                System.out.println("sending ...." + new String(send, StandardCharsets.UTF_8));

                System.out.println("sending ...." + new String(getSending, StandardCharsets.UTF_8));
                dOut.writeUTF(new String(getSending, StandardCharsets.UTF_8));
                String response;


                if (true) {
                    byte[] buffer = new byte[4096];
                    int bytesRead = dIn.read(buffer);
                    response = new String(buffer, 0, bytesRead);
                } else {
                    response = dIn.readUTF();
                    System.out.println("sending ...." + response);
                    //dataInputStream.readUTF();
                }

//                printHexDump(response.getBytes());
                System.out.println(response.length() + "-" + response);
                //80810023800000280000008041330301330301330300804252105F597
                // 0810023800000280000008041330301330301330300804252105F597
                dOut.close();
                dIn.close();
                socket.close();
                return response.getBytes(StandardCharsets.UTF_8);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}

