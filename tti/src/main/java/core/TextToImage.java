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
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextToImage {

  private static final String[] EXCEPTION_EXT = {"png"};
  private Set<String> EXCEPTION = new LinkedHashSet<String>(Arrays.asList(EXCEPTION_EXT));

  private static Logger logger = LoggerFactory.getLogger(TextToImage.class);

  public void convert(List<String> text, String fileName, String fontFamily, int fontSize)
      throws Exception {
    if (text == null || fileName == null || fontFamily == null || fontSize == 0) {
      logger.error("Invalid Parameteres...");
      if (text == null) {
        logger.error("text was null", new NullPointerException());
      }
      if (fileName == null) {
        logger.error("file name was null", new NullPointerException());
      }
      if (fontFamily == null) {
        logger.error("font family was null", new NullPointerException());
      }
      if (fontSize == 0) {
        logger.error("font size was 0", new IllegalArgumentException());
      }
    }

    Graphics2D graphics = null;
    BufferedImage bufImage = null;
    Font font = null;
    FileOutputStream fos = null;

    //font에 따라 자간이 다르므로 한 글자가 차지하는 픽셀 크기가 다르다. fontSize/2는 한 글자의 픽셀 추정치.
    int w = TextToImage.getMaxLengthTextLine(text) * (fontSize/2);
    int h = (text.size()+3) * fontSize;

    logger.info(String.format("Image width : %d", w));
    logger.info(String.format("Image height : %d", h));

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
      logger.info("Successfully converted :" + fileName);
    }else{
      logger.error("Failed to convert :" + fileName);
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

  public List<String> getFileListFromDir(String pDir) throws Exception {
    File dir = new File(pDir);
    File[] fileList = dir.listFiles();

    List<String> pathList = new ArrayList<>();

    for(int i = 0; i <fileList.length; i++){
      String name = fileList[i].getName();
      if (EXCEPTION.contains(name.substring(name.lastIndexOf(".")+1))) {
        logger.debug(name + " is not a text file -> SKIPPING");
        continue;
      }
      String path = fileList[i].getParent();
      pathList.add(path + "/" + name);
    }

    return pathList;

  }

  public List<String> readFile(String fileName) throws Exception {
    BufferedReader br = new BufferedReader(new FileReader(fileName));
    String contents = new String();
    List<String> list = new ArrayList<>();

    while((contents = br.readLine()) != null){
      contents = contents.replaceAll("\t","    ");
      list.add(contents);
      logger.debug(contents);
    }
    br.close();
    return list;
  }


}
