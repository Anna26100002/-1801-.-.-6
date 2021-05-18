import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import javax.swing.JFileChooser.*;
import javax.swing.filechooser.*;
import javax.imageio.ImageIO.*;
import java.awt.image.*;


public class FractalExplorer
{
    //Поля для кнопок сохранить, сбросить и комбобокса
    private JButton saveButton;
    private JButton resetButton;
    private JComboBox myComboBox;


    private int rowsRemaining;


    private int displaySize;


    private JImageDisplay display;


    private FractalGenerator fractal;

    /**
     * A Rectangle2D.Double object which specifies the range of the complex
     * that which we are currently displaying.
     */
    private Rectangle2D.Double range;

    /**
     * A constructor that takes a display-size, stores it, and
     * initializes the range and fractal-generator objects.
     */
    public FractalExplorer(int size) {
        // Stores display-size
        displaySize = size;

        // Initializes the fractal-generator and range objects.
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);

    }

    /**
     * This method intializes the Swing GUI with a JFrame holding the
     * JImageDisplay object and a button to reset the display, a button
     * to save the current fractal image, and a JComboBox to select the
     * type of fractal.  The JComboBox is held in a JPanel with a label.
     */
    public void createAndShowGUI()
    {
        // Set the frame to use a java.awt.BorderLayout for its contents.
        display.setLayout(new BorderLayout());
        JFrame myFrame = new JFrame("Fractal Explorer");

        // Add the image-display object in the BorderLayout.CENTER position.
        myFrame.add(display, BorderLayout.CENTER);

        // Create a reset button.
        resetButton = new JButton("Reset");

        // Instance of the ButtonHandler on the reset button.
        ButtonHandler resetHandler = new ButtonHandler();
        resetButton.addActionListener(resetHandler);

        // Instance of the MouseHandler on the fractal-display component.
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        // Set the frame's default close operation to "exit".
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set up a combo box.
        myComboBox = new JComboBox();

        // Add each fractal type object to the combo box.
        FractalGenerator mandelbrotFractal = new Mandelbrot();
        myComboBox.addItem(mandelbrotFractal);
        FractalGenerator tricornFractal = new Tricorn();
        myComboBox.addItem(tricornFractal);
        FractalGenerator burningShipFractal = new BurningShip();
        myComboBox.addItem(burningShipFractal);

        // Instance of ButtonHandler on the combo box.
        ButtonHandler fractalChooser = new ButtonHandler();
        myComboBox.addActionListener(fractalChooser);

        // Create a new JPanel object, add a JLabel object and a JComboBox
        // object to it, and add the panel into the frame in the NORTH
        // position in the layout.
        JPanel myPanel = new JPanel();
        JLabel myLabel = new JLabel("Fractal:");
        myPanel.add(myLabel);
        myPanel.add(myComboBox);
        myFrame.add(myPanel, BorderLayout.NORTH);

        // Create a save button, add it to a JPanel in the BorderLayout.SOUTH
        // position along with the reset button.
        saveButton = new JButton("Save");
        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(saveButton);
        myBottomPanel.add(resetButton);
        myFrame.add(myBottomPanel, BorderLayout.SOUTH);

        // Instance of ButtonHandler on the save button.
        ButtonHandler saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);


        // Lay out contents of the frame, cause it to be visible, and
        // disallow resizing of the window.
        myFrame.pack();
        myFrame.setVisible(true);
        myFrame.setResizable(false);

    }

    /** Private helper method to display the fractal.  **/
    private void drawFractal()
    {
        // Отключение всех элементов пользовательского интерфейса во время рисования
        enableUI(false);

        // Общее кол-во строк
        rowsRemaining = displaySize;

        // Для каждой строки вызываем экземпляр класса FractalWorker
        for (int x=0; x<displaySize; x++){
            FractalWorker drawRow = new FractalWorker(x);
            drawRow.execute();
        }

    }
    //Включение и выключение кнопок на дисплее
    private void enableUI(boolean val) {
        myComboBox.setEnabled(val);
        resetButton.setEnabled(val);
        saveButton.setEnabled(val);
    }
    /**
     * An inner class to handle ActionListener events.
     */
    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // Get the source of the action.
            String command = e.getActionCommand();

            // If the source is the combo box, get the fractal the user
            // selected and display it.
            if (e.getSource() instanceof JComboBox) {
                JComboBox mySource = (JComboBox) e.getSource();
                fractal = (FractalGenerator) mySource.getSelectedItem();
                fractal.getInitialRange(range);
                drawFractal();

            }
            // If the source is the reset button, reset the display and draw
            // the fractal.
            else if (command.equals("Reset")) {
                fractal.getInitialRange(range);
                drawFractal();
            }
            // If the source is the save button, save the current fractal
            // image.
            else if (command.equals("Save")) {

                // Выбираем файл для сохранения
                JFileChooser myFileChooser = new JFileChooser();

                // Save only PNG images.
                FileFilter extensionFilter =
                        new FileNameExtensionFilter("PNG Images", "png");
                myFileChooser.setFileFilter(extensionFilter);

                // Ensures that the filechooser won't allow non-".png"
                // filenames.
                myFileChooser.setAcceptAllFileFilterUsed(false);

                // Pops up a "Save file" window which lets the user select a
                // directory and file to save to.
                int userSelection = myFileChooser.showSaveDialog(display);

                // If the outcome of the file-selection operation is
                // APPROVE_OPTION, continue with the file-save operation.
                if (userSelection == JFileChooser.APPROVE_OPTION) {

                    // Get the file and file name.
                    java.io.File file = myFileChooser.getSelectedFile();
                    String file_name = file.toString();

                    // Try saving the fractal image to disk.
                    try {
                        BufferedImage displayImage = display.getImage();
                        javax.imageio.ImageIO.write(displayImage, "png", file);
                    }
                    // Catches all exceptions and prints a message with the
                    // exception.
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(display,
                                exception.getMessage(), "Cannot Save Image",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                // If the file-save operation is not APPROVE_OPTION, return.
                else return;
            }
        }
    }

    /**
     * An inner class to handle MouseListener events from the display.
     */
    private class MouseHandler extends MouseAdapter
    {
        /**
         * When the handler receives a mouse-click event, it maps the pixel-
         * coordinates of the click into the area of the fractal that is being
         * displayed, and then calls the generator's recenterAndZoomRange()
         * method with coordinates that were clicked and a 0.5 scale.
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            // Return immediately if rowsRemaining is nonzero.
            if (rowsRemaining != 0) {
                return;
            }
            // Get x coordinate of display area of mouse click.
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x,
                    range.x + range.width, displaySize, x);

            // Get y coordinate of display area of mouse click.
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y,
                    range.y + range.height, displaySize, y);

            // Call the generator's recenterAndZoomRange() method with
            // coordinates that were clicked and a 0.5 scale.
            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            // Redraw the fractal after the area being displayed has changed.
            drawFractal();
        }
    }

    //Вычисляет значение цвета для 1 строки фрактала
    private class FractalWorker extends SwingWorker<Object, Object> //obj1 - тип значения,
            // возвращаемого функцией doInBackground(), когда задача полностью выполнена
        //Тип obj2 используется, когда фоновая задача возвращает промежуточные значения во время выполнения
    {
        // y координата вычисляемой строки
        int yCoordinate;

        //Массив чисел для хранения вычисленных значений RGB для каждого пикселя в этой строке
        int[] computedRGBValues;

        private FractalWorker(int row) {
            yCoordinate = row;
        }

        //Вызывается в фоновом потоке и отвечает за выполнение длительной задачи.
        protected Object doInBackground() {

            //Для сохранения каждого значения RGB в соответствующем элементе целочисленного массива
            computedRGBValues = new int[displaySize];

            // Проходимся по всем пикселям в строке
            for (int i = 0; i < computedRGBValues.length; i++) {

                // Находим соответствующие координаты x и y
                double xCoord = fractal.getCoord(range.x,
                        range.x + range.width, displaySize, i);
                double yCoord = fractal.getCoord(range.y,
                        range.y + range.height, displaySize, yCoordinate);

                // Вычисляем кол-во итераций для координат
                int iteration = fractal.numIterations(xCoord, yCoord);


                if (iteration == -1){
                    computedRGBValues[i] = 0;
                }

                else {
                    // Выбираем значение цвета на основе числа итераций
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    // Обновляем массив с цветами текущего пикселя
                    computedRGBValues[i] = rgbColor;
                }
            }
            return null;

        }

        //Вызывается, когда фоновая задача завершена, и этот метод вызывается из потока обработки событий Swing
        protected void done() {
            //Перебираем массив строк данных, рисуя пиксели, которые были вычислены в doInBackground ()
            for (int i = 0; i < computedRGBValues.length; i++) {
                display.drawPixel(i, yCoordinate, computedRGBValues[i]);
            }
            display.repaint(0, 0, yCoordinate, displaySize, 1);


            rowsRemaining--;
            if (rowsRemaining == 0) {
                enableUI(true);
            }
        }
    }


    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }

}