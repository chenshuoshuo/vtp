package com.you07.util;

import com.you07.VtpApplication;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author egan
 * @date 2019/11/25 13:46
 * @desc
 */
public class VTPFileUtil {

    public static String getRootPath() throws FileNotFoundException {
        ApplicationHome home = new ApplicationHome(VtpApplication.class);
        File jarFile = home.getSource();
        return jarFile.getParent()+"\\";
    }


}
