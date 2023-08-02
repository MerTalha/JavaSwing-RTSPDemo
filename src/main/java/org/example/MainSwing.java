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

        // Uygulama kapatıldığında medya oynatıcısı kaynağını serbest bırakın
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayer.release();
                System.out.println("Uygulama kapandı");
                System.exit(0); // Uygulamayı tamamen kapat
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
        // vlcArgs.add("--video-filter=sharpen");
        // vlcArgs.add("--contrast = 4");
        // vlcArgs.add("--realrtsp-caching=0");
        vlcArgs.add("--video-filter=adjust");
        vlcArgs.add(" --brightness=1.8 ");
        // vlcArgs.add("bits = 1024");
        vlcArgs.add("--sout-keep");
        // vlcArgs.add("--scale=2");
        //vlcArgs.add("--fps=4");
        // vlcArgs.add("--sout-mux-caching=10");
        // vlcArgs.add("--sout-mp4-faststart");
        vlcArgs.add(
                ":sout = #transcode{vcodec=x264,vb=800,scale=0.20,acodec=none,fps=24}:display :no-sout-rtp-sap :no-sout-standard-sap :ttl=1  :sout-keep");
        //// --contrast <float> Image contrast (0-2)
        // --brightness <float> Image brightness (0-2)
        // --hue <integer> Image hue (0-360)
        // --saturation <float> Image saturation (0-3)
        // --gamma <float> Image gamma (0-10)
        // --brightness-threshold, --no-brightness-threshold
        // Brightness threshold (default disabled)
        // vlcArgs.add("--video-filter adjust --brightness 1.8 --contrast 1.5");
        // vlcArgs.add(":contrast 1.5");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainSwing::new);
    }

}

