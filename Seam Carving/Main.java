import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Main {

    public static void main(String[] args) {
        // Read in the image
        String imgfilename = "test1.png";
        
        BufferedImage img;
        try {
            img = ImageIO.read(new File(imgfilename));
        } catch(Exception e) {
            System.out.println("Could not read: " + imgfilename);
            return;
        }
        
        int[][][] imagePixels = new int[img.getWidth()][img.getHeight()][3];
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int tmp = img.getRGB(i, j);
                
                imagePixels[i][j][0] =  (tmp & 0x00ff0000) >> 16;
                imagePixels[i][j][1] =  (tmp & 0x0000ff00) >> 8;
                imagePixels[i][j][2] =  (tmp & 0x000000ff);
            }
        }
        
        
        Long start = System.currentTimeMillis();
        SeamCarving sc = new SeamCarving();
        double weight = sc.run(imagePixels);
        System.out.println("weight: " + weight);
        System.out.println("seam: " + Arrays.toString(sc.getSeam()));
        Long end = System.currentTimeMillis();
        System.out.println("time: " + ((end - start) / 1000.0));
       
        // Uncomment to display seam on image 
        /*
        int[] seam = sc.getSeam();
        for (int j = 0; j < img.getHeight(); j++) {
            img.setRGB(seam[j], j, 0x00ff0000);
        }
        
        JFrame jf = new JFrame();
        JPanel jp = new JPanel();
        jf.add(new JLabel(new ImageIcon(img)));
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
        */
    }
}
