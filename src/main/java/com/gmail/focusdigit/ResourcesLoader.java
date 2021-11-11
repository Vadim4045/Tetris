package com.gmail.focusdigit;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ResourcesLoader {
    final String jarPath;
    final String idePath;

    public ResourcesLoader(String idePath, String jarPath){
        this.idePath = idePath;
        this.jarPath = jarPath;
    }

    public BufferedImage get(String name) throws IOException {
        final File jarFile = new File(ResourcesLoader
                .class.getProtectionDomain()
                .getCodeSource().getLocation()
                .getPath());

        //try {
            if (jarFile.isFile()){
                String path = (jarPath + "/" + name).replace("//","/").trim();
                return ImageIO.read(getClass().getResourceAsStream(path));
            }
            else{
                String path = (idePath + "/" + name).replace("//","/").trim();
                return ImageIO.read(new File(path));
            }
        /*}catch(IOException ie){
            JOptionPane.showMessageDialog(null,ie.getMessage(),"IOexception",JOptionPane.WARNING_MESSAGE);
        }
        return null;*/
    }
}
