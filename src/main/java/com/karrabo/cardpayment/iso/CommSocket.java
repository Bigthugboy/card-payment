package com.karrabo.cardpayment.iso;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

@Slf4j
public class CommSocket {
    private SocketChannel mSocketChannel;
    private Selector mSelector;
    private boolean mIsGoOn = true;

    public boolean open(String host, String port) {
        if (port == null) return false;
        int intPort = Integer.parseInt(port);
        log.info("ABOUT OPENING PORT");
        return open(host, intPort);
    }

    public void setStop() {
        mIsGoOn = false;
    }

    public boolean open(String host, int port) {
    	log.info("host="+host+", port="+port);
        if (host.isEmpty() || port <= 0) {
            log.info("host or port error.");
            return false;
        }
        try {
            mSocketChannel = SocketChannel.open();
            mSocketChannel.configureBlocking(false);
            long mEndtTime = System.currentTimeMillis() + 5000;
            mSocketChannel.connect(new InetSocketAddress(host, port));
            while(!mSocketChannel.finishConnect() && (System.currentTimeMillis() <= mEndtTime)){}
            if(System.currentTimeMillis() > mEndtTime){
                log.info("TIME ERROR WHILE OPENING PORT");
                return false;
            }
            mSelector = Selector.open();
            mSocketChannel.register(mSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("EXCEPTION WHILE OPENING PORT");
            return false;
        }
        log.info("DONE OPENING PORT");
        return true;
    }


    public int send(byte[] sendPacket) {
        //log.info("send = "+BCDASCII.bytesToHexString(sendPacket));
        log.info("ABOUT SENDING BYTES");
        int count = 0;
        try {
            mIsGoOn = true;
            //TODO:
            long mEndtTime = System.currentTimeMillis() + 60000;
            while (mIsGoOn && mSelector.select() > 0
                    && (System.currentTimeMillis() <= mEndtTime)) {

                if(System.currentTimeMillis() > mEndtTime){
                    log.info("TIME OUT WHILE SENDING BYTES");
                    return 0;
                }

                Iterator it = mSelector.selectedKeys().iterator();
                while (mIsGoOn && it.hasNext()) {
                    SelectionKey sk = (SelectionKey) it.next();
                    it.remove();
                    if (sk.isWritable()) {
                        SocketChannel socketChannel = (SocketChannel) sk.channel();
                        ByteBuffer bb = ByteBuffer.wrap(sendPacket, 0, sendPacket.length);
                        log.info("LENGTH OF SENT: " + sendPacket.length);
                        count = socketChannel.write(bb);
                        log.info("Send ok.");
                        return count;
                    }
                    mSelector.selectedKeys().remove(sk);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("EXCEPTION HAPPENED WHILE SENDING BYTES");
            return -1;
        }
        log.info("SENT: " + count);
        return count;
    }

    public byte[] recv() {
        byte[] receive = null;
        int count = 0;
        log.info("ABOUT RECEIVING BYTES");
        try {
            mIsGoOn = true;
            //TODO:
            long mEndtTime2 = System.currentTimeMillis() + 60000;
            while (mIsGoOn && mSelector.select() > 0
                    && (System.currentTimeMillis() <= mEndtTime2)) {
                Iterator it = mSelector.selectedKeys().iterator();
                if(System.currentTimeMillis() > mEndtTime2){
                    log.info("A: TIME OUT WHILE RECEIVING BYTES");
                    return null;
                }
                long mEndtTime = System.currentTimeMillis() + 60000;
                while (mIsGoOn && it.hasNext()
                        && (System.currentTimeMillis() <= mEndtTime)) {

                    if(System.currentTimeMillis() > mEndtTime){
                        log.info("TIME OUT WHILE RECEIVING BYTES");
                        return null;
                    }

                    SelectionKey sk = (SelectionKey) it.next();
                    it.remove();
                    if (sk.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) sk.channel();
                        ByteBuffer bb = ByteBuffer.allocate(ISO8583.MAXBUFFERLEN);
                        bb.clear();
                        count = socketChannel.read(bb);
                        bb.flip();
                        if (count>0) {
                            receive = new byte[count];
                            System.arraycopy(bb.array(), 0, receive, 0, count);
                            log.info("RECV: " + socketChannel.socket().getRemoteSocketAddress() + ": " + BCDASCII.bytesToHexString(receive));
                            log.info("DONE RECEIVING BYTES");
                            return receive;
                        } else {
                            log.info("CHECKING... A");
                            log.info("COULD NOT RECEIVE BYTES");
                            return null;
                        }
                    }
                    mSelector.selectedKeys().remove(sk);
                }
            }
        } catch (IOException e) {
            log.info("EXCEPTION OCCURRED WHILE RECEIVING BYTES");
            e.printStackTrace();
        }
        log.info("RECEIVED SUCCESSFULLY");
        return receive;
    }

    public void close() {
        log.info("ABOUT CLOSING SOCKET");
        try {
            if (mSocketChannel != null && mSocketChannel.isConnected()) {
                mSocketChannel.finishConnect();
                mSelector.close();
                mSocketChannel.close();
                log.info("SOCKET CLOSED");
            }
        } catch (IOException e) {
            log.info("COUNLD NOT CLOSE SOCKET");
            e.printStackTrace();
        }
    }
}
