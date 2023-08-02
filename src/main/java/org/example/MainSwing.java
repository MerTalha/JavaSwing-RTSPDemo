package org.example;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainSwing extends JFrame {

    private EmbeddedMediaPlayer mediaPlayer;

    public MainSwing() {
        // VLC player'ın çalıştırılabilir dosya yolunu belirtin (VLC player'ı sisteminize kurmuşsanız bu gerekli değildir)
        NativeDiscovery nativeDiscovery = new NativeDiscovery();
        nativeDiscovery.discover();

        // VLC player için ayarları yapın
        String[] vlcArgs = {"--no-video-title-show", "--rtsp-tcp"};

        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(vlcArgs);
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        Canvas videoSurface = new Canvas();
        videoSurface.setBackground(Color.black);
        mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(videoSurface));

        // IP kameranın RTSP URL'sini belirtin
        String rtspUrl = "rtsp://zephyr.rtsp.stream/pattern?streamKey=ebf83f60502a6471dd6ea395626f4389";

        mediaPlayer.playMedia(rtspUrl);

        // Pencere ve görüntü ayarları
        setTitle("RTSP Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLayout(new BorderLayout());
        add(videoSurface, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Uygulama kapatıldığında medya oynatıcısı kaynağını serbest bırakın
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayer.release();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainSwing::new);
    }
}

