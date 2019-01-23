package com.MeMaker.gui;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class PersonFileFilter extends FileFilter {

    public PersonFileFilter() {
        super();
    }

    @Override
    public boolean accept(File f) {

        if(f.isDirectory()) { // it enables view of directories, without it you can not see them
            return true;
        }

        String name = f.getName();

        String extension = Utils.getFileExtension(name);

        if(extension == null) {
            return false;
        }

        if(extension.equals("per")){
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Person database files (*.per)";
    }
}
