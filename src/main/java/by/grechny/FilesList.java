package by.grechny;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FilesList {

    public FilesList(Map commandArgs) {

        List<String> filesList = new ArrayList<String>();

        File dir = new File(System.getProperty("user.dir"));
        filesList.addAll(Arrays.asList(dir.list()));

        for (String file : filesList){
            System.out.println(file);
        }
    }
}
