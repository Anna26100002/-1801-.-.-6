import javax.swing.*;
import java.awt.image.*;
import java.awt.*;

/** Класс для отображения фракталов. Производный от javax.swing.JComponent.*/
class JImageDisplay extends JComponent
{
    /**
     * Экземпляр Buffered Image.
     * Управляет изображением, содержимое которого можно записать.
     */
    private BufferedImage displayImage;

    public BufferedImage getImage() {
        return displayImage;
    }
    /**
     * Конструктор JImageDisplay принимает целочисленные значения ширины и высоты,
     * и инициализирует объект BufferedImage новым изображением с этой шириной и высотой, и типом изображения TYPE_INT_RGB
     */
    public JImageDisplay(int width, int height) {
        displayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        /**
         * Вызов метода родительского класса setPreferredSize()
         * с указанной шириной и высотой.
         */
        Dimension imageDimension = new Dimension(width, height);
        super.setPreferredSize(imageDimension);
    }

    /**
     * Суперкласс paintComponent(g) для правильного отображения изображения и отрисовка изображения.
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(displayImage, 0, 0, displayImage.getWidth(), displayImage.getHeight(), null);
    }

    /**
     * Установка всех пикселей изображения в чёрный цвет.
     */
    public void clearImage()
    {
        int[] blankArray = new int[getWidth() * getHeight()];
        displayImage.setRGB(0, 0, getWidth(), getHeight(), blankArray, 0, 1);
    }
    /**
     * Установка пикселя в определённый цвет.
     */
    public void drawPixel(int x, int y, int rgbColor0) { displayImage.setRGB(x, y, rgbColor0);
    }
}