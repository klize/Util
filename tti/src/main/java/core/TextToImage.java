package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class TextToImage {

  public void convert(List<String> text, String fileName, String fontFamily, int fontSize)
      throws Exception {
    if (text == null || fileName == null || fontFamily == null || fontSize == 0) {
      StringBuilder sb = new StringBuilder();
      sb.append("Invalid Parameteres...");
      sb.append("\n\t");
      if (text == null) {
        sb.append("text").append("was null\n\t");
      }
      if (fileName == null) {
        sb.append("file name").append("was null\n\t");
      }
      if (fontFamily == null) {
        sb.append("font family").append("was null\n\t");
      }
      if (fontSize == 0) {
        sb.append("font size is 0").append("\n");
      }
      System.err.println(sb.toString());
    }

    assert text != null;
    assert fileName != null;
    assert fontFamily != null;
    assert fontSize != 0;

    Graphics2D graphics = null;
    BufferedImage bufImage = null;
    Font font = null;
    FileOutputStream fos = null;

    //font에 따라 자간이 다르므로 한 글자가 차지하는 픽셀 크기가 다르다. fontSize/2는 한 글자의 픽셀 추정치.
    int w = TextToImage.getMaxLengthTextLine(text) * (fontSize/2);
    int h = (text.size()+3) * fontSize;

    System.out.println("Image width : " + w);
    System.out.println("Image height : " + h);

    //Init
    font = new Font(fontFamily, Font.PLAIN, fontSize);
    bufImage = new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB);
    fos = new FileOutputStream(new File(fileName));
    graphics = bufImage.createGraphics();

    //Draw
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, w, h);

    //Draw Text
    graphics.setFont(font);
    graphics.setColor(Color.BLACK);

    for(int i = 0 ; i < text.size(); i++){
      String contents = text.get(i);
      int hStart = 10;
      int vStart = (i+2) * fontSize;

      graphics.drawString(contents, hStart, vStart);
    }

    //Write Image
    if (ImageIO.write(bufImage, "PNG", fos) ){
      System.out.println(fileName + " to Image Success");
    }else{
      System.err.print(fileName + " to Image Failed");
    }
  }

  private static int getMaxLengthTextLine(List<String> text) {
    int maxSize = 0;
    for(int i = 0 ; i < text.size(); i++){
      int size = 0;
      String line = text.get(i);
      for(int j = 0; j < line.length(); j++){
        if( Character.getType(line.charAt(j)) == Character.OTHER_LETTER){
          size += 2;
        }else {
          size += 1;
        }
      }
      if (maxSize < size) {
        maxSize = size;
      }
    }
    return maxSize;
  }

  public String[] getFileListFromDir(String pDir) throws Exception {
    File dir = new File(pDir);
    File[] fileList = dir.listFiles();

    if (fileList.length >= 1) {
      String[] pathList = new String[fileList.length];

      for(int i = 0; i <fileList.length; i++){
        String path = fileList[i].getParent();
        String name = fileList[i].getName();
        pathList[i] = path + "/" + name;
      }

      return pathList;
    }else{
      return null;
    }
  }

  public List<String> readFile(String fileName) throws Exception {
    BufferedReader br = new BufferedReader(new FileReader(fileName));
    String contents = new String();
    List<String> list = new ArrayList<>();

    while((contents = br.readLine()) != null){
      contents = contents.replaceAll("\t","    ");
      list.add(contents);
      System.out.println(contents);
    }
    br.close();
    return list;
  }


}
