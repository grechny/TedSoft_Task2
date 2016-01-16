package by.grechny;

import java.io.File;
import java.util.Map;

public class CurrentPath {

    public CurrentPath(Map commandArgs){

        System.out.println(new File(System.getProperty("user.dir")).getPath());

    }

}
