package org.example;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Canvas;
import java.awt.event.WindowListener;
import java.util.ArrayList;

public class MainSwing extends JFrame {

    private EmbeddedMediaPlayer mediaPlayer;
    static ArrayList<String> vlcArgs;


    public MainSwing() {
        NativeDiscovery nativeDiscovery = new NativeDiscovery();
        boolean found = nativeDiscovery.discover();
        System.out.println("VLC discovery status: " + found);

        setVlcParams();

        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(vlcArgs.toArray(new String[vlcArgs.size()]));
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        Canvas videoSurface = new Canvas();
        videoSurface.setBackground(Color.black);

        Dimension videoSize = new Dimension(640, 480);
        videoSurface.setPreferredSize(videoSize);
        add(videoSurface, BorderLayout.CENTER);
        pack();
        setVisible(true);

        mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(videoSurface));

        String rtspUrl = "rtsp://192.168.1.10:554/user=admin&password=&channel=1&stream=0.sdp?";

        mediaPlayer.playMedia(rtspUrl);

        setTitle("RTSP Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayer.release();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayer.release();
                System.out.println("Uygulama kapandı");
                System.exit(0); 
            }
        });



    }
    private static void setVlcParams() {
        vlcArgs = new ArrayList<String>();
        vlcArgs.add("--no-plugins-cache");
        vlcArgs.add("--no-video-title-show");
        vlcArgs.add("--no-snapshot-preview");
        vlcArgs.add("--file-caching=0");
        vlcArgs.add("--clock-jitter=0");
        vlcArgs.add("--network-caching=230");
        vlcArgs.add("--live-caching=0");
        vlcArgs.add("--no-overlay");
        vlcArgs.add("--clock-jitter=0");
        vlcArgs.add("--video-filter=adjust");
        vlcArgs.add(" --brightness=1.8 ");
        vlcArgs.add("--sout-keep");
        vlcArgs.add(
                ":sout = #transcode{vcodec=x264,vb=800,scale=0.20,acodec=none,fps=24}:display :no-sout-rtp-sap :no-sout-standard-sap :ttl=1  :sout-keep");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainSwing::new);
    }

}

