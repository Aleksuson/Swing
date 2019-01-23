package com.MeMaker.gui;

import javax.swing.*;
import java.awt.*;

import java.io.IOException;
import java.net.URL;

class Utils {

    // in tutorial on fileChooser you can find it
    static String getFileExtension(String name) {

        int pointIndex = name.lastIndexOf(".");

        if(pointIndex == -1) {
            return null;
        }

        if(pointIndex == name.length()-1) {
            return null;
        }

        return name.substring(pointIndex+1);

    }

    // method for loading images from file
    static ImageIcon createIcon(String path) {
        URL url = Utils.class.getResource(path);

        if(url == null) {
            System.err.println("Unable to load image " + path);
        }

        assert url != null;
        return new ImageIcon(url);

    }

    static Font createFont(String path) {
        URL url = Utils.class.getResource(path);

        if(url == null) System.err.println("Unable to load font " + path);
        Font font = null;
        try {
            assert url != null;
            font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
        } catch (FontFormatException e) {
            System.out.println("Bad format in font file: " + path);
        } catch (IOException e) {
            System.out.println("Unable to read font file: " + path);
        }

        return font;

    }


}
