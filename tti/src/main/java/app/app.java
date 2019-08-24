package app;

import java.util.List;
import core.TextToImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class app {

  public static final Logger logger = LoggerFactory.getLogger(app.class);

  public static void main(String[] args){
    TextToImage converter = new TextToImage();
    String path = "/home/dk/Data/coding-style";

    try{
      List<String> fileList = converter.getFileListFromDir(path);
      logger.info("files : {}", fileList);

      if (fileList != null){
        for(int i = 0 ; i < fileList.size(); i++){
          String filename = fileList.get(i);
          List<String> contents = converter.readFile(filename);
          String output = filename.split("\\.")[0]+".png";

          converter.convert(contents, output, "바탕", 25);
        }
      }
    }catch (Exception e){
      e.printStackTrace();
    }


  }
}
