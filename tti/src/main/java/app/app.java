package app;

import java.util.List;
import core.TextToImage;

public class app {
  public static void main(String[] args){
    TextToImage converter = new TextToImage();
    String path = "/home/dk/Data/coding-style";

    try{
      String[] fileList = converter.getFileListFromDir(path);

      if (fileList != null){
        for(int i = 0 ; i < fileList.length; i++){
          List<String> contents = converter.readFile(fileList[i]);
          String output = fileList[i].split("\\.")[0]+".png";

          converter.convert(contents, output, "바탕", 25);
        }
      }


    }catch (Exception e){
      e.printStackTrace();
    }


  }
}
